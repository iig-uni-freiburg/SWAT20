package de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis;

import java.awt.event.ActionEvent;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.TransitionView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

public class TransitionTimeAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 6281674267218778803L;

	public TransitionTimeAction(PNEditor editor) {
		super(editor);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IFNetGraph graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		PNGraphCell cell = (PNGraphCell) graph.getSelectionCell();
		//TransitionView view = new TransitionView(cell.getId(), SwatComponents.getInstance().getTimeAnalysisForNet(editor.getNetContainer()));
		TransitionView view = new TransitionView(cell.getId(), SwatComponents.getInstance().getTimeContext(
				graph.getNetContainer().getPetriNet().getName(), "hardcodedTimeContext"));
		view.setVisible(true);
	}

}
