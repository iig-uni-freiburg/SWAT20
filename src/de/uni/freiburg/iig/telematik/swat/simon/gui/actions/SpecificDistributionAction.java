package de.uni.freiburg.iig.telematik.swat.simon.gui.actions;

import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.AbstractAction;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import de.invation.code.toval.misc.CollectionUtils;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.JOptionPaneMultiInput;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.simon.AbstractTimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.simon.ITimeBehaviourFactory;

public class SpecificDistributionAction extends AbstractAction {

	private static final long serialVersionUID = 2692756060636577888L;
	
	private DistributionType type;

	public SpecificDistributionAction(DistributionType type) {
		super(type.toString());
		this.type=type;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LinkedList<Double> list = new LinkedList<>();
		list.add(1d); //null parameters. Just to instantiate object
		list.add(1d);
		AbstractTimeBehaviour template = (AbstractTimeBehaviour) ITimeBehaviourFactory.getBahaviour(type, list);
		JOptionPaneMultiInput input = new JOptionPaneMultiInput(template.getParameters());
		ITimeBehaviour behaviour = ITimeBehaviourFactory.getBahaviour(type, input.getResultAsDouble());
		
		switch (type) {
		case LOG_NORMAL:
		case NORMAL:
			//2-Parameters
			break;

		default:
			break;
		}
		
	}
	

}
