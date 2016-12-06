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

public class SetNetWeightAction extends AbstractWorkbenchAction {

	public SetNetWeightAction(String name) {
		super(name);
	}
	
	public SetNetWeightAction() {
		this("Set/Show net weight");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (isRTPNet()) {
			TimedNet net = getRTPNNet();
			double netWeight = net.getNetWeight();
			try {
			String result = JOptionPane.showInputDialog((Component) e.getSource(), "Weight for net " + net.getName(), netWeight);
			if(result != null){
				double newNetWeight=Double.parseDouble(result);
				net.setNetWeight(newNetWeight);
				SwatComponents.getInstance().getContainerPetriNets().storeComponent(net.getName());
			}
			} catch (Exception e1) {
				Workbench.errorMessage("Could not set NetWeight", e1, true);
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
