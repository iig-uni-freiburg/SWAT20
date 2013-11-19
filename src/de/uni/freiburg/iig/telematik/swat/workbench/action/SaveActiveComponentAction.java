package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;

public class SaveActiveComponentAction extends AbstractAction {

	private static final long serialVersionUID = 7231652730616663333L;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			SwatComponents.getInstance().storePetriNet(
					SwatComponents.getInstance().getNetWithFileName(SwatState.getInstance().getActiveFile()));
		} catch (ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
