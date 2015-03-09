package de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Set;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class AddAccessControlAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public AddAccessControlAction(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "New Organizational Context", IconFactory.getIcon("accesscontrol"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (editor != null) {
			
			//Get ifNet
			IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
			String name = ifNet.getName() + "_Context";
			Set<String> transitions = PNUtils.getNameSetFromTransitions(ifNet.getTransitions(), true);
			
			//Create Context
//			SWATContextForAC context = new SWATContextForAC(name, transitions);
//			Set<String> initialSubjects = new HashSet<String>();
//			initialSubjects.add("initialSubject");
//			context.setSubjects(initialSubjects);
//
//			Window window = SwingUtilities.getWindowAncestor(getEditor().getGraphComponent());
//			ACModel acModel = SWATACModelDialog.showDialog(window, context);
//			if (acModel != null) {
//				context.setACModel(acModel);
//				((IFNetGraph)getEditor().getGraphComponent().getGraph()).setSelectedACModel(acModel);
//			}

		}
		
	}
}
