package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;

public class setRecurringAction extends AbstractWorkbenchAction {
	
	private static final long serialVersionUID = 1L;
	
	public setRecurringAction(){
		this("recurring net");
	}

	public setRecurringAction(String name) {
		super(name);
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		Component c = SwatTabView.getInstance().getSelectedComponent();
		if(c instanceof RTPNEditorComponent){
			TimedNet net = ((RTPNEditorComponent)c).getNetContainer().getPetriNet();
			net.setRecurring(!net.isRecurring());
			SwatComponents.getInstance().getContainerPetriNets().storeComponent(net.getName());
		}

	}

}
