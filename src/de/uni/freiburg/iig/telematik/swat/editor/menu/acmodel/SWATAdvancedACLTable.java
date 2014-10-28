package de.uni.freiburg.iig.telematik.swat.editor.menu.acmodel;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import de.invation.code.toval.graphic.renderer.VerticalTableHeaderCellRenderer;
import de.invation.code.toval.types.DataUsage;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.graphic.permission.ObjectPermissionItemEvent;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.graphic.permission.ObjectPermissionItemListener;



public class SWATAdvancedACLTable extends JTable implements ObjectPermissionItemListener, ItemListener{

	private static final long serialVersionUID = 223793804425867377L;

	private ACLModel aclModel = new ACLModel();
	
	private SWATActivityPermissionTableModel transactionModel = null;
	private SWATObjectPermissionTableModel objectModel = null;
	private VIEW currentView = VIEW.TRANSACTION;
	private boolean deriveAttributePermissions = false;
	private SWATContextForAC context;
	
	private SWATAdvancedACLTable(SWATContextForAC context) throws ParameterException{
		Validate.notNull(context);
		this.context = context;
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setBackground(new Color(243, 244, 244));
	}
	
	public SWATAdvancedACLTable(ACLModel aclModel, SWATContextForAC context) throws ParameterException{
		this(context);
		Validate.notNull(aclModel);
		
		this.aclModel = aclModel;
		this.setEnabled(deriveAttributePermissions);
		initialize(aclModel.getSubjects(), aclModel.getObjects(), aclModel.getActivities());
		update();
	}
	
	public SWATAdvancedACLTable(Set<String> subjects, SWATContextForAC context) throws ParameterException{
		this(context);
		Validate.notNull(subjects);
		Validate.noNullElements(subjects);
		
		aclModel = new ACLModel();
		aclModel.setSubjects(subjects);
		aclModel.setActivities(context.getActivities());
		if(context.hasAttributes())
			aclModel.setObjects(context.getAttributes());
		initialize(subjects, context.getActivities(), context.getAttributes());
	}
	
	private void initialize(Set<String> subjects, 
							Set<String> objects, 
							Set<String> transactions) throws ParameterException{
		
		Validate.notNull(subjects);
		Validate.notNull(objects);
		Validate.notNull(transactions);
		
		objectModel = new SWATObjectPermissionTableModel(aclModel);
		objectModel.addTableModelListener(this);
		objectModel.addPermissionItemListener(this);
		transactionModel = new SWATActivityPermissionTableModel(aclModel);
		transactionModel.addTableModelListener(this);
		transactionModel.addItemListener(this);
		update();
	}

	public void setDeriveAttributePermissions(boolean deriveAttributePermissions) {
		this.deriveAttributePermissions = deriveAttributePermissions;
		this.setEnabled(!deriveAttributePermissions);
		if(deriveAttributePermissions){
			for(String subject: aclModel.getSubjects()){
				deriveObjectPermission(subject);
			}
			objectModel.update();
		}
	}

	public void setView(VIEW view) throws ParameterException{
		Validate.notNull(view);
		currentView = view;
		update();
	}
	
	public void update(){
		int numCols = 0;
		if(currentView == VIEW.OBJECT){
			this.setModel(objectModel);
			numCols = objectModel.getColumnCount();
			if(deriveAttributePermissions){
				objectModel.update();
				this.setEnabled(false);
			}
		} else {
			this.setModel(transactionModel);
			numCols = transactionModel.getColumnCount();
			this.setEnabled(true);
		}
		TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
		
		int preferredCellWidth = 0;
		if(getModel() instanceof SWATObjectPermissionTableModel){
			setRowHeight(((SWATObjectPermissionTableModel) getModel()).preferredCellSize().height);
			preferredCellWidth = ((SWATObjectPermissionTableModel) getModel()).preferredCellSize().width;
		} else {
			setRowHeight(((SWATActivityPermissionTableModel) getModel()).preferredCellSize().height);
			preferredCellWidth = ((SWATActivityPermissionTableModel) getModel()).preferredCellSize().width;
		}
		
		for(int i=0; i<numCols; i++){
			getColumnModel().getColumn(i).setWidth(preferredCellWidth);
			getColumnModel().getColumn(i).setMinWidth(preferredCellWidth);
			getColumnModel().getColumn(i).setMaxWidth(preferredCellWidth);
			getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
			if(i == 0){
				getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer());
			} else {
				getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
				getColumnModel().getColumn(i).setCellEditor(new CustomCellEditor(new JCheckBox()));
			}
		}
		getColumnModel().getColumn(0).setWidth(100);
		getColumnModel().getColumn(0).setMinWidth(100);
		getColumnModel().getColumn(0).setMaxWidth(100);
		getColumnModel().getColumn(0).setPreferredWidth(100);
	}
	
	public ACLModel getACLModel(){
		return aclModel;
	}
	
	@Override
	public void permissionChanged(ObjectPermissionItemEvent e) {
		if(currentView == VIEW.OBJECT){
		if(getSelectedRow() >= 0 && getSelectedColumn() >=0){
		JCheckBox checkBox = (JCheckBox) e.getSource();
		boolean permissionActivated = checkBox.isSelected();
		String subject = ((SWATObjectPermissionTableModel) getModel()).getRowName(getSelectedRow());
		String object = getModel().getColumnName(getSelectedColumn());
				
		try{
        	if(permissionActivated){
        		aclModel.addObjectPermission(subject, object, e.getDataUsageMode());
        	} else {
        		aclModel.removeObjectPermissions(subject, object, e.getDataUsageMode());
        	}
		} catch(ParameterException ex){
			ex.printStackTrace();
		}
		}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(currentView == VIEW.TRANSACTION){
		if(getSelectedRow() >= 0 && getSelectedColumn() >=0){
		JCheckBox checkBox = (JCheckBox) e.getSource();
		boolean permissionActivated = checkBox.isSelected();
		String subject = ((SWATActivityPermissionTableModel) getModel()).getRowName(getSelectedRow());
		String transaction = getModel().getColumnName(getSelectedColumn());
		try{
			if(permissionActivated){
				aclModel.addActivityPermission(subject, transaction);
				if(deriveAttributePermissions){
					Map<String, Set<DataUsage>> transactionDataUsage = context.getDataUsageFor(transaction);
					for(String object: transactionDataUsage.keySet()){
						aclModel.addObjectPermission(subject, object, transactionDataUsage.get(object));
					}
				}
			} else {
				aclModel.removeActivityPermission(subject, transaction);
				if(deriveAttributePermissions){
					deriveObjectPermission(subject);
				}
			}
		} catch(ParameterException ex){
			ex.printStackTrace();
		}
		}
		}
	}
	
	private void deriveObjectPermission(String subject){
		try{
			aclModel.removeObjectPermissions(subject);
			for(String authorizedTransaction: aclModel.getAuthorizedTransactionsForSubject(subject)){
				Map<String, Set<DataUsage>> transactionDataUsage = context.getDataUsageFor(authorizedTransaction);
				for(String object: transactionDataUsage.keySet()){
					aclModel.addObjectPermission(subject, object, transactionDataUsage.get(object));
				}
			}
		} catch(ParameterException e){
			e.printStackTrace();
		}
	}
	
	public class CustomCellRenderer implements TableCellRenderer{

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component comp = (Component) value;
			if (isSelected) {
				comp.setForeground(table.getSelectionForeground());
				comp.setBackground(table.getSelectionBackground());
			} else {
				comp.setForeground(table.getForeground());
				comp.setBackground(table.getBackground());
			}
			return comp;
		}
		
	}
	
	public class CustomCellEditor extends DefaultCellEditor {

		private static final long serialVersionUID = -5452183629220317768L;

		public CustomCellEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			return (Component) value;
		}
		
	}
	
	public enum VIEW {OBJECT, TRANSACTION}

}
