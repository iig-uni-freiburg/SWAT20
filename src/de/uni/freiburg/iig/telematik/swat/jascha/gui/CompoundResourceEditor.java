package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.CompoundResource;

public class CompoundResourceEditor extends JFrame{
	
	private static final long serialVersionUID = -5754764148782001157L;
	private CompoundResource res;
	JList<IResource> list;
	DefaultListModel<IResource> model = new javax.swing.DefaultListModel<>();
	ArrayList<IResource> resources = new ArrayList<>();

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
	}

}
