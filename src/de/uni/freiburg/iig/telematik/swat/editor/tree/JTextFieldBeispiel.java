package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
 
public class JTextFieldBeispiel {
    public static void main(String[] args) {
        JFrame meinJFrame = new JFrame();
        meinJFrame.setTitle("JTextFieldBeispiel");
        meinJFrame.setSize(300, 150);
        JPanel panel = new JPanel();
 
        JLabel label = new JLabel("Ihr Name");
        panel.add(label);
 
        // Textfeld wird erstellt
        // Text und Spaltenanzahl werden dabei direkt gesetzt
        JTextField tfName = new JTextField("Paul Programmierer", 15);

        // Schriftfarbe wird gesetzt
        tfName.setForeground(Color.BLUE);
        // Hintergrundfarbe wird gesetzt
        tfName.setBackground(Color.YELLOW);
        // Textfeld wird unserem Panel hinzugefügt
        panel.add(tfName);
 
        JButton buttonOK = new JButton("OK");
        panel.add(buttonOK);
        tfName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyTyped(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
//					validateInput();
					System.out.println("enter");
					
					
				}
			}
			
		});
        meinJFrame.add(panel);
        meinJFrame.setVisible(true);
 
    }
}