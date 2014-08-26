package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
/**
 * this is the specific subclass of AnalyzePanel which is instantiated when analysing
 * logfiles
 * @author bernhard
 *
 */
public class AnalyzePanelLogfile extends AnalyzePanel {

	public AnalyzePanelLogfile(SwatComponent component, String net) {
		super(component, net);
		objectInformationReader=new LogFileInformation( (LogFileViewer) component);
		objectChanged();
		update();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void analyze() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addCounterExampleButton(PatternResult result, JPanel newPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String adjustValue(ParamValue val) {
		// TODO Auto-generated method stub
		
		return val.getOperandName();
	}

}
