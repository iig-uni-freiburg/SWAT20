package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import de.invation.code.toval.file.FileUtils;
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
	
	private String workingDirectory = null;
	private NewWorkingDirectoryAction newDirectoryAction = null;
	private OpenWorkingDirectoryAction openDirectoryAction = null;
	
	private List<String> directories = new ArrayList<String>();

	public WorkingDirectoryDialog(Window owner) {
		super(owner);
		setResizable(false);
		setBounds(100, 100, 369, 345);
		setModal(true);
		setLocationRelativeTo(owner);
		setTitle("Please choose a working directory");
		
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
		scrollPane.setBounds(20, 20, 327, 206);
		contentPanel.add(scrollPane);
		scrollPane.setViewportView(getValueList());
		
		JButton btnExistingDirectory = new JButton(openDirectoryAction);
		//btnExistingDirectory.setBounds(45, 264, 157, 29);
		btnExistingDirectory.setBounds(20, 238, 160, 29);
		contentPanel.add(btnExistingDirectory);
		
		JButton btnNewButton = new JButton(newDirectoryAction);
		//btnNewButton.setBounds(214, 264, 133, 29);
		btnNewButton.setBounds(193, 238, 155, 29);
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
						workingDirectory = directories.get(stringList.getSelectedIndex());
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
					workingDirectory = null;
					dispose();
				}
			});
			btnCancel.setActionCommand("Cancel");
		}
		return btnCancel;
	}
	
	private JList getValueList(){
		if(stringList == null){
			stringList = new JList(stringListModel){
				private static final long serialVersionUID = -5571466323700430126L;

				@Override
				public String getToolTipText(MouseEvent event) {
					int index = locationToIndex(event.getPoint());
					if(index != -1){
						return directories.get(index);
					}
					return super.getToolTipText(event);
				}
				
			};
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
		stringListModel.clear();
		try {
			for(String knownDirectory: SwatProperties.getInstance().getKnownWorkingDirectories()){
				stringListModel.addElement(FileUtils.getDirName(knownDirectory));
				directories.add(knownDirectory);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(WorkingDirectoryDialog.this, "Cannot extract known simulation directories.\nReason: "+e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String getSimulationDirectory(){
		return workingDirectory;
	}
	
	
	public static String showDialog(Window owner){
		WorkingDirectoryDialog activityDialog = new WorkingDirectoryDialog(owner);
		return activityDialog.getSimulationDirectory();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() instanceof NewWorkingDirectoryAction || evt.getSource() instanceof OpenWorkingDirectoryAction){
			if(evt.getPropertyName().equals(AbstractWorkingDirectoryAction.PROPERTY_NAME_WORKING_DIRECTORY)){
				this.workingDirectory = evt.getNewValue().toString();
			} else if(evt.getPropertyName().equals(AbstractWorkingDirectoryAction.PROPERTY_NAME_SUCCESS)){
				dispose();
			}
		}
	}
}
