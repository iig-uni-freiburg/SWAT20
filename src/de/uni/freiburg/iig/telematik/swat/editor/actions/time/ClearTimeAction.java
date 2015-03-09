package de.uni.freiburg.iig.telematik.swat.editor.actions.time;

import java.awt.event.ActionEvent;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public class ClearTimeAction extends AbstractPNEditorAction {

	public ClearTimeAction(PNEditorComponent editor) throws ParameterException {
		super(editor);
		this.putValue(NAME, "clear timing");
	}

	private static final long serialVersionUID = 2722902765243066067L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		TimeContext context = SwatState.getInstance().getActiveContext(editor.getNetContainer().getPetriNet().getName());
		PNGraphCell cell = (PNGraphCell) editor.getGraphComponent().getGraph().getSelectionCell();
		context.removeBehavior(cell.getId());
		SwatComponents.getInstance().storeTimeContext(context, editor.getNetContainer().getPetriNet().getName());
	}

}
