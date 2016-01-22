package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**Joption pane with multiple input fields determined by stringarray**/
public class JOptionPaneMultiInput {
	
	String[] results;
	boolean hasUserInput=false;
	
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
			   fields[i]=new JTextField(6);
			   dialogPanel.add(fields[i]);
			   dialogPanel.add(Box.createHorizontalStrut(5));
		   }
		   

	      int result = JOptionPane.showConfirmDialog(null, dialogPanel, 
	               "New Resource", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	    	  hasUserInput=true;
	         results= new String[fieldNames.length];
	         for (int i = 0;i<fieldNames.length;i++){
	        	 results[i]=fields[i].getText();
	         }
	      }
	   }
	   
	   public String getResult(int index){
		   return results[index];
	   }
	   
	   /**Asume inputs are double values**/
	   public List<Double> getResultAsDouble(){
		   LinkedList<Double> result = new LinkedList<>();
		   for (int i = 0;i<results.length;i++){
			   result.add(Double.parseDouble(results[i]));
		   }
		   return result;
	   }
	   
	   public boolean hasUserInput(){
		   return hasUserInput;
	   }
	}
