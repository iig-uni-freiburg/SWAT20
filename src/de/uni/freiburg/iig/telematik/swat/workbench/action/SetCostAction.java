package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.management.remote.JMXServiceURL;
import javax.swing.JOptionPane;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.JOptionPaneMultiInput;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;

public class SetCostAction extends AbstractWorkbenchAction {

	public SetCostAction(String name) {
		super(name);
	}
	
	public SetCostAction(){
		this("set/show cost");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if(!isRTPNet())
			return;
		
		
		TimedNet net = getRTPNNet();
		Double oldCost = net.getCostPerTimeUnit();
		Double oldCostAfterDeadline=net.getCostPerTimeUnitAfterDeadline();
		
		//String result = JOptionPane.showInputDialog((Component) e.getSource(), "Deadline for net " + net.getName(), oldCost);
		String[] fieldNames={"Cost per time unit","cost per time unit after deadline"};
		String[] fieldValues={Double.toString(oldCost),Double.toString(oldCostAfterDeadline)};
		JOptionPaneMultiInput input = new JOptionPaneMultiInput(fieldValues,fieldNames);
		
		if(input.hasUserInput() && input.getResult(0)!=null&&!input.getResult(0).equals("")){
			try {
				net.setCostPerTimeUnit(Double.parseDouble(input.getResult(0)));
				net.setCostPerTimeUnitAfterDeadline(Double.parseDouble(input.getResult(1)));
				SwatComponents.getInstance().getContainerPetriNets().storeComponent(net.getName());
			} 
			catch (ProjectComponentException pe) {
				Workbench.errorMessage("Could not store new costs", pe, true);
			}
			catch(Exception e1){
				Workbench.errorMessage("Could not parse cost value", e1, true);
			}
		}

	}
	
	private boolean isRTPNet() throws Exception {
		Component comp = SwatTabView.getInstance().getSelectedComponent();
		if (comp instanceof RTPNEditorComponent) {
			return true;
		}
		return false;
	}
	
	private TimedNet getRTPNNet() throws Exception {
		Component comp = SwatTabView.getInstance().getSelectedComponent();
		if (comp instanceof RTPNEditorComponent) {
			RTPNEditorComponent editor = (RTPNEditorComponent) comp;
			return editor.getNetContainer().getPetriNet();
		}
		return null;
	}

}
