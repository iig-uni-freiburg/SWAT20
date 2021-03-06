package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.lola.LolaPresenter;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.lola.LolaRunner;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.lola.LolaRunner.LOLA_TEST;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class LolaAnalyzeAction<K> extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 9111775745565090191L;
	String lolaDir;

	public LolaAnalyzeAction() throws ParameterException, PropertyException, IOException {
		super("Lola", IconFactory.getIcon("detective"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

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

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		LolaRunner lola = new LolaRunner((PNEditorComponent) SwatTabView.getInstance().getSelectedComponent());
		HashMap<LOLA_TEST, String> result = lola.analyse();
		if (result == null)
			return;
		LolaPresenter presenter = new LolaPresenter(hashMapToString(result));
		presenter.show();

	}
}
