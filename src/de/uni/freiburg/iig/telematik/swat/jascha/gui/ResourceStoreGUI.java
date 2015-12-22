package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.thoughtworks.xstream.XStream;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;
import de.uni.freiburg.iig.telematik.swat.jascha.fileHandling.ResourceStoreContainer;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.ChangeNamedComponentAction;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.ResourceDetailAction;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.SaveResourceStoreAction;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.addResourceAction;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.removeResourceAction;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

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
		ResourceStoreGUI manager = new ResourceStoreGUI();
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
		setPreferredSize(new Dimension(width, height));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Resource Store Editor");
		setUpElements();
	}
	
	private void setUpElements(){
		add(getNameLabel(),BorderLayout.PAGE_START);
		add(getList(),BorderLayout.CENTER);
		add(getPlusMinusButtons(),BorderLayout.PAGE_END);
		revalidate();
		pack();
	}
	
	public JPanel getAsPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(getNameLabel(),BorderLayout.PAGE_START);
		panel.add(getList(),BorderLayout.CENTER);
		panel.add(getPlusMinusButtons(),BorderLayout.PAGE_END);
		return panel;
	}
	
	private Box getNameLabel(){
		Box horizontal = Box.createHorizontalBox();
		if(nameOfStore==null){
			nameOfStore=new JLabel("<html>Resource-Store: <b>"+resourceStore.getName()+"</b></html>");
			nameOfStore.setFont(new Font(nameOfStore.getFont().getFontName(), Font.PLAIN, nameOfStore.getFont().getSize()));
		}
		horizontal.add(nameOfStore);
		horizontal.add(Box.createHorizontalGlue());
		horizontal.add(new JButton(new ChangeNamedComponentAction(resourceStore)));
		return horizontal;
	}
	
	private JScrollPane getList(){
		list = new JList<>();
		list.setModel(model);
		list.addMouseListener(new ResourceDetailAction());
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
		panel.add(getSaveButton());
		panel.add(getSaveAsButton());
		panel.add(Box.createHorizontalGlue());
		panel.add(new JLabel("add or remove: "));
		panel.add(getPlusButton());
		panel.add(getMinusButton());
		return panel;
	}
	
	private JButton getSaveAsButton(){
		JButton save = new JButton(new SaveResourceStoreAction(resourceStore));
		return save;
	}
	
	private JButton getSaveButton(){
		JButton save = new JButton("save");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ResourceStoreContainer container = SwatComponents.getInstance().getResourceStoreContainer();
					if(container.containsComponent(resourceStore.getName()))
						container.storeComponents();
					else
						container.addComponent(resourceStore,true,true);
				} catch (ProjectComponentException | ParameterException e1) {
					Workbench.errorMessage("could not save ResourceStore "+resourceStore.getName(), e1, true);
				}
				
			}
		});
		return save;
	}
	
	private JButton getPlusButton(){
		JButton plus = new JButton("+");
		plus.setText("+");
		plus.setSize(new Dimension(50, 50));
		plus.addActionListener(new addResourceAction(resourceStore));
		return plus;
	}
	
	private JButton getMinusButton(){
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
	
	public ResourceStore getResourceStore(){
		return resourceStore;
	}

	@Override
	public void nameChanged(String newName) {
		nameOfStore.setText("<html>Resource-Store: <b>"+newName+"</b></html>");
	}

}
