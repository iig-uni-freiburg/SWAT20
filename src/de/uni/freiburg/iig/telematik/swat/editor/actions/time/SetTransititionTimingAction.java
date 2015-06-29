package de.uni.freiburg.iig.telematik.swat.editor.actions.time;

import java.awt.event.ActionEvent;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.AbstractDistributionView;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.DistributionChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public class SetTransititionTimingAction extends AbstractPNEditorAction {

	public SetTransititionTimingAction(PNEditorComponent editor) throws ParameterException {
		super(editor);
		this.putValue(NAME, "set timing behavior");
	}

	private static final long serialVersionUID = 8902738458250812750L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		TimeContext context = SwatState.getInstance().getActiveContext(editor.getNetContainer().getPetriNet().getName());
		IFNetGraph graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		PNGraphCell cell = (PNGraphCell) graph.getSelectionCell();
		System.out.println("Time context for " + cell.getId() + " before: " + context.getTimeBehavior(cell.getId()));
		AbstractDistributionView view = context.getTimeBehavior(cell.getId());
		DistributionChooser chooser = new DistributionChooser(view);
		chooser.setVisible(true);
		if (chooser.userAbort())
			System.out.println("User aborted");
		if (!chooser.userAbort()) {
			context.addTimeBehavior(cell.getId(), chooser.getDistribution());
			try {
				SwatComponents.getInstance().storeTimeContext(context, editor.getNetContainer().getPetriNet().getName());
			} catch (SwatComponentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("Time context for " + cell.getId() + " AFTER: " + context.getTimeBehavior(cell.getId()));
	}

}
