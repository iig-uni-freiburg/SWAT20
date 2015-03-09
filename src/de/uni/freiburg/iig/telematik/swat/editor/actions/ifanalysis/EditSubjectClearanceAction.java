package de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class EditSubjectClearanceAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;
	
	public EditSubjectClearanceAction(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "Edit Clearance", IconFactory.getIcon("user_shield"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (editor != null) {
			IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
			//TODO: Adapt to new ACStructure 
//			AbstractACModel acModel = ((IFNetGraph) editor.getGraphComponent().getGraph()).getSelectedACModel();
//			
//			Set<String>  ifSubjects = new HashSet<String>();
//			if(acModel instanceof ACLModel){
//				ifSubjects = acModel.getContext().getSubjects();
//			}
//			if(acModel instanceof RBACModel){
//				ifSubjects = ((RBACModel) acModel).getRoles();
//			};
//			AnalysisContext ac = new AnalysisContext(acModel.getContext());
		}
	}
}
