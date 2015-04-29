package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.Window;
import java.util.Collection;

import javax.swing.JOptionPane;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.misc.soabase.SOABaseDialog;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.graphic.ACModelChooserDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

@SuppressWarnings("rawtypes")
public class SwatACModelChooserDialog extends ACModelChooserDialog {

	private static final long serialVersionUID = 4764408993583650284L;

	public SwatACModelChooserDialog(Window owner, Collection<AbstractACModel> acModels, Collection<SOABase> contexts) {
		super(owner, acModels, contexts);
	}

	public SwatACModelChooserDialog(Window owner, Collection<AbstractACModel> acModels) {
		super(owner, acModels);
	}
	
	public static AbstractACModel showDialog(Window owner) throws Exception{
		if(!SwatComponents.getInstance().containsContexts()){
			int result = JOptionPane.showInternalConfirmDialog(owner, "Working directory does not contain any contexts so far.\nCreate new context now?", "No process contexts found", JOptionPane.YES_NO_OPTION);
			if(result != JOptionPane.YES_OPTION)
				return null;

			SOABase newContext = SOABaseDialog.showDialog(owner);
			if(newContext == null)
				return null;
			
			SwatComponents.getInstance().addContext(newContext, true);
		}
		return showDialog(owner, SwatComponents.getInstance().getACModels(), SwatComponents.getInstance().getContexts());
	}
	
}
