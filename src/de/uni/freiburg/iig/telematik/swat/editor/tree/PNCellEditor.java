package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
//	private JTree tree;

	@Override
	public Component getTreeCellEditorComponent(final JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		Component container=super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
		
	       PNTreeNode node = (PNTreeNode) value;
	       Component result = null;
//	       this.tree = tree;
	       container.addFocusListener(new FocusListener() {

      			@Override
      			public void focusGained(FocusEvent arg0) {

      			}

      			@Override
      			public void focusLost(FocusEvent arg0) {
      				System.out.println("stop");
      				tree.stopEditing();
      			}

      		});
				
	
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
		 if(event.getSource() instanceof JTree){
		 JTree tree = (JTree)event.getSource(); 
		  PNTreeNode node = (PNTreeNode) tree.getLastSelectedPathComponent();
	            if(node!= null){
	            	switch(node.getFieldType()){
					case ARC:
						break;
					case ARCS:
						break;
					case LEAF:
						MouseEvent mouseEvent = (MouseEvent)event;
						Point mouseLocation = mouseEvent.getPoint();
						Rectangle rectLastSelected = tree.getPathBounds(new TreePath(node.getPath()));
						
						return rectContainsPoint(
						new Point((int) rectLastSelected.getMinX(),(int) rectLastSelected.getMinY()),
						new Point((int) rectLastSelected.getMaxX(), (int) rectLastSelected.getMaxY()),
						mouseLocation);
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
	            }}

		 return false;
	    }
	 
		//Check if cursor location lies in edited field
		    private boolean rectContainsPoint(Point start, Point end, Point point) {
		        return point.equals(max(start, min(end, point)));
		    }
		    private Point min(Point p1, Point p2) {
		        return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
		    }
		 
		    private Point max(Point p1, Point p2) {
		        return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
		    }

}
