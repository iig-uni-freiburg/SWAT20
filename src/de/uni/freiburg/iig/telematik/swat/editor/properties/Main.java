package de.uni.freiburg.iig.telematik.swat.editor.properties;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import de.invation.code.toval.graphic.DisplayFrame;
import de.uni.freiburg.iig.telematik.jagal.graph.GraphUtils;

public class Main {
  public static void main(String[] argv) throws Exception {
    int rows = 10;
    int cols = 5;
JPanel panel = new JPanel();
    final JTable table = new JTable(rows, cols);

    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    table.setColumnSelectionAllowed(true);
    table.setRowSelectionAllowed(false);
    table.setCellEditor(new DefaultCellEditor(new JTextField()));
	table.getCellEditor().addCellEditorListener(new CellEditorListener() {
		
		@Override
		public void editingStopped(ChangeEvent arg0) {
System.out.println("editing stopped");						
		}
		
		@Override
		public void editingCanceled(ChangeEvent arg0) {
System.out.println("Cancelled");						
		}
	});
	table.addFocusListener(new FocusListener() {
		
		@Override
		public void focusLost(FocusEvent e) {
System.out.println("lost");	
table.clearSelection();
		}
		
		@Override
		public void focusGained(FocusEvent e) {
System.out.println("gain");

		}
	});
  
    table.clearSelection();
    panel.add(table);
    JTable table2 = new JTable(rows, cols);
    table2.setCellEditor(new DefaultCellEditor(new JTextField()));
    table2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    table2.setColumnSelectionAllowed(true);
    table2.setRowSelectionAllowed(false);
   
	table2.getCellEditor().addCellEditorListener(new CellEditorListener() {
		
		@Override
		public void editingStopped(ChangeEvent arg0) {
System.out.println("editing stopped2");						
		}
		
		@Override
		public void editingCanceled(ChangeEvent arg0) {
System.out.println("Cancelled2");						
		}
	});
	
    table2.clearSelection();
    panel.add(table2);
    new DisplayFrame(panel, true);
    
    
  }
}