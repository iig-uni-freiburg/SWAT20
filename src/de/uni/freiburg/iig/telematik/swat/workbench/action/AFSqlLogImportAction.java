package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowSQLConnector;
import de.uni.freiburg.iig.telematik.swat.sciff.DatabaseChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

public class AFSqlLogImportAction extends AbstractWorkbenchAction {

	public AFSqlLogImportAction() {
		this("");
	}

	public AFSqlLogImportAction(String name) {
		super(name);
		setTooltip("Analyze AristaFlow Log");
		try {
			setIcon(IconFactory.getIcon("aristaflow"));
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		AristaFlowSQLConnector connector = DatabaseChooser.DatabaseChooser();
		//LogFileViewer viewer = con.dumpIntoWorkbench();
		LogModel model = connector.getModel();
		model = SwatComponents.getInstance().storeLogModelTo(model, model.getName());
		SwatComponents.getInstance().addLogModel(model);
		//SwatComponents.getInstance().reload();
		//connector.parse();
		//SciffAnalyzeAction sciffAction = new SciffAnalyzeAction(connector.getTempFile());

	}

}
