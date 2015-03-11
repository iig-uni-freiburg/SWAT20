package de.uni.freiburg.iig.telematik.swat.sciff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogEntry;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;

import de.uni.freiburg.iig.telematik.sewol.log.DataAttribute;
import de.uni.freiburg.iig.telematik.sewol.log.LockingException;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class AristaFlowParser implements ISciffLogReader, Serializable {
	private File log;
	private LinkedHashMap<String, ISciffLogTrace> traces = new LinkedHashMap<String, ISciffLogTrace>();
	private BufferedReader br;

	private LinkedHashMap<AristaFlowTokens, Integer> header;

	public static void main(String args[]) {
		File test = new File("/home/richard/ReisekostenabrechnungSE.csv");
		System.out.println("using file " + test.getAbsolutePath());
		try {
			AristaFlowParser parser = new AristaFlowParser(test);
			parser.parse(whichTimestamp.BOTH);
			System.out.println(parser.traces.keySet());
			System.out.println("Erster Eintrag: ");
			Iterator<ISciffLogTrace> iter = parser.traces.values().iterator();

			while (iter.hasNext()) {
				ISciffLogTrace trace = iter.next();
				//System.out.println(trace.getName() + ": ");
				//System.out.println(trace.get(0));
			}

			System.out.println("Instances: ");
			System.out.println(parser.getInstances());


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** Returns trace names **/
	public Set<String> getUniqueTraceNames() {
		return traces.keySet();
	}


	public AristaFlowParser(File logFile) throws FileNotFoundException {
		this.log = logFile;
		this.br = new BufferedReader(new java.io.FileReader(log));
		//parse();
	}

	public void parse(whichTimestamp useStartStopTimestamp) throws Exception {
		if (!isAristaFlowCSV()) {
			throw new ParseException(this.getClass().getName() + " - " + log.getName() + ": Is not an Arista-Flow CSV log", 0);
		}
		getHeader();
		parseAllInstances(useStartStopTimestamp);
	}

	private String sanitize(String s) {
		s = s.replace("\"", "");
		s = s.replace("%s", "");
		s = s.replace("%", "");
		return s;
	}

	private void parseAllInstances(whichTimestamp useStartOrStop) throws IOException {
		String nextLine;
		AristaFlowLogEntry endEntry;
		AristaFlowLogEntry startEntry;
		while ((nextLine = br.readLine()) != null) {
			nextLine = sanitize(nextLine);

			String[] currentLine = nextLine.split(";");
			
			//check if trace (instance) with given name already exists
			String instance = currentLine[header.get(AristaFlowTokens.INSTANCENAME)];
			if (traces.isEmpty() || traces.get(instance) == null) {
				//Insance is not available. Create new one
				traces.put(instance, new AristaFlowLogTrace(instance));
			}


			switch (useStartOrStop) {
			case END:
				endEntry = new AristaFlowLogEntry(currentLine, header, EventType.COMPLETE);
				((AristaFlowLogTrace) traces.get(instance)).add(endEntry);
				break;
			case START:
				startEntry = new AristaFlowLogEntry(currentLine, header, EventType.START);
				((AristaFlowLogTrace) traces.get(instance)).add(startEntry);
				break;
			case BOTH:
				endEntry = new AristaFlowLogEntry(currentLine, header, EventType.COMPLETE);
				startEntry = new AristaFlowLogEntry(currentLine, header, EventType.START);
				((AristaFlowLogTrace) traces.get(instance)).add(startEntry);
				((AristaFlowLogTrace) traces.get(instance)).add(endEntry);
				break;
			default:
				break;
			}

		}

	}

	/** Scan first line for header info **/
	private void getHeader() throws IOException {
		this.header = new LinkedHashMap<AristaFlowTokens, Integer>(5);
		String first = br.readLine(); //read first line
		first = sanitize(first); //sanitize header
		String[] tokens = first.split(";"); //get individual columns

		for (AristaFlowTokens token : AristaFlowTokens.values()) {
			header.put(token, searchIndex(tokens, token));
		}
		//return firstLine;
	}

	/** find token in tokens and return index **/
	private int searchIndex(String[] tokens, AristaFlowTokens token) {
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].toLowerCase().equals(token.toString().toLowerCase()))
				return i;
		}
		System.out.println(getClass() + ": Could not find token for " + token + " in Log!");
		return -1;
	}

	private boolean isAristaFlowCSV() {
		try {
			String firstLine = new BufferedReader(new java.io.FileReader(log)).readLine();
			return firstLine.toUpperCase().contains("START") && firstLine.toUpperCase().contains("END")
					&& firstLine.toUpperCase().contains("NAME") && firstLine.toUpperCase().contains("NODENAME")
					&& firstLine.toUpperCase().contains("INSTANCENAME");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<ISciffLogTrace> getInstances() {
		LinkedList<ISciffLogTrace> list = new LinkedList<ISciffLogTrace>(traces.values());
		return list;
	}

	@Override
	public ISciffLogTrace getInstance(int index) {
		//instances.keySet();
		LinkedList<ISciffLogTrace> list = new LinkedList<ISciffLogTrace>(traces.values());
		return list.get(index);
	}

	@Override
	public int traceCount() {

		return traces.size();
	}

	@Override
	public ISciffLogSummary getSummary() {
		return new AristaFlowLogSummary(this);
	}

	public enum whichTimestamp {
		START, END, BOTH;
	}

	public List<List<LogTrace<LogEntry>>> getLogEntries() {
		ArrayList<List<LogTrace<LogEntry>>> parsedLogFiles = new ArrayList<List<LogTrace<LogEntry>>>();
		List<LogTrace<LogEntry>> traceList = new ArrayList<LogTrace<LogEntry>>();
		parsedLogFiles.add(traceList);

		int i = 0;
		for (Entry<String, ISciffLogTrace> entry : traces.entrySet()) {
			LogTrace<LogEntry> logTrace = new LogTrace<LogEntry>(i);
			i++;
			for (int j = 0; j < entry.getValue().size(); j++) {
				String name;
				try {
					ISciffLogEntry iSciffLogEntry=entry.getValue().get(j);
					name = entry.getValue().get(j).getElement();
					LogEntry logEntry = new LogEntry(name);
					addAttributes(logEntry, entry.getValue().get(j));
					//logEntry.addTime(iSciffLogEntry.getTimestamp().getTime());
					logEntry.setTimestamp(iSciffLogEntry.getTimestamp());
					logEntry.setOriginator(iSciffLogEntry.getOriginator());
					logTrace.addEntry(logEntry);
				} catch (IndexOutOfBoundsException e) {
					//continue
				} catch (IOException e) {
					//continue
				} catch (LockingException e) {
					//continue
				}

			}

			traceList.add(logTrace);
		}

		return parsedLogFiles;
	}

	private void addAttributes(LogEntry logEntry, ISciffLogEntry iSciffLogEntry) {
		for (Entry<String, String> entry : iSciffLogEntry.getAttributes().entrySet()) {
			logEntry.addMetaAttribute(new DataAttribute(entry.getKey(), entry.getValue()));
		}

	}

	public Collection<String> getOriginatorCandidates() {
		HashSet<String> result = new HashSet<String>();
		ISciffLogEntry entry;

		for (ISciffLogTrace trace : getInstances()) {
			Iterator<ISciffLogEntry> iter = trace.iterator();
			while (iter.hasNext()) {
				entry = iter.next();
				result.add(entry.getOriginator());
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public Collection<String> getActivityCandidates() {

		HashSet<String> result = new HashSet<String>();
		ISciffLogEntry entry;

		for (ISciffLogTrace trace : getInstances()) {
			Iterator<ISciffLogEntry> iter = trace.iterator();
			while (iter.hasNext()) {
				entry = iter.next();
				result.add(entry.getElement());
			}
		}
		return Collections.unmodifiableSet(result);
	}

}

class AristaFlowLogSummary implements ISciffLogSummary, Serializable {
	private AristaFlowParser parser;

	public AristaFlowLogSummary(AristaFlowParser parser) {
		this.parser = parser;
	}

	@Override
	/** returns unique trace names **/
	public String[] getModelElements() {
		Set<String> list = parser.getUniqueTraceNames();
		String[] result = new String[list.size()];
		result = list.toArray(result);
		//		int size = list.size();
		//
		//		String result[] = new String[size];
		//
		//		int i = 0;
		//		for (String s : parser.getUniqueTraceNames()) {
		//			result[i] = s;
		//		}

		return result;
	}

	@Override
	public String[] getOriginators() {
		HashSet<String> set = new HashSet<String>();
		for (ISciffLogTrace trace : parser.getInstances()) {
			Iterator<ISciffLogEntry> iter = trace.iterator();
			while (iter.hasNext()) {
				ISciffLogEntry entry = iter.next();
				set.add(entry.getOriginator());
			}

		}
		
		int i = 0;
		String[] result = new String[set.size()];
		
		for (String s:set){
			result[i] = s;
		}

		return result;
	}

	public String[] getRoles() {
		// TODO Auto-generated method stub
		return null;
	}


}

class AristaFlowLogTrace implements ISciffLogTrace, Serializable {

	ArrayList<ISciffLogEntry> entries = new ArrayList<ISciffLogEntry>();
	public String name;

	public AristaFlowLogTrace(String name) {
		this.name = name;
	}

	@Override
	public int size() {
		return entries.size();
	}

	@Override
	public ISciffLogEntry get(int index) throws IndexOutOfBoundsException, IOException {
		return entries.get(index);
	}

	@Override
	public Iterator<ISciffLogEntry> iterator() {
		return entries.iterator();
	}

	@Override
	public int getNumberOfSimilarTraces() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void add(AristaFlowLogEntry entry) {
		entries.add(entry);
	}

	public String toString() {
		return name + " (" + entries.size() + " entries)";
	}

}

class AristaFlowLogEntry implements ISciffLogEntry, Serializable {

	public String element;
	public String originator;
	public EventType type;
	public Date timestamp;
	public Map<String, String> attributes = new HashMap<String, String>(5);
	private SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss:SSS");
	private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS");

	public AristaFlowLogEntry(String[] entries, HashMap<AristaFlowTokens, Integer> firstLine, EventType type) {

		originator = entries[firstLine.get(AristaFlowTokens.NAME)];
		element = entries[firstLine.get(AristaFlowTokens.NODENAME)];
		this.type = type;
		AristaFlowTokens token;

		if (type.equals(EventType.START))
			token = AristaFlowTokens.START;
		else
			token = AristaFlowTokens.END;

		try {
			timestamp = formatter1.parse(entries[firstLine.get(token)]);
		} catch (java.text.ParseException e) {
			try {
				timestamp = formatter2.parse(entries[firstLine.get(token)]);
			} catch (java.text.ParseException e1) {
				System.out.println(this.getClass().getName() + ": Could not parse date: " + entries[firstLine.get(token)]);
				e1.printStackTrace();
			}


		}
		attributes.put("Activity", entries[firstLine.get(AristaFlowTokens.NODENAME)]);
		attributes.put("User", entries[firstLine.get(AristaFlowTokens.LASTNAME)]);
	}


	@Override
	public String getElement() {
		// TODO Auto-generated method stub
		return element;
	}

	@Override
	public String getEventType() {
		// TODO Auto-generated method stub
		return type.toString();
	}

	@Override
	public Date getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

	@Override
	public String getOriginator() {
		// TODO Auto-generated method stub
		return originator;
	}

	@Override
	public Map<String, String> getAttributes() {
		if (attributes == null || attributes.isEmpty())
			return null;
		return attributes;
	}

	public String toString() {
		return timestamp + ": " + getElement() + " " + getEventType() + " from " + getOriginator();
	}


	public String getRole() {
		// TODO Auto-generated method stub
		return null;
	}



}



enum AristaFlowTokens implements Serializable {
	START, END, NODENAME, NAME, INSTANCENAME, LASTNAME;
}

enum EventType implements Serializable {
	COMPLETE, START, END;
}

