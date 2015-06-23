package de.uni.freiburg.iig.telematik.swat.plugin.sciff;

import java.io.IOException;
import java.util.Iterator;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogEntry;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;

import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class LogTraceAdapter implements ISciffLogTrace{
	
	private LogTrace<LogEntry> logTrace = null;

	public LogTraceAdapter(LogTrace<LogEntry> logTrace){
		this.logTrace = logTrace;
	}

	@Override
	public int size() {
		return logTrace.size();
	}

	@Override
	public ISciffLogEntry get(int index) throws IndexOutOfBoundsException, IOException {
		return new LogEntryAdapter(logTrace.getEntries().get(index));
	}

	@Override
	public Iterator<ISciffLogEntry> iterator() {
		return null;
	}

	@Override
	public int getNumberOfSimilarTraces() {
		return logTrace.getSimilarInstances().size();
	}

	@Override
	public String getName() {
		return String.valueOf(logTrace.getCaseNumber());
	}

}
