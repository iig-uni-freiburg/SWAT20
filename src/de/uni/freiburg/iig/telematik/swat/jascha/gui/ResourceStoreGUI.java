package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.ResourceDetailListener;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.addResourceAction;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.removeResourceAction;

public class ResourceStoreGUI extends JFrame implements ResourceStoreListener{

	private static final long serialVersionUID = 1L;
	ResourceStore resourceStore;
	JList<IResource> list;
	DefaultListModel<IResource> model = new javax.swing.DefaultListModel<>();
	JLabel nameOfStore;
	private final int width=600;
	private final int height=400;
	
	public static void main (String args[]){
		ResourceStore store = new ResourceStore();
		store.instantiateResource(ResourceType.SET, "Hammer",4);
		store.instantiateResource(ResourceType.SIMPLE, "Säge");
		store.instantiateResource(ResourceType.SIMPLE, "Holzbrett");
		store.setName("Test-Store");
		ResourceStoreGUI manager = new ResourceStoreGUI(store);
		manager.setVisible(true);
		
	}
	
	public ResourceStoreGUI(){
		resourceStore=new ResourceStore(); 
		resourceStore.addResourceStoreListener(this);
		setUpFrame();
	}
	
	public ResourceStoreGUI(ResourceStore store){
		resourceStore=store;
		resourceStore.addResourceStoreListener(this);
		setUpFrame();
	}
	
	private void setUpFrame(){
		setSize(width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Resource Manager");
		setUpElements();
	}
	
	private void setUpElements(){
		add(getTopPanel(),BorderLayout.CENTER);
		add(getPlusMinusButtons(),BorderLayout.PAGE_END);
		revalidate();
		pack();
	}
	
	private JPanel getTopPanel(){
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
		topPanel.setPreferredSize(new Dimension(width, height));
		topPanel.add(getNameLabel());
		topPanel.add(new JLabel("available resources: "));
		topPanel.add(getList());
		return topPanel;
	}
	
	private JLabel getNameLabel(){
		if(nameOfStore==null){
			nameOfStore=new JLabel("<html>Resource-Store: <b>"+resourceStore.getName()+"</b></html>");
			nameOfStore.setFont(new Font(nameOfStore.getFont().getFontName(), Font.PLAIN, nameOfStore.getFont().getSize()));
		}
		return nameOfStore;
	}
	
	private JScrollPane getList(){
		list = new JList<>();
		list.setModel(model);
		list.addMouseListener(new ResourceDetailListener());
		for(IResource res:resourceStore.getAllResources())
			model.addElement(res);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        return listScroller;
	}
	
	private JPanel getPlusMinusButtons(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(Box.createHorizontalGlue());
		panel.add(new JLabel("add or remove: "));
		panel.add(getPlusButton());
		panel.add(getRemoveButton());
		return panel;
	}
	
	private JButton getPlusButton(){
		JButton plus = new JButton("+");
		plus.setText("+");
		plus.setSize(new Dimension(50, 50));
		plus.addActionListener(new addResourceAction(resourceStore));
		return plus;
	}
	
	private JButton getRemoveButton(){
		JButton minus = new JButton("-");
		minus.setText("-");
		minus.setSize(new Dimension(50, 50));
		minus.addActionListener(new removeResourceAction(resourceStore, list));
		return minus;
	}

	@Override
	public void resourceStoreElementAdded(IResource res) {
		model.addElement(res);
	}

	@Override
	public void informStoreElementRemoved(IResource resource) {
		model.removeElement(resource);
	}

}
