package de.uni.freiburg.iig.telematik.swat.simon.gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.timeSimulation.ContextRepo;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import javafx.scene.control.SelectionMode;

public class TimeContextGui extends JFrame{
	
	private final static int width = 550;
	private final static int height = 450;
	
	private DefaultListModel<String> activities = new DefaultListModel<>();
	private JList<String> activitiesList = new JList<>();

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
		for(String s:context.getKnownActivities())
			activities.addElement(s);
	}
	
	private void setup() {
		activitiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLocationByPlatform(true);
		this.setSize(width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//add(getTopPanel(), BorderLayout.PAGE_START);
		add(getCenterPanel(), BorderLayout.CENTER);
		//add(getBottomButtons(), BorderLayout.PAGE_END);
	}
	
	private JPanel getCenterPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(getActivitiesPanel());
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

		return panel;
	}

}
