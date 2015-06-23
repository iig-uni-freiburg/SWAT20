package de.uni.freiburg.iig.telematik.swat.plugin.sciff;

import java.util.ArrayList;
import java.util.List;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParserInterface;

public class LogParserAdapter implements ISciffLogReader {
	
	private List<ISciffLogTrace> logTraces = null;
	private ISciffLogSummary logSummary = null;
	
	public LogParserAdapter(LogParserInterface logParser) throws ParameterException{
		getSciffTraceList(logParser.getFirstParsedLog());
		logSummary = new LogSummaryAdapter(logParser.getSummary(0));
	}

	@Override
	public List<ISciffLogTrace> getInstances() {
		return logTraces;
	}

	@Override
	public ISciffLogTrace getInstance(int index) {
		return logTraces.get(index);
	}

	@Override
	public int traceCount() {
		return logTraces.size();
	}

	@Override
	public ISciffLogSummary getSummary() {
		return logSummary;
	}
	
	private void getSciffTraceList(List<LogTrace<LogEntry>> logTraces) {
		this.logTraces = new ArrayList<ISciffLogTrace>();
		for(LogTrace<LogEntry> trace: logTraces){
			this.logTraces.add(new LogTraceAdapter(trace));
		}
	}

}
