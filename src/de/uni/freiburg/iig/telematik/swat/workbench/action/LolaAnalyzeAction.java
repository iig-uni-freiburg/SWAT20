package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.lola.LolaRunner;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;

public class LolaAnalyzeAction extends AbstractAction {

	private static final long serialVersionUID = 9111775745565090191L;
	private File file;
	LolaRunner lola = null;

	public LolaAnalyzeAction(File file) {
		this.file = file;
		//Generate LoLA Runner
		try {
			this.lola = new LolaRunner(file);
			//lola.analyze();
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			SwatState.getInstance().setOperatingMode(this, OperatingMode.ANALYSIS_MODE);
			System.out.println("Analayze " + file.getCanonicalPath());
			try {
				lola.analyze();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO: implement system call to LoLA
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
