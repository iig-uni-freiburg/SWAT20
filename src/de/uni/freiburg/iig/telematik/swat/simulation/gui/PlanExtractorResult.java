package de.uni.freiburg.iig.telematik.swat.simulation.gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.simulation.PlanExtractor;
import de.uni.freiburg.iig.telematik.swat.simulation.WorkflowExecutionPlan;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PlanExtractorResult extends JFrame   {
	
	private PlanExtractor ex;
	JList<WorkflowExecutionPlan> jlist;

	public PlanExtractorResult(PlanExtractor ex) {
		this.ex = ex;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 480));
		add(content());
		setVisible(true);
	}
	
	
	private JPanel content(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new PlanExtractorGuiToolbar(this));
		panel.add(new JLabel("possible execution plans: "));
		panel.add(getList());
		return panel;
	}
	
	private JScrollPane getList(){
		WorkflowExecutionPlan[] plans = new WorkflowExecutionPlan[1];
		plans = ex.getExecutionPlan().toArray(plans);
		jlist = new JList<>(plans);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
		        JList<WorkflowExecutionPlan> list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {

		            // Double-click detected
		            //int index = list.locationToIndex(evt.getPoint());
		            startOptimization(list.getSelectedValue());
		        } 
		    }
		});
		JScrollPane pane = new JScrollPane(jlist);
		return pane;
		
	}
	
	private void startOptimization(WorkflowExecutionPlan workflowExecutionPlan){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				WorkflowTimeMachine wtm = WorkflowTimeMachine.getInstance();
				try {
					wtm.simulateExecutionPlan(2000, workflowExecutionPlan.getSeq());
					new SimulationResult(wtm, getTimeContext(), false).setVisible(true);
				} catch (PNException e) {
					Workbench.errorMessage("Could not simulate execution plan", e, true);
					
				} catch (ProjectComponentException|IOException e) {
					Workbench.errorMessage("Could not load time context", e, true);
				} 
				
			}
		});
		t.run();//change to start() later
		System.out.println("running");
	}


	protected AwesomeTimeContext getTimeContext() throws ProjectComponentException, IOException {
		return (AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(SwatProperties.getInstance().getActiveTimeContext());
	}


	public WorkflowExecutionPlan getSelectedValue() {
		return jlist.getSelectedValue();
	}




}
