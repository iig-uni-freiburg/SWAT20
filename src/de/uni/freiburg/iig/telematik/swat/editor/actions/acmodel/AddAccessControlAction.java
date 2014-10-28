package de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.menu.acmodel.SWATACModelDialog;
import de.uni.freiburg.iig.telematik.swat.editor.menu.acmodel.SWATContextForAC;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class AddAccessControlAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public AddAccessControlAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "New Organizational Context", IconFactory.getIcon("accesscontrol"));
	}

	public void actionPerformed(ActionEvent e) {
		if (editor != null) {
			IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
			String name = ifNet.getName() + "_Context";
			Set<String> transitions = PNUtils.getNameSetFromTransitions(ifNet.getTransitions(), true);
			SWATContextForAC context = new SWATContextForAC(name, transitions);
			Set<String> initialSubjects = new HashSet<String>();
			initialSubjects.add("initialSubject");
			context.setSubjects(initialSubjects);

			Window window = SwingUtilities.getWindowAncestor(getEditor().getGraphComponent());
			ACModel acModel = SWATACModelDialog.showDialog(window, context);
			if (acModel != null) {
				context.setACModel(acModel);
				((IFNetGraph)getEditor().getGraphComponent().getGraph()).setSelectedACModel(acModel);
			}

		}
	}
}
