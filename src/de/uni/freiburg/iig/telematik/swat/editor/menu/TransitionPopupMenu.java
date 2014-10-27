package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup.TransitionLabelingAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup.TransitionSilentAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.SubjectDescriptorChange;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.TransitionView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

public class TransitionPopupMenu extends JPopupMenu {

	@Override
	public void show(Component invoker, int x, int y) {
		updateSubjectDescriptorMenu();
		super.show(invoker, x, y);
	}

	private static final long serialVersionUID = -2983257974918330746L;

	private boolean hascontext = true;
	private JMenu submenu3;
	private IFNetGraph graph;

	public TransitionPopupMenu(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		Validate.notNull(pnEditor);
		boolean selected = !pnEditor.getGraphComponent().getGraph().isSelectionEmpty();

		JMenu submenu = (JMenu) add(new JMenu("Transition"));

		submenu.add(new TransitionSilentAction(pnEditor, "silent", true));
		submenu.add(new TransitionSilentAction(pnEditor, "not silent", false));
		if (pnEditor.getGraphComponent().getGraph() instanceof IFNetGraph) {
			graph = (IFNetGraph) pnEditor.getGraphComponent().getGraph();
			addSeparator();
			JMenu submenu2 = (JMenu) add(new JMenu("Classification"));
			TransitionLabelingAction high = new TransitionLabelingAction(pnEditor, SecurityLevel.HIGH);
			TransitionLabelingAction low = new TransitionLabelingAction(pnEditor, SecurityLevel.LOW);
			submenu2.add(high);
			submenu2.add(low);
			if (graph.getCurrentAnalysisContext() != null)
				hascontext = true;
			high.setEnabled(hascontext);
			low.setEnabled(hascontext);
			submenu3 = (JMenu) add(new JMenu("Subject Descriptor"));
			updateSubjectDescriptorMenu();
			if (graph.getCurrentAnalysisContext() != null)
				hascontext = true;
			high.setEnabled(hascontext);
			low.setEnabled(hascontext);

			add(getTimingMenu(pnEditor));

		}

	}

	private void updateSubjectDescriptorMenu() {
		submenu3.removeAll();
		if(SwatComponents.getInstance().containsACModels()){
			if(graph instanceof IFNetGraph){
			if(((IFNetGraph)graph).getSelectedACModel() != null){
		ACModel acModel = ((IFNetGraph)graph).getSelectedACModel();
		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
		AbstractIFNetTransition<IFNetFlowRelation> t = graph.getNetContainer().getPetriNet().getTransition(selectedCell.getId());
//		graph.getNetContainer().getPetriNet().addDeclassificationTransition(transitionName)
		List<String> authorizedSubjects = null;
		//		graph.getNetContainer().getPetriNet().addDeclassificationTransition(transitionName)
		if (acModel instanceof ACLModel)
		authorizedSubjects = acModel.getAuthorizedSubjectsForTransaction(selectedCell.getId());
		if(acModel instanceof RBACModel)
		authorizedSubjects = ((RBACModel)acModel).getRolePermissions().getAuthorizedSubjectsForTransaction(selectedCell.getId());
		for(final String s:authorizedSubjects){
			JMenuItem item = new JMenuItem(s);
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
//					((mxGraphModel)graph.getModel()).execute(new SubjectDescriptorChange);
					((mxGraphModel) graph.getModel()).execute(new SubjectDescriptorChange(graph,((PNGraphCell) graph.getSelectionCell()).getId(),s));
					
				}
			});
			submenu3.add(item);
		}
		}}
		else {
			JMenuItem noSubjects = new JMenuItem("No Subjects Defined");
			submenu3.add(noSubjects );
			noSubjects.setEnabled(false);
		}
		}
	}

	private JMenu getTimingMenu(PNEditor pnEditor) {
		JMenu submenu4 = new JMenu("Timing");
		JMenuItem item = new JMenuItem("set timing...");
		item.addActionListener(new TransitionTimeAction(pnEditor));
		submenu4.add(item);
		return submenu4;
	}
}

class TransitionTimeAction implements ActionListener {

	private PNEditor editor;

	public TransitionTimeAction(PNEditor editor) {
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IFNetGraph graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		PNGraphCell cell = (PNGraphCell) graph.getSelectionCell();
		//TransitionView view = new TransitionView(cell.getId(), SwatComponents.getInstance().getTimeAnalysisForNet(editor.getNetContainer()));
		TransitionView view = new TransitionView(cell.getId(), SwatComponents.getInstance().getTimeContext(
				graph.getNetContainer().getPetriNet().getName(),
				"hardcodedTimeContext"));
		view.setVisible(true);
	}

}
