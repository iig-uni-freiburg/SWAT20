package de.uni.freiburg.iig.telematik.swat.plugin.sciff;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;

import de.uni.freiburg.iig.telematik.sewol.log.LogSummary;

public class LogSummaryAdapter implements ISciffLogSummary{

	private LogSummary logSummary = null;
	
	public LogSummaryAdapter(LogSummary logSummary){
		this.logSummary = logSummary;
	}

	@Override
	public String[] getModelElements() {
		return (String[]) logSummary.getActivities().toArray(new String[0]);
	}

	@Override
	public String[] getOriginators() {
		return (String[]) logSummary.getOriginators().toArray(new String[0]);
	}

	@Override
	public String[] getRoles() {
		return (String[]) logSummary.getRoles().toArray(new String[0]);
	}

}
