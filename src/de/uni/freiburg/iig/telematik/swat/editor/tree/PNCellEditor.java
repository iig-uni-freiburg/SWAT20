package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

public class PNCellEditor extends DefaultCellEditor {


	private static final long serialVersionUID = -8137148608944410589L;

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
				super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	       PNTreeNode node = (PNTreeNode) value;
	       Component result = null;
	       switch (node.getFieldType()) {
           case LEAF:
        	   result = getTextPanel(node);
               break;
	       }
	       
	       return result;
	}

	public PNCellEditor(JTextField name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getCellEditorValue() {
System.out.println("jups");		
return super.getCellEditorValue();
	}

	private Component getTextPanel(PNTreeNode node) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JLabel label = new JLabel(node.toString());
		label.setSize(new Dimension(200, 30));
		panel.add(label, BorderLayout.LINE_START);
		panel.add(new JLabel(":"), BorderLayout.CENTER);
		panel.add(node.getTextfield(), BorderLayout.LINE_END);
		return panel;
	}
	 @Override  
	    public boolean isCellEditable(EventObject event)  
	    {  
		 System.out.println("isit?");
		 
		 JTree tree = (JTree)event.getSource();  
		  PNTreeNode node = (PNTreeNode) tree.getLastSelectedPathComponent();

	            if(node!= null){
	            	switch(node.getFieldType()){
					case ARC:
						break;
					case ARCS:
						break;
					case LEAF:
						return true;
					case PLACE:
						break;
					case PLACES:
						break;
					case ROOT:
						break;
					case TRANSITION:
						break;
					case TRANSITIONS:
						break;
					default:
						break;
	            	
	            	}
	            }

		 return false;
	    }

}
