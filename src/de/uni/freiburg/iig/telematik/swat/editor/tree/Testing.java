package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.awt.*;

import javax.swing.*;

public class Testing extends JFrame {
	public Testing() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel p = new JPanel(new BorderLayout());
		JTextField tf = new JTextField(5);
		ImageIcon placeIcon = new ImageIcon(getClass().getResource("/images/ellipse.png"));
		JLabel label = new JLabel(placeIcon);
		label.setOpaque(true);
		label.setBackground(tf.getBackground());
		label.setPreferredSize(new Dimension(label.getPreferredSize().width, tf.getPreferredSize().height));
		p.setBorder(tf.getBorder());
		tf.setBorder(null);
		p.add(label, BorderLayout.WEST);
		p.add(tf, BorderLayout.CENTER);
		JPanel p1 = new JPanel();
		p1.add(p);
		getContentPane().add(p1);
		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Testing().setVisible(true);
	}
}
