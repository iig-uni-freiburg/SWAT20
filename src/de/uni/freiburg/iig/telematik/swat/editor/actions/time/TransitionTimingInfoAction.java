package de.uni.freiburg.iig.telematik.swat.editor.actions.time;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.TransitionView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public class TransitionTimingInfoAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 6281674267218778803L;

	public TransitionTimingInfoAction(PNEditorComponent editor) {
		super(editor);
		this.putValue(NAME, "get timing info");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		IFNetGraph graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		PNGraphCell cell = (PNGraphCell) graph.getSelectionCell();
		String cellName = cell.getId();
		String netID = editor.getNetContainer().getPetriNet().getName();
		if (hasTimingContext()) {
			if(hasActiveContext()){
				TimeContext context = SwatState.getInstance().getActiveContext(netID);
				TransitionView view = new TransitionView(cellName, context);
				view.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(editor, "please select time context first or create a new one...");
			}
		} else {
			//create a context
			String newContextName = optionDialog();
			if (newContextName != null) {
				TimeContext newContext = new TimeContext();
				newContext.setName(newContextName);
				newContext.setCorrespondingNet(netID);
				TransitionView view = new TransitionView(cellName, newContext);
				view.setVisible(true);
				SwatComponents.getInstance().addTimeContext(newContext, netID, true);
				SwatState.getInstance().setActiveContext(netID, newContextName);
			}

		}
	}

	protected boolean hasTimingContext() {
		return !SwatComponents.getInstance().getTimeContexts(editor.getNetContainer().getPetriNet().getName()).isEmpty();
	}

	protected boolean hasActiveContext() {
		try {
		SwatState.getInstance().getActiveContext(editor.getNetContainer().getPetriNet().getName());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected String optionDialog() {
		String s = (String) JOptionPane.showInputDialog(editor, "Enter a name for the new time context:", "Time Context Name",
				JOptionPane.PLAIN_MESSAGE);

		//If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			return s;
		} else
			return null;
	}

}
