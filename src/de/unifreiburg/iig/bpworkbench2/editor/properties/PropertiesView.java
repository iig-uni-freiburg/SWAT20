package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.invation.code.toval.validate.ParameterException;
import de.unifreiburg.iig.bpworkbench2.editor.soul.GraphProperties;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;



public class PropertiesView extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField label;
	private JTextField size_x;
	protected PTProperties properties =  null;

	public PropertiesView(PTProperties properties){
		this.properties = properties;
//		setName("Properties");
//		JMenuItem menuItem = new JMenuItem("Edit Place");
//		add(menuItem);
//		
//		label = new JTextField("name");
//		label.addActionListener(new java.awt.event.ActionListener() {
//	            public void actionPerformed(java.awt.event.ActionEvent e) {
//	            	System.out.println(((JTextField)e.getSource()).getText() + "########");
//	    			try {
//						GraphProperties.getInstance().setName(((JTextField)e.getSource()).getText());
//					} catch (ParameterException e1) {
//						e1.printStackTrace();
//					}
//
//	            }
//	        });
//add(label);
//
//
//size_x = new JTextField("xxxx");
//size_x.addActionListener(new java.awt.event.ActionListener() {
//        public void actionPerformed(java.awt.event.ActionEvent e) {
//        	System.out.println(((JTextField)e.getSource()).getText() + "########");
//			try {
//				GraphProperties.getInstance().setSizeX(((JTextField)e.getSource()).getText());
//			} catch (ParameterException e1) {
//				e1.printStackTrace();
//			}
//
//        }
//    });
//
//add(size_x);




	}
   
}
