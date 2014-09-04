package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
/**
 * this is the specific subclass of AnalyzePanel which implements
 * the functionality used to analyze logfiles
 * @author bernhard
 *
 */
public class AnalyzePanelLogfile extends AnalyzePanel {

	/**
	 * Create an AnalyzePanel for analyzing Logfiles
	 * @param component the LogFileViewer
	 * @param log the filename of the logfile
	 */
	public AnalyzePanelLogfile(SwatComponent component, String log) {
		super(component, log);
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
