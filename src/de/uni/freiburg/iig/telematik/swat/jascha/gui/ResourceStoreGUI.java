package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.addResourceAction;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.removeResourceAction;

public class ResourceStoreGUI extends JFrame implements ResourceStoreListener{

	private static final long serialVersionUID = 1L;
	ResourceStore resourceStore;
	JList<IResource> list;
	DefaultListModel<IResource> model = new javax.swing.DefaultListModel<>();
	
	public static void main (String args[]){
		ResourceStore store = new ResourceStore();
		store.instantiateResource(ResourceType.SET, "Hammer",4);
		store.instantiateResource(ResourceType.SIMPLE, "Säge");
		store.instantiateResource(ResourceType.SIMPLE, "Holzbrett");
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
		int width=600;
		int height=400;
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Resource Manager");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setUpElements();
	}
	
	private void setUpElements(){
		add(new JLabel("available resources: "));
		add(updateList());
		add(getPlusButton());
		add(getRemoveButton());
		
	}
	
	private JScrollPane updateList(){
		list = new JList<>();
		list.setModel(model);
		for(IResource res:resourceStore.getAllResources())
			model.addElement(res);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        return listScroller;
	}
	
	private JButton getPlusButton(){
		JButton plus = new JButton("+");
		plus.setText("+");
		plus.setSize(new Dimension(50, 50));
		plus.addActionListener(new addResourceAction(resourceStore));
		//plus.setPreferredSize(new Dimension(10, 10));
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
