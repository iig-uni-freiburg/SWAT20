package de.uni.freiburg.iig.telematik.swat.editor.properties;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphSelectionModel;

import de.invation.code.toval.graphic.RestrictedTextField;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNCellEditor;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNodeRenderer;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNodeType;

public class PropertiesView extends JTree implements PNPropertiesListener, mxIEventListener {

	private static final long serialVersionUID = -23504178961013201L;

	protected PNProperties properties = null;

	// The Three basic ParentNodes of the tree
	PNTreeNode placesNode = new PNTreeNode("Places", PNTreeNodeType.PLACES);
	PNTreeNode transitionsNode = new PNTreeNode("Transitions", PNTreeNodeType.TRANSITIONS);
	PNTreeNode arcsNode = new PNTreeNode("Arcs", PNTreeNodeType.ARCS);

	private PNTreeNode rootNode;

	private PNTreeNode root;
	private DefaultTreeModel treeModel;

	public PropertiesView(PNProperties properties) throws ParameterException {
		Validate.notNull(properties);
		this.properties = properties;
		setUpGUI();

		// expand all nodes in the tree to be visible
		for (int i = 0; i < getRowCount(); i++) {
			expandRow(i);
		}

	}

	protected void setUpGUI() throws ParameterException {
		root = new PNTreeNode("root", PNTreeNodeType.ROOT);

		root.add(placesNode);
		root.add(transitionsNode);
		root.add(arcsNode);

		treeModel = new DefaultTreeModel(root);
		this.setModel(treeModel);
		setInvokesStopCellEditing(false);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setRootVisible(false);

		// Set Editor for Property Fields
		JTextField textField = new JTextField();
		textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		PNCellEditor editor = new PNCellEditor(textField);
		setCellEditor(editor);
		setEditable(true);
		setRowHeight(0);
		PNTreeNodeRenderer renderer = new PNTreeNodeRenderer();
		setCellRenderer(renderer);

		// add(new JScrollPane(this), BorderLayout.CENTER);

	}

	//Creates PropertiesFields of for the given Name
	
	private PNTreeNode createFields(String nodeName, PNComponent pnProperty, PNTreeNodeType nodeType) {
		PNTreeNode node = new PNTreeNode(nodeName, nodeType);
		Set<PNProperty> propertiesSet = null;
		switch (pnProperty) {
		case ARC:
			propertiesSet = properties.getArcProperties();
			break;
		case PLACE:
			propertiesSet = properties.getPlaceProperties();
			break;
		case TRANSITION:
			propertiesSet = properties.getTransitionProperties();
			break;
		}
		List<PNProperty> list = new ArrayList<PNProperty>(propertiesSet);
		Collections.sort(list);
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnCount(2);
		for (PNProperty property : list) {
			PropertiesField field = null;
			try {
				field = new PropertiesField(pnProperty, nodeName, properties.getValue(pnProperty, nodeName, property), property);
			} catch (ParameterException e1) {
				System.out.println("properties.getValue(...) Values could not be called");
				e1.printStackTrace();
			}
			tableModel.addRow(new Object[] { property, field });
			switch (property) {
			case ARC_WEIGHT:
				break;
			case PLACE_LABEL:
				node.setTextField(field);
				break;
			case TRANSITION_LABEL:
				node.setTextField(field);
				break;
			default:
				break;

			}
		}

		// Order of Properties corresponds to Order of PropertiesClass

		final JTable table = new JTable(tableModel);
		TableColumnModel colModel = table.getColumnModel();
		TableColumn col1 = colModel.getColumn(1);
		col1.setCellRenderer(new JTableRenderer());
		col1.setCellEditor(new EditorForPropertiesFieldColumn());
		TableColumn col0 = colModel.getColumn(0);
		col0.setCellEditor(new EditorForFirstColumn());
		table.setPreferredScrollableViewportSize(table.getPreferredSize());

		Object key = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(KeyStroke.getKeyStroke("ENTER"));
		final Action action = table.getActionMap().get(key);
		Action custom = new AbstractAction("wrap") {
			// Currently no reaction on ENTER
			@Override
			public void actionPerformed(ActionEvent e) {

				// Default behaviour on Enter
				// int row = table.getSelectionModel().getLeadSelectionIndex();
				// if (row == table.getRowCount() - 1) {
				// // do custom stuff
				// // return if default shouldn't happen or call default after
				// return;
				// }
				// action.actionPerformed(e);
			}

		};
		table.getActionMap().put(key, custom);

		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setBorder(BorderFactory.createLineBorder(table.getSelectionBackground()));
		table.setGridColor(table.getSelectionBackground());
		node.add(new PNTreeNode(table, PNTreeNodeType.LEAF));

		return node;
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
			case TRANSITION:
			case ARC:
				setPropertiesFieldValue(name, property, oldValue);
			}
		}
	}

	@Override
	public void propertyChange(PNPropertyChangeEvent event) {
		if (event.getSource() != this) {
			switch (event.getFieldType()) {
			case PLACE:
			case TRANSITION:
			case ARC:
				setPropertiesFieldValue(event.getName(), event.getProperty(), event.getNewValue().toString());
				repaint();
				break;
			}
		}
	}

	/**
	 * @param name
	 * @param property
	 * @param oldValue
	 */
	protected void setPropertiesFieldValue(String name, PNProperty property, String oldValue) {
		PNTreeNode child = (PNTreeNode) findTreeNodeByName((DefaultMutableTreeNode) getModel().getRoot(), name).getFirstChild();
		int i = 0;
		for (i = 0; i <= child.getTable().getRowCount(); i++) {
			if (property == child.getTable().getValueAt(i, 0))
				break;
		}
		((JTextField) child.getTable().getValueAt(i, 1)).setText(oldValue);
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
			this.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					super.keyReleased(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						stopEditing();
						clearSelection();
					}
				}

			});

		}

		protected PropertiesField getPropertyField() {
			return this;

		}

		public PNProperty getPNProperty() {
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

	// CURRENTLY NOT IN USE

	@Override
	public void componentAdded(PNComponent component, String name) {
		// try {
		switch (component) {
		case PLACE:
			// createFieldsForPlace(name);
			// TODO: Add place fields to GUI
			break;
		case TRANSITION:
			// createFieldsForTransition(name);
			// TODO: Add transition fields to GUI
			break;
		case ARC:
			// createFieldsForArc(name);
			// TODO: Add arc fields to GUI
			break;
		}
		// } catch (ParameterException e) {
		// e.printStackTrace();
		// }
	}

	// CURRENTLY NOT IN USE

	@Override
	public void componentRemoved(PNComponent component, String name) {

		DefaultMutableTreeNode comp = findTreeNodeByName((DefaultMutableTreeNode) getModel().getRoot(), name);
		treeModel.removeNodeFromParent(comp);

	}

	
	
	
	//Listens to different Events from the Graph to update the treestructure and selection

	@Override
	public void invoke(Object sender, mxEventObject evt) {
		if (evt.getName().equals(mxEvent.CELLS_ADDED)) {
			Object[] cells = (Object[]) evt.getProperty("cells");
			for (Object object : cells) {
				if (object instanceof PNGraphCell) {
					PNGraphCell cell = (PNGraphCell) object;
					switch (cell.getType()) {
					case PLACE:
						treeModel.insertNodeInto(createFields(cell.getId(), PNComponent.PLACE, PNTreeNodeType.PLACE), placesNode, placesNode.getChildCount());
						break;
					case TRANSITION:
						treeModel.insertNodeInto(createFields(cell.getId(), PNComponent.TRANSITION, PNTreeNodeType.TRANSITION), transitionsNode, transitionsNode.getChildCount());
						break;
					case ARC:
						treeModel.insertNodeInto(createFields(cell.getId(), PNComponent.ARC, PNTreeNodeType.ARC), arcsNode, arcsNode.getChildCount());
						break;
					}
				}
			}
		}
		
		
		
		if (evt.getName().equals(mxEvent.CELLS_REMOVED)) {
			Object[] cells = (Object[]) evt.getProperty("cells");
			for (Object object : cells) {
				if (object instanceof PNGraphCell) {
					PNGraphCell cell = (PNGraphCell) object;
					treeModel.removeNodeFromParent(findTreeNodeByName((DefaultMutableTreeNode) getModel().getRoot(), cell.getId()));
				}
			}

		}

		if (evt.getName().equals(mxEvent.REPAINT)) {
			repaint();
		}

		if (sender instanceof JTree) {
		}
		
		if (evt.getName().equals(mxEvent.CHANGE)) {
			if (sender instanceof mxGraphSelectionModel) {
				for (int i = getRowCount(); i >= 0; i--) {
					collapseRow(i);
				}

				if (((mxGraphSelectionModel) sender).getCell() instanceof PNGraphCell) {
					PNGraphCell cell = (PNGraphCell) ((mxGraphSelectionModel) sender).getCell();
					DefaultMutableTreeNode node = findTreeNodeByName((DefaultMutableTreeNode) getModel().getRoot(), cell.getId());

					PNTreeNode firstChild = (PNTreeNode) ((PNTreeNode) node).getChildAt(0);

					TreePath propPath = new TreePath(firstChild.getPath());
					collapsePath(propPath);
					setSelectionPath(new TreePath(node.getPath()));

				}
			}
		}
	}

	private DefaultMutableTreeNode findTreeNodeByName(DefaultMutableTreeNode root, String name) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			if (node.toString().equalsIgnoreCase(name)) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(0);
				return node;
			}
		}
		return null;
	}

}


