package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.CompoundResource;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.AddActivityAction;

public class CompoundResourceEditor extends JFrame{
	
	private static final long serialVersionUID = -5754764148782001157L;
	private CompoundResource res;
	JList<IResource> list;
	DefaultListModel<IResource> model = new javax.swing.DefaultListModel<>();
	ArrayList<IResource> resources = new ArrayList<>();
	JComboBox<IResource> comboBox;

	public CompoundResourceEditor (CompoundResource res, JList<IResource> sourceList){
		this.res=res;
		resources=extractResources(res, sourceList);
		for (IResource r: res.getConsistingResources()){
			model.addElement(r);
		}
		setupGUI();
		
	}
	
	private ArrayList<IResource> extractResources (CompoundResource exclude, JList<IResource> sourceList){
		ArrayList<IResource>result = new ArrayList<>();
		
		for (int i = 0;i<sourceList.getModel().getSize();i++){
			if(!sourceList.getModel().getElementAt(i).equals(exclude))
				//if(!(sourceList.getModel().getElementAt(i) instanceof CompoundResource))
					result.add(sourceList.getModel().getElementAt(i));
			
		}
		
		return result;
		
	}
	
	private void setupGUI(){
		setSize(new Dimension(400, 300));
		setLayout(new BorderLayout());
		setTitle(res.getName()+" detail");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		list = new JList<>(model);
		JScrollPane pane = new JScrollPane(list);
		add(pane, BorderLayout.CENTER);
		add(getButtons(), BorderLayout.SOUTH);
	}
	
	private JPanel getButtons(){
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
		bottom.add(addActivityButton());
		bottom.add(Box.createHorizontalStrut(5));
		bottom.add(removeActivityBtn());
		return bottom;
	}
	
	private JButton addActivityButton(){
		JButton btn = new JButton("+");
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog((Component) e.getSource(), getDialogPanel(), "Add resource...",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION && comboBox.getSelectedItem()!=null && comboBox.getSelectedItem()instanceof IResource) {
					if(res.addResource((IResource) comboBox.getSelectedItem(), true)){
						model.addElement((IResource) comboBox.getSelectedItem());
					}
					
				} else {
					JOptionPane.showMessageDialog((Component) e.getSource(), "Resource not in Resource-Store");
				}
				
			}
		});
		
		return btn;
	}
	
	private JButton removeActivityBtn(){
		JButton btn = new JButton("-");
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				IResource remove=list.getSelectedValue();
				res.removeResource(remove.getName());
				model.removeElement(remove);
			}
		});
		return btn;
	}
	
	private JPanel getDialogPanel() {
		JPanel panel = new JPanel();
		if (resources != null)
			comboBox = new JComboBox<>(getHints());
		else
			comboBox = new JComboBox<>();
		comboBox.setEditable(true);
		panel.add(comboBox);
		return panel;
	}
	
	private IResource[] getHints() {
		try {
			int i = 0;
			IResource[] array = new IResource[resources.size()];
			for (IResource res:resources){
				array[i]=res;
				i++;
			}
			return array;
		} catch (NullPointerException e) {
			return null;
		}
	}

}
