package de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

public class EditSubjectClearanceAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;
	
	public EditSubjectClearanceAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "Edit Clearance", IconFactory.getIcon("user_shield"));
	}

	public void actionPerformed(ActionEvent e) {
		if (editor != null) {
			IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
			ACModel acModel = ((IFNetGraph) editor.getGraphComponent().getGraph()).getSelectedACModel();
			
			Set<String>  ifSubjects = new HashSet<String>();
			if(acModel instanceof ACLModel){
				ifSubjects = acModel.getSubjects();
			}
			if(acModel instanceof RBACModel){
				ifSubjects = ((RBACModel) acModel).getRoles();
			};
			AnalysisContext ac = new AnalysisContext(ifNet, ifSubjects, SecurityLevel.LOW);
		}
	}
}
