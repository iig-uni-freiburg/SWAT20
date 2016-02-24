package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;

public class SetDeadlineAction extends AbstractWorkbenchAction {
	
	public SetDeadlineAction() {
		super("set deadline...");
	}

	public SetDeadlineAction(String name) {
		super(name);
	}

	private static final long serialVersionUID = 7045605296251955925L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if(isRTPNet()){
			TimedNet net = getRTPNNet();
			
			String result = JOptionPane.showInputDialog((Component)e.getSource(), "Deadline for net "+net.getName());
			Double deadline = Double.parseDouble(result);
			AwesomeTimeContext context = (AwesomeTimeContext) SwatComponents.getInstance().getTimeContextContainer().getComponent(SwatProperties.getInstance().getActiveTimeContext());
			context.setDeadline(net.getName(), deadline);
			SwatComponents.getInstance().getTimeContextContainer().storeComponent(SwatProperties.getInstance().getActiveTimeContext());
		}

	}
	
	private boolean isRTPNet() throws Exception{
		Component comp = SwatTabView.getInstance().getSelectedComponent();
		if (comp instanceof RTPNEditorComponent){
			return true;
		}
		return false;
	}
	
	private TimedNet getRTPNNet() throws Exception{
		Component comp = SwatTabView.getInstance().getSelectedComponent();
		if (comp instanceof RTPNEditorComponent){
			RTPNEditorComponent editor = (RTPNEditorComponent) comp;
			return editor.getNetContainer().getPetriNet();
		}
		return null;
	}

}
