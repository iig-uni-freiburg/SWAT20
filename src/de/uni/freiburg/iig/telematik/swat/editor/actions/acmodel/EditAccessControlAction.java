package de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.swat.editor.actions.IFNetContextAbstractAction;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.SwatACModelChooserDialog;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class EditAccessControlAction extends IFNetContextAbstractAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public EditAccessControlAction(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "New Organizational Context", IconFactory.getIcon("accesscontrol"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		
		AbstractACModel selectedACModel = SwatACModelChooserDialog.showDialog(SwingUtilities.getWindowAncestor(getEditor()));
		if(selectedACModel != null){
			setSelectedModel(selectedACModel.getName());
		} else {
			setSelectedModel(null);
		}
//		
//		int i = JOptionPane.showConfirmDialog(getEditor().getGraphComponent(), "Is an AC Model selected?");
//
//			if (i == 0)
//				setSelectedModel("Gerd");
//			else if (i == 1)
//				setSelectedModel(null);
//			else if (i == 2) {
//				// do nothing
//			}


		// Get ifNet
		// IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
		// String name = ifNet.getName() + "_Context";
		// Set<String> transitions =
		// PNUtils.getNameSetFromTransitions(ifNet.getTransitions(), true);

		// Create Context
		// SWATContextForAC context = new SWATContextForAC(name, transitions);
		// Set<String> initialSubjects = new HashSet<String>();
		// initialSubjects.add("initialSubject");
		// context.setSubjects(initialSubjects);
		//
		// Window window =
		// SwingUtilities.getWindowAncestor(getEditor().getGraphComponent());
		// ACModel acModel = SWATACModelDialog.showDialog(window, context);
		// if (acModel != null) {
		// context.setACModel(acModel);
		// ((IFNetGraph)getEditor().getGraphComponent().getGraph()).setSelectedACModel(acModel);
		// }

	}

	@Override
	protected String getButtonSpecificToolTipForSelection() {
		return "Selected AC Model: " + "\"" + getModelName() + "\"";

	}

	@Override
	protected String getButtonSpecificToolTipForNonSelection() {
		return "No AC Model selected";

	}

	@Override
	protected void setConsequencesForSelection() {
	
	}

	@Override
	protected void setConsequencesForNonSelection() {
		getFollowingDependency().setSelectedModel(null);
	}

}
