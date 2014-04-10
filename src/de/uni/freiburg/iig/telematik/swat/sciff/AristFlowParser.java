package de.uni.freiburg.iig.telematik.swat.sciff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.batik.parser.ParseException;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogEntry;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;

public class AristFlowParser implements ISciffLogReader {
	private File log;
	private LinkedHashMap<String, ISciffLogTrace> instances = new LinkedHashMap<String, ISciffLogTrace>();
	private BufferedReader br;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss:SSS");

	private HashMap<AristFlowTokens, Integer> firstLine;

	public static void main(String args[]) {
		File test = new File("/home/richard/ReisekostenabrechnungSE.csv");
		System.out.println("using file " + test.getAbsolutePath());
		try {
			AristFlowParser parser = new AristFlowParser(test);
			parser.parse();
			System.out.println(parser.instances.keySet());
			System.out.println("Erster Eintrag: ");
			Iterator<ISciffLogTrace> iter = parser.instances.values().iterator();

			while (iter.hasNext()) {
				ISciffLogTrace trace = iter.next();
				System.out.println(trace.getName() + ": ");
				System.out.println(trace.get(0));
			}

			System.out.println("Instances: ");
			System.out.println(parser.getInstances());


			//System.out.println(parser.instances.values().);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public AristFlowParser(File logFile) throws FileNotFoundException {
		this.log = logFile;
		this.br = new BufferedReader(new java.io.FileReader(log));
		//parse();
	}

	public void parse() throws IOException {
		if (!isAristaFlowCSV()) {
			throw new ParseException(this.getClass().getName() + ": Is not an Arista-Flow CSV log", -1, -1);
		}
		firstLine = getFirstLine();
		getAllInstances();

	}

	private void getAllInstances() throws IOException {
		String nextLine;
		while ((nextLine = br.readLine()) != null) {

			String[] entries = nextLine.split(";");

			AristaFlowLogEntry entry = new AristaFlowLogEntry();
			entry.originator = entries[firstLine.get(AristFlowTokens.NAME)];
			entry.element = entries[firstLine.get(AristFlowTokens.NODENAME)];
			entry.type = EventType.COMPLETE;
			try {
				entry.timestamp = formatter.parse(entries[firstLine.get(AristFlowTokens.END)]);
			} catch (java.text.ParseException e) {
				System.out.println(this.getClass().getName() + ": Could not parse date: " + entries[firstLine.get(AristFlowTokens.END)]);
				e.printStackTrace();
			}

			String instance = entries[firstLine.get(AristFlowTokens.INSTANCENAME)];


			if (instances.isEmpty() || instances.get(instance) == null) {
				instances.put(instance, new AristaFlowLogTrace(instance));
			}

			((AristaFlowLogTrace) instances.get(instance)).add(entry);

		}

	}

	private HashMap<AristFlowTokens, Integer> getFirstLine() throws IOException {
		HashMap<AristFlowTokens, Integer> firstLine = new HashMap<AristFlowTokens, Integer>(5);
		String first = br.readLine();
		String[] tokens = first.split(";");

		for (AristFlowTokens token : AristFlowTokens.values()) {
			firstLine.put(token, searchIndex(tokens, token));
		}
		return firstLine;
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
		// TODO Auto-generated method stub
		return new LinkedList<ISciffLogTrace>(instances.values());
	}

	@Override
	public ISciffLogTrace getInstance(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int traceCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ISciffLogSummary getSummary() {
		// TODO Auto-generated method stub
		return null;
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
		return name + " [" + entries.size() + " entries]";
	}

}

class AristaFlowLogEntry implements ISciffLogEntry {

	public String element;
	public String originator;
	public EventType type;
	public Date timestamp;
	public Map<String, String> attributes;

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
	START, END, NODENAME, NAME, INSTANCENAME;
}

enum EventType {
	COMPLETE, START, END;
}
