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
		add(getAnotherButton());
		add(getPlusButton());
		
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
		JButton plus = new JButton("t");
		plus.setText("+");
		plus.setSize(new Dimension(50, 50));
		plus.addActionListener(new addResourceAction(resourceStore));
		//plus.setPreferredSize(new Dimension(10, 10));
		return plus;
	}
	
	private JButton getAnotherButton(){
		JButton another = new JButton("tgg");
		another.setText("+++");
		another.setSize(new Dimension(50, 50));
		//plus.setPreferredSize(new Dimension(10, 10));
		return another;
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
