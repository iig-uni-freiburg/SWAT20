package de.uni.freiburg.iig.telematik.swat.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.mxgraph.model.mxGraphModel;
import de.invation.code.toval.misc.wd.ProjectComponentException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup.TransitionLabelingAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.SwatIFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.SubjectDescriptorChange;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphpopup.TransitionSilentAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public class SwatTransitionPopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2032963439895247488L;
	private boolean hascontext = true;
	private JMenu submenuSubjectDescriptor;
	private SwatIFNetGraph graph;

	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
	}

	public SwatTransitionPopupMenu(PNEditorComponent pnEditor) throws Exception {
		Validate.notNull(pnEditor);
		JMenu submenu = (JMenu) add(new JMenu("Transition"));
		submenu.add(new TransitionSilentAction(pnEditor, "silent", true));
		submenu.add(new TransitionSilentAction(pnEditor, "not silent", false));

		if (pnEditor.getGraphComponent().getGraph() instanceof IFNetGraph) {
			graph = (SwatIFNetGraph) pnEditor.getGraphComponent().getGraph();
			addSeparator();
			JMenu submenuClassification = (JMenu) add(new JMenu("Classification"));
			TransitionLabelingAction high = new TransitionLabelingAction(pnEditor, SecurityLevel.HIGH);
			TransitionLabelingAction low = new TransitionLabelingAction(pnEditor, SecurityLevel.LOW);
			submenuClassification.add(high);
			submenuClassification.add(low);
			if (graph.getCurrentAnalysisContext() != null)
				hascontext = true;

			high.setEnabled(hascontext);
			low.setEnabled(hascontext);
			submenuSubjectDescriptor = (JMenu) add(new JMenu("Subject Descriptor"));
			updateSubjectDescriptorMenu();
			if (graph.getCurrentAnalysisContext() != null)
				hascontext = true;
			high.setEnabled(hascontext);
			low.setEnabled(hascontext);

			// add(getTimingMenu(pnEditor));

		}

	}

	private void updateSubjectDescriptorMenu() throws Exception {
		// submenu3.removeAll();
		if (SwatComponents.getInstance().containsACModels()) {
			if (graph instanceof SwatIFNetGraph) {
				if(graph.getCurrentAnalysisContext() != null){
				AbstractACModel acModel = graph.getCurrentAnalysisContext().getACModel();
				PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
				AbstractIFNetTransition<IFNetFlowRelation> t = graph.getNetContainer().getPetriNet().getTransition(selectedCell.getId());
				List<String> authorizedSubjects = null;
				if (acModel instanceof ACLModel)
					authorizedSubjects = acModel.getAuthorizedSubjectsForTransaction(selectedCell.getId());
				if (acModel instanceof RBACModel)
					authorizedSubjects = ((RBACModel) acModel).getRolePermissions().getAuthorizedSubjectsForTransaction(selectedCell.getId());
				for (final String s : authorizedSubjects) {
					JMenuItem item = new JMenuItem(s);
					item.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							((mxGraphModel) graph.getModel()).execute(new SubjectDescriptorChange(graph, ((PNGraphCell) graph.getSelectionCell()).getId(), s));

						}
					});
					submenuSubjectDescriptor.add(item);
				}

			} else {
				JMenuItem noSubjects = new JMenuItem("No Subjects Defined");
				submenuSubjectDescriptor.add(noSubjects);
				noSubjects.setEnabled(false);
			}
			}
		}
	}

	// private JMenu getTimingMenu(PNEditorComponent pnEditor) {
	// JMenu submenu4 = new JMenu("Timing");
	// JMenuItem item = new JMenuItem(new
	// SetTransititionTimingAction(pnEditor));
	// JMenuItem item1 = new JMenuItem(new
	// TransitionTimingInfoAction(pnEditor));
	// JMenuItem item2 = new JMenuItem(new ClearTimeAction(pnEditor));
	// //item1.addActionListener(new TransitionTimeAction(pnEditor));
	// submenu4.add(item);
	// submenu4.add(item1);
	// submenu4.add(item2);
	// return submenu4;
	// }
}
