package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.uni.freiburg.iig.telematik.swat.workbench.CustomListRenderer;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AbstractWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.NewWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.OpenWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


public class WorkingDirectoryDialog extends JDialog implements PropertyChangeListener {
	
	private static final long serialVersionUID = 2306027725394345926L;

	private final JPanel contentPanel = new JPanel();
	
	private JList stringList = null;
	private JButton btnOK = null;
	private JButton btnCancel = null;
	private DefaultListModel stringListModel = new DefaultListModel();
	
	private String simulationDirectory = null;
	private NewWorkingDirectoryAction newDirectoryAction = null;
	private OpenWorkingDirectoryAction openDirectoryAction = null;
	
	public static void main(String args[]) {
		//WorkingDirectoryDialog dialog = new WorkingDirectoryDialog(null);
		System.out.println(WorkingDirectoryDialog.showDialog(null));
	}

	public WorkingDirectoryDialog(Window owner) {
		super(owner);
		setResizable(false);
		setBounds(100, 100, 369, 365);
		setModal(true);
		setLocationRelativeTo(owner);
		
		newDirectoryAction = new NewWorkingDirectoryAction(this);
		newDirectoryAction.addPropertyChangeListener(this);
		openDirectoryAction = new OpenWorkingDirectoryAction(this);
		openDirectoryAction.addPropertyChangeListener(this);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(20, 101, 327, 151);
		contentPanel.add(scrollPane);
		scrollPane.setViewportView(getValueList());
		
		JTextArea txtrThereAreNo = new JTextArea();
		txtrThereAreNo.setBackground(UIManager.getColor("Panel.background"));
		txtrThereAreNo.setText("Please choose the working directory.\n\nWorking directories are used to store swat-related content.");
		txtrThereAreNo.setBounds(20, 16, 392, 64);
		contentPanel.add(txtrThereAreNo);
		
		JButton btnExistingDirectory = new JButton(openDirectoryAction);
		//btnExistingDirectory.setBounds(45, 264, 157, 29);
		btnExistingDirectory.setBounds(20, 264, 160, 29);
		contentPanel.add(btnExistingDirectory);
		
		JButton btnNewButton = new JButton(newDirectoryAction);
		//btnNewButton.setBounds(214, 264, 133, 29);
		btnNewButton.setBounds(193, 264, 155, 29);
		contentPanel.add(btnNewButton);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.add(getButtonOK());
		buttonPane.add(getButtonCancel());
		
		
		setVisible(true);
	}
	
	private JButton getButtonOK(){
		if(btnOK == null){
			btnOK = new JButton("OK");
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!stringListModel.isEmpty()){
						if(stringList.getSelectedValue() == null){
							JOptionPane.showMessageDialog(WorkingDirectoryDialog.this, "Please choose a working directory.", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
							return;
						}
						simulationDirectory = stringList.getSelectedValue().toString();
						dispose();
					} else {
						JOptionPane.showMessageDialog(WorkingDirectoryDialog.this, "No known entries, please create new working directory.", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
			btnOK.setActionCommand("OK");
			getRootPane().setDefaultButton(btnOK);
		}
		return btnOK;
	}
	
	private JButton getButtonCancel(){
		if(btnCancel == null){
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					simulationDirectory = null;
					dispose();
				}
			});
			btnCancel.setActionCommand("Cancel");
		}
		return btnCancel;
	}
	
	private JList getValueList(){
		if(stringList == null){
			stringList = new JList(stringListModel);
			stringList.setCellRenderer(new CustomListRenderer());
			stringList.setFixedCellHeight(20);
			stringList.setVisibleRowCount(10);
			stringList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			stringList.setBorder(null);
			updateValueList();
		}
		return stringList;
	}
	
	private void updateValueList(){
		try {
			for(String knownDirectory: SwatProperties.getInstance().getKnownWorkingDirectories()){
				stringListModel.addElement(knownDirectory);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(WorkingDirectoryDialog.this, "Cannot extract known simulation directories.\nReason: "+e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String getSimulationDirectory(){
		return simulationDirectory;
	}
	
	
	public static String showDialog(Window owner){
		WorkingDirectoryDialog activityDialog = new WorkingDirectoryDialog(owner);
		return activityDialog.getSimulationDirectory();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() instanceof NewWorkingDirectoryAction || evt.getSource() instanceof OpenWorkingDirectoryAction){
			if(evt.getPropertyName().equals(AbstractWorkingDirectoryAction.PROPERTY_NAME_WORKING_DIRECTORY)){
				this.simulationDirectory = evt.getNewValue().toString();
			} else if(evt.getPropertyName().equals(AbstractWorkingDirectoryAction.PROPERTY_NAME_SUCCESS)){
				dispose();
			}
		}
	}
}
