package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatWorkbenchRedrawListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class setSimulationRunsAction extends AbstractWorkbenchAction {

	public setSimulationRunsAction(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public setSimulationRunsAction(){
		super("set/show number of simulation runs");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		long defValue=SwatProperties.getInstance().getNumberOfSimulationsRuns();
		String result = JOptionPane.showInputDialog("Number of simulation runs?", defValue);
		if(result==null||result.equals("")) 
			return;
		
		try{
			long newValue=Long.parseLong(result);
			SwatProperties.getInstance().setNumberOfSimulationRuns(newValue);
		} catch (Exception e1){
			Workbench.errorMessage("Could not parse value", e1, true);
		}
		
		try{
			SwatProperties.getInstance().store();
		} catch (IOException e2) {
			Workbench.errorMessage("Could not store property file", e2, true);
		}

	}

}
