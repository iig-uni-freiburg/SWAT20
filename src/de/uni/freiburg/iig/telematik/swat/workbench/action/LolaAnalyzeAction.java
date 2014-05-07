package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lola.LolaPresenter;
import de.uni.freiburg.iig.telematik.swat.lola.LolaRunner;
import de.uni.freiburg.iig.telematik.swat.lola.LolaRunner.LOLA_TEST;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class LolaAnalyzeAction<K> extends AbstractAction {

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
			LolaPresenter presenter = new LolaPresenter(hashMapToString(result));
			presenter.show();

		} catch (ClassCastException e) {
			MessageDialog.getInstance().addMessage("This is not a Petri Net");
		}

		catch (NullPointerException e) {
			MessageDialog.getInstance().addMessage("Could not analyse with LoLA Reason: " + e.getMessage() + " " + e.getCause());
		}
	}
	

	private String hashMapToString(HashMap<LOLA_TEST, String> hashMap) {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<LOLA_TEST, String> entry : hashMap.entrySet()) {
			builder.append(entry.getKey().toString());
			builder.append(" - ");
			builder.append(entry.getValue());
			builder.append("\r\n");
		}
		return builder.toString();

	}


}
