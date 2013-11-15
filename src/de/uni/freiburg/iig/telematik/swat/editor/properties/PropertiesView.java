package de.uni.freiburg.iig.telematik.swat.editor.properties;

import java.awt.BorderLayout;
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
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import de.invation.code.toval.graphic.RestrictedTextField;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView.PropertiesField;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeBuilder;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeCellEditor;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeModel;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNodeRenderer;



public class PropertiesView extends JPanel implements PNPropertiesListener, TreeSelectionListener{

	private static final long serialVersionUID = 1L;
	
	protected Map<String, HashMap<PNProperty, PropertiesField>> placeFields = new HashMap<String,HashMap<PNProperty,PropertiesField>>();
	protected Map<String, HashMap<PNProperty, PropertiesField>> transitionFields = new HashMap<String,HashMap<PNProperty,PropertiesField>>();
	protected Map<String, HashMap<PNProperty, PropertiesField>> arcFields = new HashMap<String,HashMap<PNProperty,PropertiesField>>();
	
	private JTree tree;
	
	protected PNProperties properties =  null;

	public PropertiesView(PNProperties properties) throws ParameterException{
		Validate.notNull(properties);
		this.properties = properties;
		setUpGUI();
	}
	
	protected void setUpGUI() throws ParameterException{
		for(String placeName: properties.getPlaceNames()){
			createFieldsForPlace(placeName);
	
		}
		for(String transitionName: properties.getTransitionNames()){
				createFieldsForTransition(transitionName);
			
		}
		for(String arcName: properties.getArcNames()){
				createFieldsForArc(arcName);
			
		}
		

	        final PNTreeNode rootNode = PNTreeBuilder.build(placeFields, transitionFields, arcFields);
	        TreeModel model = new PNTreeModel(rootNode);
	        tree = new JTree(model);
	       
	        //Set Editor for Property Fields
	        JTextField textField = new JTextField();
	        TreeCellEditor editor = new PNTreeCellEditor(textField);
	        tree.setEditable(true);
	        tree.setCellEditor(editor);
	        
	        PNTreeNodeRenderer renderer = new PNTreeNodeRenderer();
	        tree.setCellRenderer(renderer);
	        tree.addTreeSelectionListener(this);
	        add(new JScrollPane(tree), BorderLayout.CENTER);
	        
	        // expand all nodes in the tree to be visible
	        for (int i = 0; i < tree.getRowCount(); i++) {
	            tree.expandRow(i);
	        }
		
	}
	
	private void createFieldsForPlace(String placeName) throws ParameterException{
		HashMap<PNProperty, PropertiesField> placeField = new HashMap<PNProperty, PropertiesField>();
		for(PNProperty placeProperty: properties.getPlaceProperties()){
			createPlaceField(placeName, placeProperty, placeField);
		}
	}
	
	private void createFieldsForTransition(String transitionName) throws ParameterException{
		HashMap<PNProperty, PropertiesField> transitionField = new HashMap<PNProperty, PropertiesField>();
		for(PNProperty transitionProperty: properties.getTransitionProperties()){
			createTransitionField(transitionName, transitionProperty, transitionField);
		}
	}
	
	private void createFieldsForArc(String arcName) throws ParameterException{
		HashMap<PNProperty, PropertiesField> arcField = new HashMap<PNProperty, PropertiesField>();
		for(PNProperty arcProperty: properties.getArcProperties()){
			createArcField(arcName, arcProperty, arcField);
		}
	}

	private void createPlaceField(String placeName, PNProperty placeProperty, HashMap<PNProperty, PropertiesField> placeField) throws ParameterException{
		placeField.put(placeProperty, new PropertiesField(PNComponent.PLACE, placeName, properties.getValue(PNComponent.PLACE, placeName, placeProperty), placeProperty));
		placeFields.put(placeName,placeField);

	}
	
	private void createTransitionField(String transitionName, PNProperty transitionProperty, HashMap<PNProperty, PropertiesField> transitionField) throws ParameterException{
		transitionField.put(transitionProperty, new PropertiesField(PNComponent.TRANSITION, transitionName, properties.getValue(PNComponent.TRANSITION, transitionName, transitionProperty), transitionProperty));
		transitionFields.put(transitionName, transitionField);
	}
	
	private void createArcField(String arcName, PNProperty arcProperty, HashMap<PNProperty, PropertiesField> arcField) throws ParameterException{
		arcField.put(arcProperty, new PropertiesField(PNComponent.ARC, arcName, properties.getValue(PNComponent.ARC, arcName, arcProperty), arcProperty));	
		arcFields.put(arcName, arcField);
	}
		
		
	
	/**
	 * This method is called each time a value is changed within one of the textfields.
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
			switch(fieldType){
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
		if(event.getSource() != this){
			switch(event.getFieldType()){
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
		}

		@Override
		protected void valueChanged(String oldValue, String newValue) {
			propertiesFieldValueChanged(type, name, property, oldValue, newValue);
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
		switch(component){
		case PLACE:
			placeFields.remove(name);
			//TODO: Remove place fields from GUI
			break;
		case TRANSITION:
			transitionFields.remove(name);
			//TODO: Remove transition fields from GUI
			break;
		case ARC:
			arcFields.remove(name);
			//TODO: Remove arc fields from GUI
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
    @Override
    public void valueChanged(TreeSelectionEvent e) {
       Object node = tree.getLastSelectedPathComponent();
       System.out.println("HALLO");
       if (node == null) {
           return;
       }
        
//       JOptionPane.showMessageDialog(this, "You have selected: " + node);
    }

}

class CustomTreeModelListener implements TreeModelListener {
    public void treeNodesChanged(TreeModelEvent e) {
    	PNTreeNode node;
        node = (PNTreeNode)
                 (e.getTreePath().getLastPathComponent());

        /*
         * If the event lists children, then the changed
         * node is the child of the node we have already
         * gotten.  Otherwise, the changed node and the
         * specified node are the same.
         */
        try {
            int index = e.getChildIndices()[0];
            node = (PNTreeNode)
                   (node.getChildAt(index));
        } catch (NullPointerException exc) {}

        System.out.println("The user has finished editing the node.");
//        System.out.println("New value: " + node.getUserObject());
    }
    public void treeNodesInserted(TreeModelEvent e) {
    }
    public void treeNodesRemoved(TreeModelEvent e) {
    }
    public void treeStructureChanged(TreeModelEvent e) {
    }

}
