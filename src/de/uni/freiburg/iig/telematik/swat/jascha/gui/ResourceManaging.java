package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;

public class ResourceManaging extends JFrame{
	
	ResourceStore resourceStore;
	JList<IResource> list;
	
	public static void main (String args[]){
		ResourceStore store = new ResourceStore();
		store.instantiateResource(ResourceType.SIMPLE, "bla 1");
		store.instantiateResource(ResourceType.SIMPLE, "Werkzeug2");
		store.instantiateResource(ResourceType.SIMPLE, "Hammer3");
		store.instantiateResource(ResourceType.SIMPLE, "bla 2");
		store.instantiateResource(ResourceType.SIMPLE, "Werkzeug 3");
		store.instantiateResource(ResourceType.SIMPLE, "Hammer 5");
		ResourceManaging manager = new ResourceManaging(store);
		manager.setVisible(true);
	}
	
	public ResourceManaging(){
		resourceStore=new ResourceStore(); 
		setUpFrame();
	}
	
	public ResourceManaging(ResourceStore store){
		resourceStore=store;
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
		list = new JList<>(resourceStore.getAllResources());
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

}
