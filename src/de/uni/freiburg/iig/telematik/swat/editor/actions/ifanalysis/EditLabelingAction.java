package de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.actions.IFNetContextAbstractAction;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class EditLabelingAction extends IFNetContextAbstractAction {

	private static final long serialVersionUID = 4315293729223367039L;
	private JToggleButton followingButton;
	private JToggleButton subjectClearanceButton;


	public EditLabelingAction(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "Edit Labeling", IconFactory.getIcon("tokenlabel"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		
		if (getPreceedingDependency() != null && getPreceedingDependency().getModelName() != null) {
			int i = JOptionPane.showConfirmDialog(getEditor().getGraphComponent(), "Is a Labeling selected?");

			if (i == 0)
				setSelectedModel("Gerd");
			else if (i == 1)
				setSelectedModel(null);
			else if (i == 2) {
				// do nothing
			}

		} else
			JOptionPane.showMessageDialog(getEditor().getGraphComponent(), "Please first select an Anaysis Context");

		//TODO: Adapt to new ACStructure 
//		AbstractACModel acModel = ((IFNetGraph) editor.getGraphComponent().getGraph()).getSelectedACModel();
//		
//		Set<String>  ifSubjects = new HashSet<String>();
//		if(acModel instanceof ACLModel){
//			ifSubjects = acModel.getContext().getSubjects();
//		}
//		if(acModel instanceof RBACModel){
//			ifSubjects = ((RBACModel) acModel).getRoles();
//		};
//		AnalysisContext ac = new AnalysisContext(acModel.getContext());

	}


	@Override
	protected String getButtonSpecificToolTipForSelection() {
		return "Selected Labeling: " +"\""+ getModelName() +"\"";
		
	}

	@Override
	protected String getButtonSpecificToolTipForNonSelection() {
		return "No Labeling selected";
		
	}

	@Override
	protected void setConsequencesForSelection() {
		subjectClearanceButton.setEnabled(true);		
	}

	@Override
	protected void setConsequencesForNonSelection() {
		subjectClearanceButton.setEnabled(false);
		
	}

	public void setSubjectClearanceButton(JToggleButton editSubjectClearanceButton) {
		this.subjectClearanceButton = editSubjectClearanceButton;
		
	}


}
