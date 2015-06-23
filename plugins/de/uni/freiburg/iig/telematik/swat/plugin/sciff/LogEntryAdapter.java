package de.uni.freiburg.iig.telematik.swat.plugin.sciff;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogEntry;

import de.uni.freiburg.iig.telematik.sewol.log.DataAttribute;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class LogEntryAdapter implements ISciffLogEntry {
	
	private LogEntry logEntry = null;

	public LogEntryAdapter(LogEntry logEntry) {
		this.logEntry = logEntry;
	}

	@Override
	public String getElement() {
		return logEntry.getActivity();
	}

	@Override
	public String getEventType() {
		if (logEntry.getEventType() == null)
			return null;
		return logEntry.getEventType().toString();
	}

	@Override
	public Date getTimestamp() {
		return logEntry.getTimestamp();
	}

	@Override
	public String getOriginator() {
		return logEntry.getOriginator();
	}

	@Override
	public Map<String, String> getAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		for(DataAttribute att: logEntry.getMetaAttributes()){
			attributes.put(att.name, att.value.toString());
		}
		return attributes;
	}

	@Override
	public String getRole() {
		return logEntry.getRole();
	}
	
	
}
