package de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.mxgraph.model.mxGraphModel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.graph.SwatIFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TransitionLabelingChange;
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

		AnalysisContext ac = ((SwatIFNetGraph) graph).getCurrentAnalysisContext();
		if (ac != null) {
			((mxGraphModel) graph.getModel()).execute(new TransitionLabelingChange((SwatIFNetGraph) graph, cell.getId(), securityLevel));
			graph.refresh();
		} else {
			JOptionPane.showMessageDialog(null, "No Analysis Context defined", "Analysis Context Missing", JOptionPane.WARNING_MESSAGE);
		}
	}
}
