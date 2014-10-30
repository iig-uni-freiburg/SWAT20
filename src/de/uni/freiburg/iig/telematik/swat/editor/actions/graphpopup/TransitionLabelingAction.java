package de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.mxgraph.model.mxGraphModel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TransitionLabelingChange;

public class TransitionLabelingAction extends AbstractPNEditorAction {

	private SecurityLevel securityLevel;

	public TransitionLabelingAction(PNEditor pnEditor, SecurityLevel sl) {
		super(pnEditor, sl.toString());
		securityLevel = sl;
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		IFNetGraph graph = (IFNetGraph) getEditor().getGraphComponent().getGraph();
		PNGraphCell cell = (PNGraphCell) graph.getSelectionCell();

		AnalysisContext ac = graph.getCurrentAnalysisContext();
		if(ac != null){
		if (ac.getActivities().contains(cell.getId()))
			((mxGraphModel) graph.getModel()).execute(new TransitionLabelingChange(graph,cell.getId(),securityLevel));
		else {
//			Labeling labeling = new Labeling(graph.getNetContainer().getPetriNet(), ac.getSubjects());
			 Labeling labeling = new Labeling();

			for (String a : ac.getActivities())
				labeling.setActivityClassification(a, ac.getLabeling().getActivityClassification(a));
			ac.setLabeling(labeling);
			((mxGraphModel) graph.getModel()).execute(new TransitionLabelingChange(graph,cell.getId(),securityLevel));
		}
		graph.refresh();

	}
	
	else {
		JOptionPane.showMessageDialog(null,"No Analysis Context defined","Analysis Context Missing", JOptionPane.WARNING_MESSAGE);
	}		
	}
}
