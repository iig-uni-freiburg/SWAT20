package de.uni.freiburg.iig.telematik.swat.simon.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class SpecificDistributionAction extends AbstractAction {

	private static final long serialVersionUID = 2692756060636577888L;
	
	private DistributionType type;

	public SpecificDistributionAction(DistributionType type) {
		super(type.toString());
		this.type=type;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
