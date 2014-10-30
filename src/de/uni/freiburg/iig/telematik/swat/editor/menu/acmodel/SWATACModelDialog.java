package de.uni.freiburg.iig.telematik.swat.editor.menu.acmodel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import de.invation.code.toval.graphic.dialog.ValueEditingDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.properties.ACMValidationException;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.lattice.RoleLattice;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.lattice.graphic.RoleLatticeDialog;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.lattice.graphic.RoleMembershipDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;



public class SWATACModelDialog extends JDialog {
	
	private static final long serialVersionUID = -5216821409053567193L;

	private final JPanel contentPanel = new JPanel();
	
	private JTextArea textArea = null;
	
	private JButton btnEditRoleMembership = null;
	private JButton btnEditRoleLattice = null;
	private JButton btnEditPermissions = null;
	private JButton btnSubjects = null;
	private JButton btnActivities = null;
	private JButton btnAttributes = null;
	private JButton btnAdd = null;
	private JButton btnOK = null;
	private JButton btnCancel = null;
	
	private JRadioButton radioACL = null;
	private JRadioButton radioRBAC = null;
	
	private JCheckBox chckbxPropagateRights = null;
	
	private JComboBox comboACModel = null;
	
	//---------------------------------------------------
	
	private SWATContextForAC context = null;
	private ACModel acModel = null;

	public SWATACModelDialog(Window owner, SWATContextForAC context) throws ParameterException {
		super(owner);
		setTitle("Access Control Model");
		Validate.notNull(context);
		this.context = context;
		this.acModel = context.getACModel();
		setBounds(100, 100, 400, 504);
		setModal(true);
		setLocationRelativeTo(owner);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		contentPanel.add(getComboACModel());
		
		contentPanel.add(getButtonAdd());
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		radioACL = new JRadioButton("Access Control List");
		radioACL.setSelected(acModel == null || acModel instanceof ACLModel);
		radioACL.setBounds(20, 19, 181, 23);
		radioACL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateModelType();
			}
		});
		buttonGroup.add(radioACL);
		contentPanel.add(radioACL);
		
		radioRBAC = new JRadioButton("RBAC Model");
		radioRBAC.setBounds(196, 19, 181, 23);
		radioRBAC.setSelected(acModel != null && acModel instanceof RBACModel);
		radioRBAC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateModelType();
			}
		});
		buttonGroup.add(radioRBAC);
		contentPanel.add(radioRBAC);
		{
			JLabel lblAcModel = new JLabel("AC Model:");
			lblAcModel.setBounds(30, 82, 71, 16);
			contentPanel.add(lblAcModel);
		}
		
		JSeparator separator = new JSeparator();
		separator.setBounds(20, 54, 357, 12);
		contentPanel.add(separator);
		contentPanel.add(getButtonEditPermissions());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 120, 360, 200);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPanel.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		textArea.setBorder(new EmptyBorder(0, 0, 0, 0));

		
		
		chckbxPropagateRights = new JCheckBox("Propagate rights along lattice");
		chckbxPropagateRights.setBounds(161, 326, 217, 23);
		chckbxPropagateRights.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if ((acModel != null) && (acModel instanceof RBACModel)) {
					((RBACModel) acModel).setRightsPropagation(chckbxPropagateRights.isSelected());
					updateTextArea();
				}
			}

		});
//		contentPanel.add(chckbxPropagateRights);
		
		btnEditRoleMembership = new JButton("Edit role membership");
		btnEditRoleMembership.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RoleMembershipDialog(SWATACModelDialog.this, (RBACModel) acModel);
				updateTextArea();
			}
		});
		btnEditRoleMembership.setEnabled(false);
		btnEditRoleMembership.setBounds(20, 360, 181, 29);
		
		contentPanel.add(btnEditRoleMembership);
		
		contentPanel.add(getButtonEditRoleLattice());
		
//		contentPanel.add(getButtonActivities());
	
		contentPanel.add(getButtonSubjects());
		
//		contentPanel.add(getButtonAttributes());
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.add(getButtonOK());
		getRootPane().setDefaultButton(getButtonOK());
		buttonPane.add(getButtonCancel());
			
		
		updateModelType();
		setVisible(true);
	}
	
	private JComboBox getComboACModel(){
		if(comboACModel == null){
			comboACModel = new JComboBox();
			comboACModel.setBounds(102, 78, 190, 27);
			comboACModel.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) {
					try {
						String acModelName = null;
						if(comboACModel.getSelectedItem() != null)
							acModelName = comboACModel.getSelectedItem().toString();
						if(acModelName != null){
							acModel = SwatComponents.getInstance().getACModel(acModelName);
						} else {
							acModel = null;
						}
					} catch (ParameterException e1) {
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "Cannot update view.", "Internal Exception", JOptionPane.ERROR_MESSAGE);
						return;
					} 
					updateTextArea();
				}
				
			});
		}
		return comboACModel;
	}
	
	private JButton getButtonEditPermissions(){
		if(btnEditPermissions == null){
			btnEditPermissions = new JButton("Edit Permissions");
			if(acModel!=null){
				btnEditPermissions.setEnabled(acModel.hasActivities() && acModel.hasSubjects());
			}
			btnEditPermissions.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(acModel == null)
						return;
					try {
						if(acModel instanceof ACLModel){
							SWATACLDialog.showDialog(SWATACModelDialog.this, "Edit subject permissions", (ACLModel) acModel, SWATACModelDialog.this.context);
						} else {
							SWATACLDialog.showDialog(SWATACModelDialog.this, "Edit role permissions", ((RBACModel) acModel).getRolePermissions(), SWATACModelDialog.this.context);
						}
					}catch(Exception ex){
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "<html>Cannot launch permission dialog.<br>Reason: "+ex.getMessage()+"</html>", "Internal Exception", JOptionPane.ERROR_MESSAGE);
					}
					updateTextArea();
				}
			});
			btnEditPermissions.setBounds(20, 325, 139, 29);
		}
		return btnEditPermissions;
	}
	
	private JButton getButtonEditRoleLattice(){
		if(btnEditRoleLattice == null){
			btnEditRoleLattice = new JButton("Edit role lattice");
			btnEditRoleLattice.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						new RoleLatticeDialog(SWATACModelDialog.this, ((RBACModel) acModel).getRoleLattice());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "<html>Cannot launch role lattice dialog:<br>Reason: "+e1.getMessage()+"</html>", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
					}
					updateTextArea();
				}
			});
			btnEditRoleLattice.setEnabled(false);
			btnEditRoleLattice.setBounds(212, 361, 165, 29);
		}
		return btnEditRoleLattice;
	}
	
	private JButton getButtonAdd(){
		if(btnAdd == null){
			btnAdd = new JButton("Add");
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object acModelName = JOptionPane.showInputDialog(SWATACModelDialog.this, "Please enter a name for the new Access Control model", "New Access Control model", JOptionPane.QUESTION_MESSAGE, null, null, "ACModel");
					if(acModelName == null){
						return;
					}
					if(SwatComponents.getInstance().containsACModel((String) acModelName)){
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "There is already an access control model with this name.", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					try {
						if(radioACL.isSelected()){
							addNewACLModel((String) acModelName);
						} else {
							addNewRBACModel((String) acModelName);
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "Cannot add new access control model.\nReason: "+e1.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
				}
			});
			btnAdd.setBounds(294, 77, 84, 29);
		}
		return btnAdd;
	}
	
	private JButton getButtonOK(){
		if(btnOK == null){
			btnOK = new JButton("OK");
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(acModel == null){
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "Please choose an Access control model.", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						acModel.checkValidity();
					} catch (ACMValidationException e2) {
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "<html>Chosen Access Control Model is not valid.<br>Reason: "+e2.getMessage()+"</html>", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
//						SwatComponents.getInstance().storeACModel(acModel);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "<html>Access Control Model cannot be stored to disk.<br>Reason: "+e1.getMessage()+"</html>", "Internal Exception", JOptionPane.ERROR_MESSAGE);
						return;
					}
					SWATACModelDialog.this.dispose();
				}
			});
			btnOK.setActionCommand("OK");
		}
		return btnOK;
	}
	
	private JButton getButtonCancel(){
		if(btnCancel == null){
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acModel = null;
					SWATACModelDialog.this.dispose();
				}
			});
			btnCancel.setActionCommand("Cancel");
		}
		return btnCancel;
	}
	
	private JButton getButtonSubjects(){
		if(btnSubjects == null){
			btnSubjects = new JButton("Subjects...");
			btnSubjects.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Set<String> editedSubjects = null;
					try {
						editedSubjects = ValueEditingDialog.showDialog(SWATACModelDialog.this, "AC Model Subjects", acModel.getSubjects());
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(SWATACModelDialog.this, "<html>Cannot launch value editing dialog.<br>Reason: "+e2.getMessage()+"</html>", "Internal Exception", JOptionPane.ERROR_MESSAGE);
					}
					if(editedSubjects != null){
						if(editedSubjects.isEmpty()){
							JOptionPane.showMessageDialog(SWATACModelDialog.this, "Cannot remove all subjects from access control model.", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
							return;
						}
						Set<String> subjectsToRemove = new HashSet<String>(acModel.getSubjects());
						subjectsToRemove.removeAll(editedSubjects);
						if(!subjectsToRemove.isEmpty()){

							try {
								acModel.removeSubjects(subjectsToRemove);
							} catch (ParameterException e1) {
								JOptionPane.showMessageDialog(SWATACModelDialog.this, "Cannot remove subjects from access control model\nReason: " + e1.getMessage(), "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
						try {
							acModel.setSubjects(editedSubjects);
						} catch (ParameterException e1) {
							JOptionPane.showMessageDialog(SWATACModelDialog.this, "Cannot set subjects of access control model\nReason: " + e1.getMessage(), "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
							return;
						}
						updateTextArea();
					} else {
						//The user cancelled the dialog.
						//Do nothing.
					}
				}
			});
			btnSubjects.setBounds(20, 395, 110, 29);
		}
		return btnSubjects;
	}

	private void addNewACLModel(String name) throws ParameterException, IOException, PropertyException {
//		ACLModel newACLModel = new ACLModel(name);
		ACLModel newACLModel = new ACLModel(name);
		newACLModel.setSubjects(SWATACModelDialog.this.context.getSubjects());
		if(SWATACModelDialog.this.context.hasAttributes())
			newACLModel.setObjects(SWATACModelDialog.this.context.getAttributes());
		newACLModel.setActivities(SWATACModelDialog.this.context.getActivities());

		//Abklären mit Schreiben
		try {
			SwatComponents.getInstance().addACModel(newACLModel, true);
		} catch (SwatComponentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateACModelComboBox(newACLModel.getName());
		updateVisibility();
		updateTextArea();
	}
	
	private void addNewRBACModel(String name) throws ParameterException, IOException, PropertyException {
		RoleLattice roleLattice = null;
		try {
			roleLattice = RoleLatticeDialog.showDialog(SWATACModelDialog.this);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(SWATACModelDialog.this, "<html>Cannot launch role lattice dialog:<br>Reason: "+e.getMessage()+"</html>", "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
		}
		if(roleLattice != null){
			RBACModel newRBACModel = new RBACModel(name, roleLattice);
			newRBACModel.setSubjects(SWATACModelDialog.this.context.getSubjects());

			if(SWATACModelDialog.this.context.hasActivities()){
				newRBACModel.setActivities(SWATACModelDialog.this.context.getActivities());
			}
			try {
				SwatComponents.getInstance().addACModel(newRBACModel, true);
			} catch (SwatComponentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateACModelComboBox(newRBACModel.getName());
			updateVisibility();
			updateTextArea();
		}
	}
	
	private void updateModelType(){
		if(acModel != null){
			updateACModelComboBox(acModel.getName());
		} else {
			updateACModelComboBox(null);
		}
		if(comboACModel.getItemCount() > 0){
			try {
				acModel = SwatComponents.getInstance().getACModel((String) comboACModel.getSelectedItem());
			} catch (ParameterException e) {
				JOptionPane.showMessageDialog(SWATACModelDialog.this, "Cannot extract ac model from simulation components.", "Internal Exception", JOptionPane.ERROR_MESSAGE);
				return;
			}
			updateVisibility();
		}
		updateTextArea();
	}
	
	private void updateVisibility(){
		if(acModel != null && acModel instanceof RBACModel){
			btnEditRoleLattice.setEnabled(true);
			btnEditRoleMembership.setEnabled(true);
			chckbxPropagateRights.setEnabled(true);
			chckbxPropagateRights.setSelected(((RBACModel) acModel).propagatesRights());
		} else {
			btnEditRoleLattice.setEnabled(false);
			btnEditRoleMembership.setEnabled(false);
			chckbxPropagateRights.setEnabled(false);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void updateACModelComboBox(String modelName){
		
		DefaultComboBoxModel theModel = (DefaultComboBoxModel) comboACModel.getModel();
		theModel.removeAllElements();
		Class aclModelType = radioACL.isSelected() ? ACLModel.class : RBACModel.class;
		
		for(ACModel acModel: SwatComponents.getInstance().getACModels()){
			if(aclModelType.isInstance(acModel)){
				theModel.addElement(acModel.getName());
			}
		}

		if(modelName != null){
			comboACModel.setSelectedItem(modelName);
		}
		updateTextArea();
	}
	
	private void updateTextArea(){
		textArea.setText("");
		if(acModel != null){
			textArea.setText(acModel.toString());
		}

			if(acModel!= null)
				try {
					SwatComponents.getInstance().storeACModel(acModel);
				} catch (SwatComponentException e) {
					JOptionPane.showMessageDialog(this, "AC-Model"+acModel.getName()+"could not be stored. \nReason: "+e.getMessage(), "Swat Component Exception", JOptionPane.ERROR);

				}

	}
	
	public ACModel getACModel(){
		return acModel;
	}
	
	
	public static ACModel showDialog(Window owner, SWATContextForAC context) throws ParameterException{
		SWATACModelDialog activityDialog = new SWATACModelDialog(owner, context);
		return activityDialog.getACModel();
	}
}
