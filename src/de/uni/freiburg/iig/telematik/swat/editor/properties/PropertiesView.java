package de.uni.freiburg.iig.telematik.swat.editor.properties;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphSelectionModel;

import de.invation.code.toval.graphic.RestrictedTextField;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.algorithm.coloring.GraphColoring;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView.PropertiesField;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeBuilder;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNCellEditor;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeModel;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNodeRenderer;

public class PropertiesView extends JPanel implements PNPropertiesListener, mxIEventListener, TreeSelectionListener {

	private static final long serialVersionUID = 1L;

	protected Map<String, HashMap<PNProperty, PropertiesField>> placeFields = new HashMap<String, HashMap<PNProperty, PropertiesField>>();
	protected Map<String, HashMap<PNProperty, PropertiesField>> transitionFields = new HashMap<String, HashMap<PNProperty, PropertiesField>>();
	protected Map<String, HashMap<PNProperty, PropertiesField>> arcFields = new HashMap<String, HashMap<PNProperty, PropertiesField>>();

	public JTree tree;

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	protected PNProperties properties = null;

	private PNTreeNode rootNode;

	public PropertiesView(PNProperties properties) throws ParameterException {
		Validate.notNull(properties);
		this.properties = properties;
		setUpGUI();
	}

	protected void setUpGUI() throws ParameterException {
		for (String placeName : properties.getPlaceNames()) {
			createFieldsForPlace(placeName);

		}
		for (String transitionName : properties.getTransitionNames()) {
			createFieldsForTransition(transitionName);

		}
		for (String arcName : properties.getArcNames()) {
			createFieldsForArc(arcName);

		}

		rootNode = PNTreeBuilder.build(placeFields, transitionFields, arcFields);
		TreeModel model = new PNTreeModel(rootNode);

		tree = new JTree(model);
		tree.setInvokesStopCellEditing(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);

		// tree.setInvokesStopCellEditing(false);
		// Set Editor for Property Fields
		JTextField textField = new JTextField();
		textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		PNCellEditor editor = new PNCellEditor(textField);
		tree.setCellEditor(editor);
		tree.setEditable(true);

		PNTreeNodeRenderer renderer = new PNTreeNodeRenderer();
		tree.setCellRenderer(renderer);
		 tree.addTreeSelectionListener(this);
		add(new JScrollPane(tree), BorderLayout.CENTER);

		// expand all nodes in the tree to be visible
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}

	}

	private void createFieldsForPlace(String placeName) throws ParameterException {
		HashMap<PNProperty, PropertiesField> placeField = new HashMap<PNProperty, PropertiesField>();
		for (PNProperty placeProperty : properties.getPlaceProperties()) {
			createPlaceField(placeName, placeProperty, placeField);
		}
	}

	private void createFieldsForTransition(String transitionName) throws ParameterException {
		HashMap<PNProperty, PropertiesField> transitionField = new HashMap<PNProperty, PropertiesField>();
		for (PNProperty transitionProperty : properties.getTransitionProperties()) {
			createTransitionField(transitionName, transitionProperty, transitionField);
		}
	}

	private void createFieldsForArc(String arcName) throws ParameterException {
		HashMap<PNProperty, PropertiesField> arcField = new HashMap<PNProperty, PropertiesField>();
		for (PNProperty arcProperty : properties.getArcProperties()) {
			createArcField(arcName, arcProperty, arcField);
		}
	}

	private void createPlaceField(String placeName, PNProperty placeProperty, HashMap<PNProperty, PropertiesField> placeField) throws ParameterException {
		placeField.put(placeProperty, new PropertiesField(PNComponent.PLACE, placeName, properties.getValue(PNComponent.PLACE, placeName, placeProperty), placeProperty));
		placeFields.put(placeName, placeField);

	}

	private void createTransitionField(String transitionName, PNProperty transitionProperty, HashMap<PNProperty, PropertiesField> transitionField) throws ParameterException {
		transitionField.put(transitionProperty, new PropertiesField(PNComponent.TRANSITION, transitionName, properties.getValue(PNComponent.TRANSITION, transitionName, transitionProperty),
				transitionProperty));
		transitionFields.put(transitionName, transitionField);
	}

	private void createArcField(String arcName, PNProperty arcProperty, HashMap<PNProperty, PropertiesField> arcField) throws ParameterException {
		arcField.put(arcProperty, new PropertiesField(PNComponent.ARC, arcName, properties.getValue(PNComponent.ARC, arcName, arcProperty), arcProperty));
		arcFields.put(arcName, arcField);
	}

	/**
	 * This method is called each time a value is changed within one of the
	 * textfields.
	 * 
	 * @param fieldType
	 * @param name
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	protected void propertiesFieldValueChanged(PNComponent fieldType, String name, PNProperty property, String oldValue, String newValue) {
		try {
			properties.setValue(this, fieldType, name, property, newValue);
		} catch (ParameterException e1) {
			switch (fieldType) {
			case PLACE:
				placeFields.get(name).get(property).setText(oldValue);
				break;
			case TRANSITION:
				transitionFields.get(name).get(property).setText(oldValue);
				break;
			case ARC:
				arcFields.get(name).get(property).setText(oldValue);
				break;
			}
		}
	}

	@Override
	public void propertyChange(PNPropertyChangeEvent event) {
		if (event.getSource() != this) {
			switch (event.getFieldType()) {
			case PLACE:
				placeFields.get(event.getName()).get(event.getProperty()).setText(event.getNewValue().toString());
				tree.repaint();
				break;
			case TRANSITION:
				transitionFields.get(event.getName()).get(event.getProperty()).setText(event.getNewValue().toString());
				break;
			case ARC:
				arcFields.get(event.getName()).get(event.getProperty()).setText(event.getNewValue().toString());
				break;
			}
		}
	}

	public class PropertiesField extends RestrictedTextField {

		private static final long serialVersionUID = -2791152505686200734L;

		private PNComponent type = null;
		private PNProperty property = null;
		private String name = null;

		public PropertiesField(PNComponent type, String name, String text, PNProperty property) {
			super(property.getRestriction(), text);
			this.type = type;
			this.property = property;
			this.name = name;
			// this.addMouseListener(new java.awt.event.MouseAdapter() {
			// public void mouseClicked(java.awt.event.MouseEvent evt) {
			// System.out.println("juhu");
			// }
			// });

			// private void
			// jTextFieldMyTextMouseClicked(java.awt.event.MouseEvent evt) {
			// jTextFieldMyText.setText("");
			// }

		}
		
		public PNProperty getPNProperty(){
			return property;
		}

		@Override
		protected void valueChanged(String oldValue, String newValue) {
			propertiesFieldValueChanged(type, name, property, oldValue, newValue);
		}

		@Override
		public void setBorder(Border border) {
			// Remove Border from Textfield
		}

	}

	@Override
	public void componentAdded(PNComponent component, String name) {
		try {
			switch (component) {
			case PLACE:
				createFieldsForPlace(name);
				// TODO: Add place fields to GUI
				break;
			case TRANSITION:
				createFieldsForTransition(name);
				// TODO: Add transition fields to GUI
				break;
			case ARC:
				createFieldsForArc(name);
				// TODO: Add arc fields to GUI
				break;
			}
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void componentRemoved(PNComponent component, String name) {
		switch (component) {
		case PLACE:
			placeFields.remove(name);
			// TODO: Remove place fields from GUI
			break;
		case TRANSITION:
			transitionFields.remove(name);
			// TODO: Remove transition fields from GUI
			break;
		case ARC:
			arcFields.remove(name);
			// TODO: Remove arc fields from GUI
			break;
		}
	}

	class LeafCellEditor extends DefaultTreeCellEditor {

		public LeafCellEditor(JTree tree, DefaultTreeCellRenderer renderer, TreeCellEditor editor) {
			super(tree, renderer, editor);
		}

		public boolean isCellEditable(EventObject event) {
			boolean returnValue = super.isCellEditable(event);
			if (returnValue) {
				Object node = tree.getLastSelectedPathComponent();
				if ((node != null) && (node instanceof TreeNode)) {
					TreeNode treeNode = (TreeNode) node;
					returnValue = treeNode.isLeaf();
				}
			}
			return returnValue;
		}
	}

	
	public PNTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(PNTreeNode rootNode) {
		this.rootNode = rootNode;
	}

	@Override
	public void invoke(Object sender, mxEventObject evt) {
		System.out.println("THE Sender:" + sender);
		if (sender instanceof JTree) {
			System.out.println(tree.getSelectionPath());
		}
		if (sender instanceof mxGraphSelectionModel) {
			System.out.println("PropertiesVIEW");

			System.out.println("ROWS:" + tree.getRowCount());
			for (int i = getTree().getRowCount(); i >= 0; i--) {
				getTree().collapseRow(i);
			}
			if (((mxGraphSelectionModel) sender).getCell() instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) ((mxGraphSelectionModel) sender).getCell();
				DefaultMutableTreeNode node = find((DefaultMutableTreeNode) tree.getModel().getRoot(), cell.getId());

				PNTreeNode firstChild = (PNTreeNode) ((PNTreeNode) node).getChildAt(0);

				TreePath propPath = new TreePath(firstChild.getPath());
tree.collapsePath(propPath);
				tree.setSelectionPath(new TreePath(node.getPath()));

			}
		}
	}

	private DefaultMutableTreeNode find(DefaultMutableTreeNode root, String s) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			if (node.toString().equalsIgnoreCase(s)) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(0);
				return node;
			}
		}
		return null;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
	JTree 	tree = (JTree) e.getSource();
		
	}

}

// mxEvent.CHANGE

class CustomTreeModelListener implements TreeModelListener {
	public void treeNodesChanged(TreeModelEvent e) {
		PNTreeNode node;
		node = (PNTreeNode) (e.getTreePath().getLastPathComponent());

		/*
		 * If the event lists children, then the changed node is the child of
		 * the node we have already gotten. Otherwise, the changed node and the
		 * specified node are the same.
		 */
		try {
			int index = e.getChildIndices()[0];
			node = (PNTreeNode) (node.getChildAt(index));
		} catch (NullPointerException exc) {
		}

		System.out.println("The user has finished editing the node.");
		// System.out.println("New value: " + node.getUserObject());
	}

	public void treeNodesInserted(TreeModelEvent e) {
	}

	public void treeNodesRemoved(TreeModelEvent e) {
	}

	public void treeStructureChanged(TreeModelEvent e) {
	}

}
