package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lola.LolaPresenter;
import de.uni.freiburg.iig.telematik.swat.lola.LolaRunner;
import de.uni.freiburg.iig.telematik.swat.lola.LolaRunner.LOLA_TEST;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class LolaAnalyzeAction extends AbstractAction {

	private static final long serialVersionUID = 9111775745565090191L;
	private SwatTabView tabView;
	String lolaDir;


	public LolaAnalyzeAction(SwatTabView tabView) {
		this.tabView = tabView;

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			LolaRunner lola = new LolaRunner((PNEditor) tabView.getSelectedComponent());
			HashMap<LOLA_TEST, String> result = lola.analyse();
			if (result == null)
				return;
			StringBuilder b = new StringBuilder();
			for (String singleResult : result.values()) {
				b.append(singleResult);
				b.append("\r\n");
			}
			LolaPresenter presenter = new LolaPresenter(b.toString());
			presenter.show();
			//run LoLA

			//present results
		} catch (ClassCastException e) {
			MessageDialog.getInstance().addMessage("This is not a Petri Net");
		}
 catch (NullPointerException e) {
			MessageDialog.getInstance().addMessage("This is not a Petri Net");
		}


	}

}
