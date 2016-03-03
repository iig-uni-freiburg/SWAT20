package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sewol.log.DataAttribute;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class ActivityCompoundExtractor {
	
	private HashSet<Compound> compoundSet;
	List<LogTrace<LogEntry>> log;
	String AttributeName = "Material";
	
	public ActivityCompoundExtractor (List<LogTrace<LogEntry>> log){
		this.log = log;
		createCompoundSet();
	}
	
	private void createCompoundSet(){
		compoundSet = new HashSet<Compound>();
		String activity;
		String human;
		String material;
		for (LogTrace<LogEntry> trace : log) {
			for (LogEntry logEntry : trace.getEntries()) {			
				activity = logEntry.getActivity();
				human = logEntry.getOriginator();
				material = getMaterial(logEntry);
				compoundSet.add(new Compound(activity, human, material));
			}
		}
	}

	private String getMaterial(LogEntry logEntry) {
		Set<DataAttribute> DA_list = logEntry.getMetaAttributes();
		for (DataAttribute DA:DA_list){
			if (DA.name.equals(AttributeName)){
				return DA.value.toString();
			}
		}
		return null;
	}

	public HashSet<Compound> getCompoundSet(){
		return compoundSet;
	}
}
