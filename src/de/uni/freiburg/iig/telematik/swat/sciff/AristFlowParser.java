package de.uni.freiburg.iig.telematik.swat.sciff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.batik.parser.ParseException;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogEntry;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;

public class AristFlowParser implements ISciffLogReader {
	private File log;
	private LinkedHashMap<String, ISciffLogTrace> traces = new LinkedHashMap<String, ISciffLogTrace>();
	private BufferedReader br;

	private LinkedHashMap<AristFlowTokens, Integer> header;

	public static void main(String args[]) {
		File test = new File("/home/richard/ReisekostenabrechnungSE.csv");
		System.out.println("using file " + test.getAbsolutePath());
		try {
			AristFlowParser parser = new AristFlowParser(test);
			parser.parse(whichTimestamp.BOTH);
			System.out.println(parser.traces.keySet());
			System.out.println("Erster Eintrag: ");
			Iterator<ISciffLogTrace> iter = parser.traces.values().iterator();

			while (iter.hasNext()) {
				ISciffLogTrace trace = iter.next();
				System.out.println(trace.getName() + ": ");
				System.out.println(trace.get(0));
			}

			System.out.println("Instances: ");
			System.out.println(parser.getInstances());


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** Returns trace names **/
	public Set<String> getUniqueTraceNames() {
		return traces.keySet();
	}

	public AristFlowParser(File logFile) throws FileNotFoundException {
		this.log = logFile;
		this.br = new BufferedReader(new java.io.FileReader(log));
		//parse();
	}

	public void parse(whichTimestamp useStartStopTimestamp) throws IOException {
		if (!isAristaFlowCSV()) {
			throw new ParseException(this.getClass().getName() + ": Is not an Arista-Flow CSV log", -1, -1);
		}
		getHeader();
		parseAllInstances(useStartStopTimestamp);

	}

	private void parseAllInstances(whichTimestamp useStartOrStop) throws IOException {
		String nextLine;
		while ((nextLine = br.readLine()) != null) {

			String[] entries = nextLine.split(";");
			
			//check if trace (instance) with given name already exists
			String instance = entries[header.get(AristFlowTokens.INSTANCENAME)];
			if (traces.isEmpty() || traces.get(instance) == null) {
				traces.put(instance, new AristaFlowLogTrace(instance));
			}

			AristaFlowLogEntry endEntry;
			AristaFlowLogEntry startEntry;
			
			switch (useStartOrStop) {
			case END:
				endEntry = new AristaFlowLogEntry(entries, header, EventType.COMPLETE);
				((AristaFlowLogTrace) traces.get(instance)).add(endEntry);
				break;
			case START:
				startEntry = new AristaFlowLogEntry(entries, header, EventType.START);
				((AristaFlowLogTrace) traces.get(instance)).add(startEntry);
				break;
			case BOTH:
				endEntry = new AristaFlowLogEntry(entries, header, EventType.COMPLETE);
				startEntry = new AristaFlowLogEntry(entries, header, EventType.START);
				((AristaFlowLogTrace) traces.get(instance)).add(startEntry);
				((AristaFlowLogTrace) traces.get(instance)).add(endEntry);
				break;
			default:
				break;
			}

		}

	}

	private void getHeader() throws IOException {
		this.header = new LinkedHashMap<AristFlowTokens, Integer>(5);
		String first = br.readLine();
		String[] tokens = first.split(";");

		for (AristFlowTokens token : AristFlowTokens.values()) {
			header.put(token, searchIndex(tokens, token));
		}
		//return firstLine;
	}

	/** Returns the index where to find the token **/
	private int searchIndex(String[] tokens, AristFlowTokens token) {
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
			return firstLine.contains("START") && firstLine.contains("END") && firstLine.contains("NAME") && firstLine.contains("NODENAME")
					&& firstLine.contains("INSTANCENAME");
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

}

class AristaFlowLogSummary implements ISciffLogSummary {
	private AristFlowParser parser;

	public AristaFlowLogSummary(AristFlowParser parser) {
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


}

class AristaFlowLogTrace implements ISciffLogTrace {

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

class AristaFlowLogEntry implements ISciffLogEntry {

	public String element;
	public String originator;
	public EventType type;
	public Date timestamp;
	public Map<String, String> attributes = new HashMap<String, String>(5);
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss:SSS");

	public AristaFlowLogEntry(String[] entries, HashMap<AristFlowTokens, Integer> firstLine, EventType type) {

		originator = entries[firstLine.get(AristFlowTokens.NAME)];
		element = entries[firstLine.get(AristFlowTokens.NODENAME)];
		this.type = type;
		try {
			timestamp = formatter.parse(entries[firstLine.get(AristFlowTokens.END)]);
		} catch (java.text.ParseException e) {
			System.out.println(this.getClass().getName() + ": Could not parse date: " + entries[firstLine.get(AristFlowTokens.END)]);
			e.printStackTrace();
		}
		attributes.put("Activity", entries[firstLine.get(AristFlowTokens.NODENAME)]);
		attributes.put("User", entries[firstLine.get(AristFlowTokens.LASTNAME)]);
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



}



enum AristFlowTokens {
	START, END, NODENAME, NAME, INSTANCENAME, LASTNAME;
}

enum EventType {
	COMPLETE, START, END;
}
