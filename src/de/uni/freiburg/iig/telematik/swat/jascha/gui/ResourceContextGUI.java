package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.AddActivityAction;
import de.uni.freiburg.iig.telematik.swat.timeSimulation.ContextRepo;

public class ResourceContextGUI extends JFrame implements ResourceStoreListener, ListSelectionListener {

	private static final long serialVersionUID = -5550468723771850810L;
	AwesomeResourceContext context;
	
	private final static String title = "Resource Context Editor";
	private final static int width = 550;
	private final static int height = 450;
	private DefaultListModel<String> activities = new DefaultListModel<>();
	private DefaultListModel<IResource> resources = new DefaultListModel<>();
	List<String> activitesHints;
	private JList<String> activitiesList = new JList<>();
	private JList<IResource> resourceList = new JList<>();
	private JLabel resourceStoreName = new JLabel();
	private JButton addActivity;

	public static void main(String args[]) {
		ResourceContextGUI gui = new ResourceContextGUI(ContextRepo.getResourceContext());
		gui.setVisible(true);
		LinkedList<String> hints = new LinkedList<>();
		hints.add("test");
		hints.add("test2");
		hints.add("test3");
		hints.add("test4");
		hints.add("neu");
		gui.setActivitiesHints(hints);
	}

	public ResourceContextGUI() {
		super(title);
		this.context = new AwesomeResourceContext();
		setup();
	}

	public ResourceContextGUI(IResourceContext context) {
		super(title+": "+context.getName());
		this.context = (AwesomeResourceContext) context;
		setup();
		for(String s:this.context.getContainingActivities()){
			activities.addElement(s);
		}
		
	}

	private void setup() {
		setupResourceList();
		setupActivitiesList();
		context.getResourceStore().addResourceStoreListener(this);
		setLocationByPlatform(true);
		this.setSize(width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(getTopPanel(), BorderLayout.PAGE_START);
		add(getCenterPanel(), BorderLayout.CENTER);
		add(getBottomButtons(), BorderLayout.PAGE_END);
	}

	private void setupActivitiesList() {
		activitiesList.addListSelectionListener(this);
	}
	
	public AwesomeResourceContext getContext() {
		return context;
	}
	
	public void setContext(AwesomeResourceContext context){
		this.context.getResourceStore().removeResourceStoreListener(this);
		
		this.context=context;
		updateLists();
		
		this.context.getResourceStore().addResourceStoreListener(this);
	}
	
	private void updateLists(){
		activities.clear();
		for(String activites: context.getContainingActivities())
			activities.addElement(activites);
		
		resources.clear();
		for(IResource res:context.getResourceStore().getAllResources())
			resources.addElement(res);
		
		activitiesList.repaint();
		resourceList.repaint();
	}

	private void setupResourceList() {
		resourceList.setSelectionModel(new DefaultListSelectionModel() {
			private int i0 = -1;
			private int i1 = -1;

			public void setSelectionInterval(int index0, int index1) {
				if (i0 == index0 && i1 == index1) {
					if (getValueIsAdjusting()) {
						setValueIsAdjusting(false);
						setSelection(index0, index1);
					}
				} else {
					i0 = index0;
					i1 = index1;
					setValueIsAdjusting(false);
					setSelection(index0, index1);
				}
				fireValueChanged(index0, index1);
			}

			private void setSelection(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					super.removeSelectionInterval(index0, index1);
				} else {
					super.addSelectionInterval(index0, index1);
				}
				fireValueChanged(index0, index1);
			}
		});
		resourceList.addListSelectionListener(this);

	}

	public void setActivitiesHints(List<String> activitesHints) {
		this.activitesHints=activitesHints;
		addActivity.setAction(new AddActivityAction(activities, activitesHints) );
	}

	private JPanel getCenterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(getActivitiesPanel());
		panel.add(getResourcePanel());
		return panel;
	}

	private JPanel getActivitiesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		activitiesList.setModel(activities);
		JScrollPane listScroller = new JScrollPane(activitiesList);
		panel.add(listScroller);

		return panel;
	}

	private JPanel getResourcePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		resourceList.setModel(resources);

		for (IResource res : context.getResourceStore().getAllResources())
			resources.addElement(res);

		JScrollPane listScroller = new JScrollPane(resourceList);
		panel.add(listScroller);

		return panel;
	}

	private JPanel getBottomButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		addActivity = new JButton(new AddActivityAction(activities, activitesHints));

		
		panel.add(addActivity);
		panel.add(getRemoveButton());
		panel.add(Box.createHorizontalGlue());

		JButton resources = new JButton("Edit resources");
		resources.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ResourceStoreGUI(context.getResourceStore()).setVisible(true);
			}
		});
		panel.add(resources);

		return panel;
	}
	
	private JButton getRemoveButton(){
		JButton remove = new JButton("remove activity");
		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String activity=activitiesList.getSelectedValue();
				activities.removeElement(activity);
				context.clearUsageFor(activity);
				
			}
		});
		return remove;
	}

	private JPanel getTopPanel() {
		JPanel panel = new JPanel();
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		panel.setLayout(new BorderLayout());
		panel.add(new ResourceContextToolbar(this),BorderLayout.PAGE_START);
		bottom.add(new JLabel("Activities"));
		bottom.add(Box.createHorizontalGlue());
		resourceStoreName.setText("Resources (using Resource Store "+context.getResourceStore().getName()+")");
		bottom.add(resourceStoreName);
		panel.add(bottom, BorderLayout.PAGE_END);

		return panel;
	}

	@Override
	public void resourceStoreElementAdded(IResource resource) {
		resources.addElement(resource);

	}

	@Override
	public void informStoreElementRemoved(IResource resource) {
		resources.removeElement(resource);

	}

	@Override
	public void nameChanged(String newName) {
		resourceStoreName.setText("Resources (using Resource Store "+context.getResourceStore().getName()+")");
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = ((JList) e.getSource()).getSelectionModel();
		
		if (e.getSource().equals(resourceList) && !e.getValueIsAdjusting()) {
			//a resource was selected clear usage, build new
			String activity = activitiesList.getSelectedValue();
			context.clearUsageFor(activity);
			for (int i = lsm.getMinSelectionIndex(); i <= lsm.getMaxSelectionIndex(); i++) {
				if (lsm.isSelectedIndex(i)) {
					context.addResourceUsage(activity, resources.get(i));
					System.out.println("Adding "+resources.get(i)+" to "+ activitiesList.getSelectedValue());
				} 
			}
		} else if(e.getSource().equals(activitiesList)&& !e.getValueIsAdjusting()){
			//new activity got selected.
			String activity = activitiesList.getSelectedValue();
			resourceList.removeListSelectionListener(this); //do not inform of list change
			resourceList.clearSelection();
			for(IResource res:context.getKnownResourcesFor(activity)){
				resourceList.setSelectedValue(res, false);
			}
			resourceList.addListSelectionListener(this);
		}

	}
	
	public void setResourceStoreForResourceContext(ResourceStore store){
		context.getResourceStore().removeResourceStoreListener(this);
		context.setResourceStore(store);
		store.addResourceStoreListener(this);
		nameChanged(store.getName());
		updateLists();
		
	}

}
