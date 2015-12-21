package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
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

import com.itextpdf.text.Jpeg;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.timeSimulation.ContextRepo;

public class ResourceContextGUI extends JFrame implements ResourceStoreListener, ListSelectionListener {

	private static final long serialVersionUID = -5550468723771850810L;
	AwesomeResourceContext context;
	private final static String title = "Resource Context Editor";
	private final static int width = 500;
	private final static int height = 400;
	private DefaultListModel<String> activities = new DefaultListModel<>();
	private DefaultListModel<IResource> resources = new DefaultListModel<>();
	List<String> activitesHints;
	private JList<String> activitiesList = new JList<>();
	private JList<IResource> resourceList = new JList<>();

	public static void main(String args[]) {
		ResourceContextGUI gui = new ResourceContextGUI(ContextRepo.getResourceContext());
		gui.setVisible(true);
		LinkedList<String> hints = new LinkedList<>();
		hints.add("test");
		hints.add("test2");
		hints.add("test3");
		hints.add("test4");
		gui.setActivitiesHints(hints);
	}

	public ResourceContextGUI() {
		super(title);
		this.context = new AwesomeResourceContext();
		setup();
	}

	public ResourceContextGUI(IResourceContext context) {
		super(title);
		this.context = (AwesomeResourceContext) context;
		setup();
	}

	private void setup() {
		setupResourceList();
		activitiesList.addListSelectionListener(this);
		context.getResourceStore().addResourceStoreListener(this);
		this.setSize(width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(getTopPanel(), BorderLayout.PAGE_START);
		add(getCenterPanel(), BorderLayout.CENTER);
		add(getBottomButtons(), BorderLayout.PAGE_END);
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
		for (String s:activitesHints){
			activities.addElement(s);
		}
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

		// JPanel buttons = new JPanel(new FlowLayout());
		// JButton addActivity=new JButton("add");
		// JButton removeActivity=new JButton("remove");
		// buttons.add(addActivity);
		// buttons.add(removeActivity);

		// panel.add(buttons);

		return panel;
	}

	private JPanel getResourcePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		// resourceList=new JList<>();
		resourceList.setModel(resources);

		for (IResource res : context.getResourceStore().getAllResources())
			resources.addElement(res);

		JScrollPane listScroller = new JScrollPane(resourceList);
		panel.add(listScroller);

		// JPanel buttons = new JPanel(new FlowLayout());
		// JButton edit=new JButton("Edit resources");
		// buttons.add(edit);

		// panel.add(buttons);

		return panel;
	}

	private JPanel getBottomButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(new JButton("add Activity"));
		panel.add(new JButton("remove Activity"));
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

	private JPanel getTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(new JLabel("Activities"));
		panel.add(Box.createHorizontalGlue());
		panel.add(new JLabel("Allowed Resources"));

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
		// TODO Auto-generated method stub

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = ((JList) e.getSource()).getSelectionModel();
		
		if (e.getSource().equals(resourceList) && !e.getValueIsAdjusting()) {
			//clear usage, build new
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
			System.out.println("loading");
			String activity = activitiesList.getSelectedValue();
			resourceList.removeListSelectionListener(this); //do not inform of list change
			resourceList.clearSelection();
			for(IResource res:context.getManagedResourcesFor(activity)){
				resourceList.setSelectedValue(res, false);
			}
			resourceList.addListSelectionListener(this);
		}

	}

}
