package de.uni.freiburg.iig.telematik.swat.editor.properties;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphSelectionModel;

import de.invation.code.toval.graphic.RestrictedTextField;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView.PropertiesField;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNCellEditor;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNodeRenderer;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNodeType;

public class PropertiesView extends JTree implements PNPropertiesListener, mxIEventListener, TreeSelectionListener, TreeModelListener {


	private static final long serialVersionUID = -23504178961013201L;

	protected PNProperties properties = null;

	private PNTreeNode rootNode;

	private PNTreeNode root;
	private DefaultTreeModel treeModel;

	public PropertiesView(PNProperties properties) throws ParameterException {
		Validate.notNull(properties);
		root = new PNTreeNode("root", PNTreeNodeType.ROOT);
		
		



		addTreeSelectionListener(this);
		
		// expand all nodes in the tree to be visible
		for (int i = 0; i < this.getRowCount(); i++) {
			this.expandRow(i);
		}

		this.properties = properties;
		setUpGUI();

		treeModel = new DefaultTreeModel(root);
		this.setModel(treeModel);
		 setInvokesStopCellEditing(false);
		 getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		 setRootVisible(false);
		
		 // tree.setInvokesStopCellEditing(false);
		 // Set Editor for Property Fields
		 JTextField textField = new JTextField();
		 textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		 PNCellEditor editor = new PNCellEditor(textField);
		 setCellEditor(editor);
		 setEditable(true);
		 setRowHeight(0);
		 PNTreeNodeRenderer renderer = new PNTreeNodeRenderer();
		 setCellRenderer(renderer);
		 addTreeSelectionListener(this);
		 getModel().addTreeModelListener(this);
//		 add(new JScrollPane(this), BorderLayout.CENTER);
		
		 // expand all nodes in the tree to be visible
		 for (int i = 0; i < getRowCount(); i++) {
		 expandRow(i);
		 }

	}

	protected void setUpGUI() throws ParameterException {
		PNTreeNode placesNode = new PNTreeNode("Places", PNTreeNodeType.PLACES);
		PNTreeNode transitionsNode = new PNTreeNode("Transitions", PNTreeNodeType.TRANSITIONS);
		PNTreeNode arcsNode = new PNTreeNode("Arcs", PNTreeNodeType.ARCS);
		
		for (String placeName : properties.getPlaceNames()) {
			placesNode.add(createFields(placeName, PNComponent.PLACE,  PNTreeNodeType.PLACE ));
		}
		for (String transitionName : properties.getTransitionNames()) {
			transitionsNode.add(createFields(transitionName, PNComponent.TRANSITION,  PNTreeNodeType.TRANSITION ));
		}
		for (String arcName : properties.getArcNames()) {
			arcsNode.add(createFields(arcName, PNComponent.ARC,  PNTreeNodeType.ARC ));
		}
		
		root.add(placesNode);
		root.add(transitionsNode);
		root.add(arcsNode);
		
	}
	
	private PNTreeNode createFields(String nodeName, PNComponent pnProperty, PNTreeNodeType nodeType) throws ParameterException {
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
//		Util.asSortedList(propertiesSet);
		DefaultTableModel tableModel = 	new DefaultTableModel();
		tableModel.setColumnCount(2);
		for (PNProperty property : list) {
		PropertiesField field = new PropertiesField(pnProperty, nodeName, properties.getValue(pnProperty, nodeName, property), property);
		tableModel.addRow(new Object[]{property, field});
		switch(property){
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
		
		
		JTable table = new JTable(tableModel);
        TableColumnModel colModel = table.getColumnModel();
        TableColumn col1 = colModel.getColumn(1);
        col1.setCellRenderer(new CustomRenderer());
        col1.setCellEditor(new CustomEditor());
        TableColumn col0 = colModel.getColumn(0);
//        col0.setCellRenderer(new CustomRenderer());
        col0.setCellEditor(new CustomEditor2());
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
//        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        table.addMouseListener(new MouseListener() {
//			
//			private boolean isOutside;
//
//			@Override
//			public void mouseReleased(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mousePressed(MouseEvent event) {
//				System.out.println("released");
//				MouseEvent mouseEvent = (MouseEvent) event;
//				Point mouseLocation = mouseEvent.getPoint();
//				if(isOutside){
//					Rectangle rectLastSelected = table.getBounds();
//				System.out.println(rectContainsPoint(new Point((int) rectLastSelected.getMinX(), (int) rectLastSelected.getMinY()),
//						new Point((int) rectLastSelected.getMaxX(), (int) rectLastSelected.getMaxY()), mouseLocation));
//				}
//				
//			}
//			
//			@Override
//			public void mouseExited(MouseEvent arg0) {
//				isOutside = true;
//System.out.println("exit");				
//			}
//			
//			@Override
//			public void mouseEntered(MouseEvent event) {
//				System.out.println("entered");
//				MouseEvent mouseEvent = (MouseEvent) event;
//				Point mouseLocation = mouseEvent.getPoint();
//				if(isOutside){
//					Rectangle rectLastSelected = table.getBounds();
//				System.out.println(rectContainsPoint(new Point((int) rectLastSelected.getMinX(), (int) rectLastSelected.getMinY()),
//						new Point((int) rectLastSelected.getMaxX(), (int) rectLastSelected.getMaxY()), mouseLocation));
//				}				
//			}
//			
//			@Override
//			public void mouseClicked(MouseEvent event) {
//				System.out.println("Mouseclicked");
//				MouseEvent mouseEvent = (MouseEvent) event;
//				Point mouseLocation = mouseEvent.getPoint();
//				if(isOutside){
//					Rectangle rectLastSelected = table.getBounds();
//				System.out.println(rectContainsPoint(new Point((int) rectLastSelected.getMinX(), (int) rectLastSelected.getMinY()),
//						new Point((int) rectLastSelected.getMaxX(), (int) rectLastSelected.getMaxY()), mouseLocation));
//				}
//			}
//		});
    	table.addFocusListener(new FocusListener() {
    		
    		

			private Object gainedSource;

			@Override
    		public void focusLost(FocusEvent e) {
				System.out.println("GAIN:" + gainedSource);
				System.out.println("LOST:" + e.getSource());
    			if(gainedSource == e.getSource())
    				System.out.println("SAME");
    System.out.println("lost" + e.getSource());	
    
//    table.getSelectionModel().isSelectionEmpty();
//    System.out.println((table.getSelectedColumn() == 1));

//    table.clearSelection();
    		}
    		
    		@Override
    		public void focusGained(FocusEvent e) {
    			gainedSource = e.getSource();
    System.out.println("gain");

    		}
    	});


//table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//	
//	@Override
//	public void valueChanged(ListSelectionEvent e) {
//		System.out.println("TABLE:"+e.getSource());
//		
//	}
//});
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);

//        table.clearSelection();
		table.setBorder(BorderFactory.createLineBorder(table.getSelectionBackground()));
		table.setGridColor(table.getSelectionBackground());
        node.add(new PNTreeNode(table, PNTreeNodeType.LEAF));
				
		return node;
	}
	
	// Check if cursor location lies in edited field
	private boolean rectContainsPoint(Point start, Point end, Point point) {
		return point.equals(max(start, min(end, point)));
	}

	private Point min(Point p1, Point p2) {
		return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
	}

	private Point max(Point p1, Point p2) {
		return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
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
		PNTreeNode child = (PNTreeNode) find((DefaultMutableTreeNode) getModel().getRoot(), name).getFirstChild();
		int i =0;
		for(i=0; i <= child.getTable().getRowCount(); i++){
			if(property == child.getTable().getValueAt(i, 0))
				break;
		}
		((JTextField)child.getTable().getValueAt(i, 1)).setText(oldValue);
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
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
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

	@Override
	public void componentAdded(PNComponent component, String name) {
//		try {
			switch (component) {
			case PLACE:
//				createFieldsForPlace(name);
				// TODO: Add place fields to GUI
				break;
			case TRANSITION:
//				createFieldsForTransition(name);
				// TODO: Add transition fields to GUI
				break;
			case ARC:
//				createFieldsForArc(name);
				// TODO: Add arc fields to GUI
				break;
			}
//		} catch (ParameterException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void componentRemoved(PNComponent component, String name) {
		
		DefaultMutableTreeNode comp =  find((DefaultMutableTreeNode) getModel().getRoot(), name);
		treeModel.removeNodeFromParent(comp);

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
		if (sender instanceof JTree) {
		}
		if (sender instanceof mxGraphSelectionModel) {

			for (int i = getRowCount(); i >= 0; i--) {
				collapseRow(i);
			}
			if (((mxGraphSelectionModel) sender).getCell() instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) ((mxGraphSelectionModel) sender).getCell();
				DefaultMutableTreeNode node = find((DefaultMutableTreeNode) getModel().getRoot(), cell.getId());

				PNTreeNode firstChild = (PNTreeNode) ((PNTreeNode) node).getChildAt(0);

				TreePath propPath = new TreePath(firstChild.getPath());
				collapsePath(propPath);
				setSelectionPath(new TreePath(node.getPath()));

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
		JTree tree = (JTree) e.getSource();

	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		repaint();
		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

}

class CustomRenderer implements TableCellRenderer {
	JScrollPane scrollPane;
	JTextField textField;

	public CustomRenderer() {
		textField = new JTextField();

		scrollPane = new JScrollPane(textField);
		
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (value instanceof JTextField)
			textField = (JTextField) value;
		else
			textField.setText((String) value);		
		return textField;
	}
}

class CustomEditor implements TableCellEditor {
	JTextField textField;
	JScrollPane scrollPane;

	public CustomEditor() {
		textField = new JTextField();
		scrollPane = new JScrollPane(textField);
	}

	public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row, int column) {

		if (value instanceof JTextField)
			textField = (PropertiesField) value;
		else
			textField.setText((String) value);
		
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				table.clearSelection();

			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
				//select whole row
			}
		});
		return textField;
	}

	public void addCellEditorListener(CellEditorListener l) {
	}

	public void cancelCellEditing() {
	}

	public Object getCellEditorValue() {
		return textField.getText();
	}

	public boolean isCellEditable(EventObject anEvent) {
		
		return true;
	}

	public void removeCellEditorListener(CellEditorListener l) {
	}

	public boolean shouldSelectCell(EventObject anEvent) {
	
		return false;
	}

	public boolean stopCellEditing() {
		return true;
	}

//	@Override
//	public boolean isCellEditable(EventObject anEvent) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void removeCellEditorListener(CellEditorListener l) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean shouldSelectCell(EventObject anEvent) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean stopCellEditing() {
//		// TODO Auto-generated method stub
//		return false;
//	}
}


class CustomEditor2 implements TableCellEditor {

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}



}


// mxEvent.CHANGE

//class CustomTreeModelListener() implements TreeModelListener {
////	public CustomTreeModelListener(PropertiesView propertiesView) {
////		super.
////	}
//
//	public void treeNodesChanged(TreeModelEvent e) {
//		System.out.println(e.getSource() + "nodechanged");
//		PNTreeNode node;
//		node = (PNTreeNode) (e.getTreePath().getLastPathComponent());
//		
//		
////		PNTreeNode parent = (PNTreeNode) node.getParent();
////		switch(node.getPropertyType()){
////		case ARC_WEIGHT:
////			break;
////		case PLACE_LABEL:
////			System.out.println("juhu");
////			parent.setUserObject(node.getTextfield().getText());
////			break;
////		case PLACE_SIZE:
////			break;
////		case TRANSITION_LABEL:
////			break;
////		case TRANSITION_SIZE:
////			break;
////		default:
////			break;
////
////		
////		}
//		
//		/*
//		 * If the event lists children, then the changed node is the child of
//		 * the node we have already gotten. Otherwise, the changed node and the
//		 * specified node are the same.
//		 */
//		try {
//			int index = e.getChildIndices()[0];
//			node = (PNTreeNode) (node.getChildAt(index));
//		} catch (NullPointerException exc) {
//		}
//
//		System.out.println("The user has finished editing the node.");
//		// System.out.println("New value: " + node.getUserObject());
//	}
//
//	public void treeNodesInserted(TreeModelEvent e) {
//	}
//
//	public void treeNodesRemoved(TreeModelEvent e) {
//	}
//
//	public void treeStructureChanged(TreeModelEvent e) {
//	}
//
//}
