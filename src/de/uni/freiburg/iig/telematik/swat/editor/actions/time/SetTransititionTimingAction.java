package de.uni.freiburg.iig.telematik.swat.editor.actions.time;

import java.awt.event.ActionEvent;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.AbstractDistributionView;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.DistributionChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;

public class SetTransititionTimingAction extends AbstractPNEditorAction {

	public SetTransititionTimingAction(PNEditor editor) throws ParameterException {
		super(editor);
		this.putValue(NAME, "set timing behavior");
	}

	private static final long serialVersionUID = 8902738458250812750L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		TimeContext context = SwatState.getInstance().getActiveContext(editor.getNetContainer().getPetriNet().getName());
		IFNetGraph graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		PNGraphCell cell = (PNGraphCell) graph.getSelectionCell();
		AbstractDistributionView view = context.getTimeBehavior(cell.getId());
		DistributionChooser chooser = new DistributionChooser(view);
		chooser.setVisible(true);
		if (!chooser.userAbort()) {
			context.addTimeBehavior(cell.getId(), chooser.getDistribution());
			SwatComponents.getInstance().storeTimeContext(context, editor.getNetContainer().getPetriNet().getName());
		}
	}

}
