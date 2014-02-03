package de.uni.freiburg.iig.telematik.swat.editor.properties.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class PNTreeNodeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -7829208008630231526L;
	private ImageIcon placeIcon = new ImageIcon(getClass().getResource("/images/ellipse.png"));
	private ImageIcon transitionIcon = new ImageIcon(getClass().getResource("/images/rectangle.png"));
	private ImageIcon arcIcon = new ImageIcon(getClass().getResource("/images/arrow.png"));
	private ImageIcon rootIcon = new ImageIcon(getClass().getResource("/images/cloud.png"));

	@Override
	public Component getTreeCellRendererComponent(final JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component container = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		final PNTreeNode node = (PNTreeNode) value;

		container.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {

			}

			@Override
			public void focusLost(FocusEvent arg0) {
				tree.stopEditing();
			}

		});

		Component result = this;
		switch (node.getFieldType()) {

		case ARCS:
			setIcon(new ImageIcon(arcIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			break;
		case PLACES:
			setIcon(new ImageIcon(placeIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			break;
		case TRANSITIONS:
			setIcon(new ImageIcon(transitionIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			break;
		default:
			break;
		case ROOT:
			setIcon(new ImageIcon(rootIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			break;
		case PLACE:
			setIcon(new ImageIcon(placeIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
//			setText(node.getTextfield().getText()); // Overrides NAME with LABEL
//			keepSelectionWhileEditing(tree, node);
			break;
		case TRANSITION:
			setIcon(new ImageIcon(transitionIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
//			setText(node.getTextfield().getText()); // Overrides NAME with LABEL
//			keepSelectionWhileEditing(tree, node);
			break;
		case ARC:
			setIcon(new ImageIcon(arcIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
//			setText(node.getTextfield().getText());
//			keepSelectionWhileEditing(tree, node);
			break;
		case LEAF:
			result = node.getTable();
			break;

		}

		return result;
	}

	
	//Überdenken wann genau Node highlighted sein soll
	/**
	 * @param tree
	 * @param node
	 */
	public void keepSelectionWhileEditing(JTree tree, PNTreeNode node) {
		DefaultMutableTreeNode child = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (child != null && child.isLeaf() && child.getParent() == node){
			selected = true;
			}
//		if (child != null && child.isLeaf() && child.getParent() != node){
//			selected = false;
//			}
//		if (child != null && !child.isLeaf() && child == node){
//			selected = true;
//			}
//		if (child != null && !child.isLeaf() && child != node){
//			selected = false;
//			}
	}

	private Component getTextPanel(PNTreeNode node) {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BorderLayout());
		JLabel label = new JLabel(node.toString() + ": ");
		label.setSize(new Dimension(200, 30));
		panel.add(label, BorderLayout.LINE_START);
		panel.add(node.getTextfield(), BorderLayout.LINE_END);
		return panel;
	}

}