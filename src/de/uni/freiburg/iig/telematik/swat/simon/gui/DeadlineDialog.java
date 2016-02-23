package de.uni.freiburg.iig.telematik.swat.simon.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.timeSimulation.ContextRepo;

public class DeadlineDialog extends JFrame implements TableModelListener{

	private static final long serialVersionUID = 6404895406985931834L;
	
	private final static int width = 550;
	private final static int height = 450;
	AwesomeTimeContext context;
	String[] columnNames={"Net","Deadline in ms"};
	JTable table;
	DefaultTableModel model = new DefaultTableModel(columnNames,0);
	
	public static void main(String[] args) {
		DeadlineDialog dialog = new DeadlineDialog((AwesomeTimeContext) ContextRepo.getTimeContext());
		dialog.setVisible(true);
	}
	
	public DeadlineDialog(AwesomeTimeContext context) {
		this.context=context;
		setup();
	}

	private void setup() {
		setLocationByPlatform(true);
		this.setSize(width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		add(getBottomPanel(), BorderLayout.SOUTH);
		fillTable();
		model.addTableModelListener(this);
	}
	
	private JPanel getBottomPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton addActivity = new JButton("add activity");
		addActivity.addActionListener(new ActionListener() {
			
			JComboBox<String> comboBox = new JComboBox<>();
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog((Component) e.getSource(), getDialogPanel(), "Add activity...", JOptionPane.OK_CANCEL_OPTION);
				
				if (result == JOptionPane.OK_OPTION && comboBox.getSelectedItem()!=null) {
					if(!context.containsDeadlineFor((String) (comboBox.getSelectedItem()))){
						Object[] row = {comboBox.getSelectedItem(),"0.0"};
						model.addRow(row);
					}
					else
						JOptionPane.showMessageDialog((Component) e.getSource(), "activity already present");
				}
			}
			
			public JPanel getDialogPanel() {
				JPanel panel = new JPanel();
				if (getHints() != null)
					comboBox = new JComboBox<>(getHints());
				else
					comboBox = new JComboBox<>();
				comboBox.setEditable(true);
				panel.add(comboBox);
				return panel;
			}
			

		});
		
		
		JButton removeEntry = new JButton("remove");
		removeEntry.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				context.removeDeadline((String) model.getValueAt(0, table.getSelectedColumn()));
				fillTable();
			}
		});
		
		panel.add(addActivity);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(removeEntry);
		
		return panel;
	}
	
	public String[] getHints(){
		String[] hints = new String[context.getKnownActivities().size()];
		int i=0;
		for (String s: context.getKnownActivities()){
			hints[i]=s;
			i++;
		}
		return hints;
	}

	private void fillTable() {
		model.setRowCount(0); //reset model
		for (String s: context.getAvailableDeadlines()){
			Vector<Object> v = new Vector<>(2);
			v.addElement(s);
			v.addElement(context.getDeadlineFor(s));
			model.addRow(v);
		}
		
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		if(row==e.getLastRow()){
			try{
			String activity = (String) model.getValueAt(row, 0);
			double time = Double.parseDouble((String) model.getValueAt(row, 1));
			context.setDeadline(activity, time);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Cannot parse "+ex.getMessage()+"in row "+row);
			}
		}
	}

}
