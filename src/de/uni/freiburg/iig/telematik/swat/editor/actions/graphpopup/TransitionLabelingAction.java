package de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup;

import java.awt.event.ActionEvent;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public class TransitionLabelingAction extends AbstractPNEditorAction {

	private SecurityLevel securityLevel;

	public TransitionLabelingAction(PNEditorComponent pnEditor, SecurityLevel sl) {
		super(pnEditor, sl.toString());
		securityLevel = sl;
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		IFNetGraph graph = (IFNetGraph) getEditor().getGraphComponent().getGraph();
		PNGraphCell cell = (PNGraphCell) graph.getSelectionCell();
//TODO: Adapt to new ACStructure 
//		AnalysisContext ac = graph.getCurrentAnalysisContext();
//		if(ac != null){
//		if (ac.getActivities().contains(cell.getId()))
//			((mxGraphModel) graph.getModel()).execute(new TransitionLabelingChange(graph,cell.getId(),securityLevel));
//		else {
////			Labeling labeling = new Labeling(graph.getNetContainer().getPetriNet(), ac.getSubjects());
//			 Labeling labeling = new Labeling();
//
//			for (String a : ac.getActivities())
//				labeling.setActivityClassification(a, ac.getLabeling().getActivityClassification(a));
//			ac.setLabeling(labeling);
//			((mxGraphModel) graph.getModel()).execute(new TransitionLabelingChange(graph,cell.getId(),securityLevel));
//		}
//		graph.refresh();
//
//	}
//	
//	else {
//		JOptionPane.showMessageDialog(null,"No Analysis Context defined","Analysis Context Missing", JOptionPane.WARNING_MESSAGE);
//	}		
	}
}
