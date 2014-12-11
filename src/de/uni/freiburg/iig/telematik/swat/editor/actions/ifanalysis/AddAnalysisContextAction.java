package de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AnalysisContextChange;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;

public class AddAnalysisContextAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public AddAnalysisContextAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "New Analysis Context", IconFactory.getIcon("plus_analysis_context"));
	}


	private String requestFileName(String message, String title) {
		return new FileNameDialog(SwingUtilities.getWindowAncestor(editor.getGraphComponent()), message, title, false).requestInput();

	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (editor != null) {
			IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
			if (SwatComponents.getInstance().containsACModels()) {
				ACModel acModel = ((IFNetGraph) editor.getGraphComponent().getGraph()).getSelectedACModel();

				if (acModel != null) {

					Set<String> ifSubjects = new HashSet<String>();
					if (acModel instanceof ACLModel) {
						ifSubjects = acModel.getSubjects();
					}
					if (acModel instanceof RBACModel) {
						ifSubjects = ((RBACModel) acModel).getRoles();
					}
					;
					String name = requestFileName("Pleache choose the name for new Analysis Context for " + getEditor().getNetContainer().getPetriNet().getName() + " :", "New Analysis Context");
					if (name != null) {
						AnalysisContext ac = new AnalysisContext(acModel);
						ac.setName(name);
						for (String a : ac.getActivities()) {
							ac.setSubjectDescriptor(a, ifSubjects.iterator().next());
						}

						// Labelings sichtbar machen
						((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new AnalysisContextChange(editor, ac));

						try {
							SwatComponents.getInstance().addAnalysisContext(ac, editor.getNetContainer().getPetriNet().getName(), false);
						} catch (SwatComponentException e1) {
							JOptionPane.showMessageDialog(editor.getGraphComponent(), "Analysis Context could not be Added",
									"Analysis Context not added", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
						((IFNetToolBar)editor.getEditorToolbar()).addAnalysisContextToComboBox(name);

					}
				}
			} else
				JOptionPane.showMessageDialog(editor.getGraphComponent(), "You first need to define your Access Controll Model and its Subjects to build an appropriate Analysis Context",
						"Subjects missing", JOptionPane.ERROR_MESSAGE);

		}		
	}
}
