package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.Window;

import javax.swing.JOptionPane;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.misc.soabase.SOABaseDialog;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.graphic.ACModelChooserDialog;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.graphic.ACModelDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

@SuppressWarnings("rawtypes")
public class SwatACModelChooserDialog extends ACModelChooserDialog {

	private static final long serialVersionUID = 4764408993583650284L;

	public SwatACModelChooserDialog(Window owner) {
		super(owner, SwatComponents.getInstance().getACModels(), SwatComponents.getInstance().getContexts());
	}
	
	@Override
	protected void addNewACModel(AbstractACModel newModel) {
		try {
			SwatComponents.getInstance().addACModel(newModel, true);
		} catch (Exception e1) {
			int result = JOptionPane.showConfirmDialog(SwatACModelChooserDialog.this, "Cannot add new AC model" + e1.getMessage() + "\nEdit AC model?", "Internal Error", JOptionPane.YES_NO_OPTION);
			if(result != JOptionPane.YES_OPTION)
				return;
			try {
				ACModelDialog.showDialog(SwatACModelChooserDialog.this, newModel, contexts);
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(SwatACModelChooserDialog.this, "Cannot launch ACModelDialog: " + e2.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		super.addNewACModel(newModel);
	}

	@Override
	protected void validateNewModel(AbstractACModel newModel) throws Exception {
		if(SwatComponents.getInstance().containsACModel(newModel.getName()))
			throw new Exception("There is already a model with name \"" + newModel.getName() + "\"");
	}

	public static AbstractACModel showDialog(Window owner) throws Exception{
		if(!SwatComponents.getInstance().containsContexts()){
			int result = JOptionPane.showConfirmDialog(owner, "Working directory does not contain any contexts so far.\nCreate new context now?", "No process contexts found", JOptionPane.YES_NO_OPTION);
			if(result != JOptionPane.YES_OPTION)
				return null;

			SOABase newContext = SOABaseDialog.showDialog(owner);
			if(newContext == null)
				return null;
			
			SwatComponents.getInstance().addContext(newContext, true);
		}
		SwatACModelChooserDialog dialog = new SwatACModelChooserDialog(owner);
		dialog.setUpGUI();
		return dialog.getDialogObject();
	}
	
}
