package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JOptionPaneMultiInput {
	
	String[] results;
	
	public static void main (String args[]){
		
		String[] params = {"field1","field2"};
		JOptionPaneMultiInput test = new JOptionPaneMultiInput(params);
		System.out.println(test.getResult(1));
		
	}
	 
	   public JOptionPaneMultiInput (String... fieldNames) {
		   
		   JTextField[] fields = new JTextField[fieldNames.length];
		   JLabel[] labels = new JLabel[fieldNames.length];
		   JPanel dialogPanel = new JPanel();
		   
		   for (int i = 0;i<fieldNames.length;i++){
			   dialogPanel.add(new JLabel(fieldNames[i]+": "));
			   fields[i]=new JTextField(5);
			   dialogPanel.add(fields[i]);
			   dialogPanel.add(Box.createHorizontalStrut(5));
		   }
		   

	      int result = JOptionPane.showConfirmDialog(null, dialogPanel, 
	               "New Resource", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	         results= new String[fieldNames.length];
	         for (int i = 0;i<fieldNames.length;i++){
	        	 results[i]=fields[i].getText();
	         }
	      }
	   }
	   
	   public String getResult(int index){
		   return results[index];
	   }
	}
