package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
 

public class PNTreeNodeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -7829208008630231526L;
	private ImageIcon placeIcon
       = new ImageIcon(getClass().getResource("/images/ellipse.png"));
   private ImageIcon transitionIcon
       = new ImageIcon(getClass().getResource("/images/rectangle.png"));
   private ImageIcon arcIcon
       = new ImageIcon(getClass().getResource("/images/arrow.png"));
   private ImageIcon rootIcon
       = new ImageIcon(getClass().getResource("/images/cloud.png"));

    
   public Component getTreeCellRendererComponent(
           JTree tree,
           Object value,
           boolean sel,
           boolean expanded,
           boolean leaf,
           int row,
           boolean hasFocus) {
       super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
       
       PNTreeNode node = (PNTreeNode) value;
       Component result = this;
       switch (node.getFieldType()) {
   	case ROOT:
   		setIcon(new ImageIcon(rootIcon.getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)));
		break;
	case PLACE:
		setIcon(new ImageIcon(placeIcon.getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)));
		break;
	case TRANSITION:
		setIcon(new ImageIcon(transitionIcon.getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)));
		break;
	case ARC:
		setIcon(new ImageIcon(arcIcon.getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)));
		break;
	case LEAF:
		result = getTextPanel(node);
		break;
	default:
		break;


       }
       
       return result;
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

}