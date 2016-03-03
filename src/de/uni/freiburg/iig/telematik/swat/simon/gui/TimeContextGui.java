package de.uni.freiburg.iig.telematik.swat.simon.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.AddActivityAction;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContextContainer;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.simon.gui.actions.BehaviorDetailListener;
import de.uni.freiburg.iig.telematik.swat.simon.gui.actions.SpecificDistributionAction;
import de.uni.freiburg.iig.telematik.swat.timeSimulation.ContextRepo;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class TimeContextGui extends JFrame implements ListSelectionListener{
	
	private final static int width = 550;
	private final static int height = 450;
	
	private DefaultListModel<String> activities = new DefaultListModel<>();
	private JList<String> activitiesList = new JList<>();
	private DefaultListModel<ITimeBehaviour> behaviors = new DefaultListModel<>();
	private JList<ITimeBehaviour> behaviorList = new JList<>();

	private static final long serialVersionUID = -6663206637927584579L;
	
	private AwesomeTimeContext context;
	
	public static void main(String args[]){
		AwesomeTimeContext context = (AwesomeTimeContext) ContextRepo.getTimeContext();
		TimeContextGui gui = new TimeContextGui(context);
		gui.setVisible(true);
	}
	
	public TimeContextGui (){
		setup();
	}
	
	public TimeContextGui(AwesomeTimeContext context){
		this();
		this.context=context;
		setTitle("Context: "+context.getName());
		for(String s:context.getKnownActivities())
			activities.addElement(s);
	}
	
	public void updateActivities(){
		activities.clear();
		for(String s:context.getKnownActivities())
			activities.addElement(s);
		
	}
	
	public void contextNameChanged(String newName){
		setTitle("Context: "+newName);
	}
	
	private void setup() {
		activitiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		activitiesList.addListSelectionListener(this);
		setLocationByPlatform(true);
		this.setSize(width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//add(getTopPanel(), BorderLayout.PAGE_START);
		add(getCenterPanel(), BorderLayout.CENTER);
		//add(getBottomButtons(), BorderLayout.PAGE_END);
		add(new TimeContextToolbar(this),BorderLayout.PAGE_START);
	}
	
	public void setContext(AwesomeTimeContext context){
		activities.clear();
		for(String s:context.getKnownActivities())
			activities.addElement(s);
		
		contextNameChanged(context.getName());
		
		this.context=context;
		
	}
	
	private JPanel getCenterPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(getActivitiesPanel());
		panel.add(getBehaviorPanel());
		return panel;
	}
	
	private IResourceContext getDefaultContext() throws ProjectComponentException, IOException{
		return SwatComponents.getInstance().getResourceContainer().getComponent(SwatProperties.getInstance().getActiveResourceContext());
	}

	
	private JPanel getActivitiesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		activitiesList.setModel(activities);
		JScrollPane listScroller = new JScrollPane(activitiesList);
		panel.add(listScroller);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(new JButton(new AddActivityAction(activities, null)));
		//buttonPanel.add(new JButton(new AddActivityAction(activities, getActivitieHints())));
		buttonPanel.add(Box.createHorizontalGlue());
		panel.add(buttonPanel);

		return panel;
	}
	
	private JPanel getBehaviorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		behaviorList.setModel(behaviors);
		//behaviorList.addListSelectionListener(new BehaviorDetailListener());
		behaviorList.addMouseListener(new BehaviorDetailListener());
		JScrollPane listScroller = new JScrollPane(behaviorList);
		panel.add(listScroller);
		panel.add(getBehaviourButtons());
		return panel;
		
	}
	
	private JPanel getBehaviourButtons(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(Box.createHorizontalGlue());
		panel.add(new JButton(new CreateTimeBehaviourAction()));
		return panel;
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		String selectedItem = activitiesList.getSelectedValue();
		ITimeBehaviour behaviour = context.getTimeObjectFor(selectedItem);

		behaviors.clear();
		if (behaviour != null) {
			behaviors.addElement(behaviour);
		}

	}
	
	public List<String> getActivitieHints() {
		LinkedList<AbstractTransition> hints = new LinkedList<>();
		LinkedList<String> result = new LinkedList<>();
		try {
			for (int i = 0; i < SwatTabView.getInstance().getComponentCount(); i++) {
				if (SwatTabView.getInstance().getComponent(i) instanceof PNEditorComponent) {
					PNEditorComponent comp = (PNEditorComponent) SwatTabView.getInstance().getComponent(i);
					hints.addAll(comp.getNetContainer().getPetriNet().getTransitions());

					for (AbstractTransition t : hints) {
						result.add(t.getLabel());

					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return result;
	}
	
	public AwesomeTimeContext getContext(){
		return context;
	}
	
	public void save() throws ProjectComponentException {
		if (context == null || context.getName() == null || context.getName().isEmpty())
			return;
		
		de.uni.freiburg.iig.telematik.swat.simon.fileHandling.ITimeContextContainer timeStore = SwatComponents.getInstance().getTimeContextContainer();
		
		if(!timeStore.containsComponent(context.getName())){
			timeStore.addComponent(context);
		}
		
		SwatComponents.getInstance().getTimeContextContainer().storeComponent(context.getName());
	}

	public class CreateTimeBehaviourAction extends AbstractAction {

		private static final long serialVersionUID = -1117202365016832417L;

		public CreateTimeBehaviourAction() {
			super("change");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JPopupMenu menu = new JPopupMenu();
			for (DistributionType type:DistributionType.values()){
				if(!type.equals(DistributionType.UNKNOWN))
					menu.add(new SpecificDistributionAction(type, context, activitiesList.getSelectedValue()));
			}
			
			JButton source = (JButton) e.getSource();
			menu.setInvoker((Component) e.getSource());
			menu.setLocation((int) source.getLocationOnScreen().getX(), (int) source.getLocationOnScreen().getY());
			menu.setVisible(true);
		}

	}

}