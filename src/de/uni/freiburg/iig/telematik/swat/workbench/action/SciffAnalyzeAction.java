package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;

public class SciffAnalyzeAction extends AbstractAction {

	private static final long serialVersionUID = 9111775745565090191L;
	private File file;

	public SciffAnalyzeAction(File file) {
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			SwatState.getInstance().setOperatingMode(this, OperatingMode.ANALYSIS_MODE);
			System.out.println("Analayze " + file.getCanonicalPath());
			// TODO: implement system call to sciff
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
