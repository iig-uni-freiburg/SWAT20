package de.uni.freiburg.iig.telematik.swat.misc.timecontext.logAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogEntry;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;
import org.processmining.analysis.sciffchecker.logic.util.EventType;

import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser;

/** takes an ISCIFFLog and extracts timing information **/
public class ISCIFFAdapter implements TimeAwareAdapter, ISciffLogReader {

	private ISciffLogReader originalParser;
	private HashMap<String, ArrayList<Double>> time = new HashMap<String, ArrayList<Double>>();
	String[] activities;

	public static void main(String[] args) throws Exception {
		AristaFlowParser parser = new AristaFlowParser(new File("/home/richard/ReisekostenabrechnungSE.csv"));
		parser.parse(AristaFlowParser.whichTimestamp.BOTH);
		ISCIFFAdapter adapter = new ISCIFFAdapter(parser);
		for (String activity : adapter.getActivities()) {
			List<Double> duration = adapter.getDurations(activity, 1);
			if (duration != null && duration.size() > 0) {
				System.out.println("Activity " + activity + " ; Duration: " + listToString(duration));
			}

		}
	}

	private static long getSum(long[] array) {
		long sum = 0l;
		for (long l : array) {
			sum += l;
		}
		return sum;
	}

	private static String arrayToString(long[] array) {
		StringBuilder s = new StringBuilder();
		s.append("{");
		for (long l : array) {
			s.append(l);
			s.append(",");
		}
		s.append("}");
		return s.toString();
	}

	private static String listToString(List<Double> list) {
		StringBuilder s = new StringBuilder();
		s.append("{");
		for (double d : list) {
			s.append(d);
			s.append(",");
		}
		s.append("}");
		return s.toString();
	}

	public ISCIFFAdapter(ISciffLogReader parser) {
		this.originalParser = parser;
		activities = getAllActivities(); //parse all available activities in log
		for (ISciffLogTrace trace : parser.getInstances()) {
			try {
				investigateTrace(trace);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ISCIFFAdapter(File file) throws FileNotFoundException {
		this(new AristaFlowParser(file));
	}

	private void investigateTrace(ISciffLogTrace trace) throws IndexOutOfBoundsException, IOException {
		//activiy: [Activity name, corresponding Log entries from this activity]
		//activity holds every activity from this trace that are present and the corresponding log entries that deal with this activity
		HashMap<String, ArrayList<ISciffLogEntry>> activity = new HashMap<String, ArrayList<ISciffLogEntry>>(0);

		//put all activities inside activity
		for (int i = 0; i < trace.size(); i++) {
			String name = trace.get(i).getElement();
			if (activity.get(name) == null || activity.get(name).isEmpty())
				activity.put(name, new ArrayList<ISciffLogEntry>());//initialize
			activity.get(name).add(trace.get(i));
		}
		//every activities trace is now within activity
		//Sort activities according to their timestamp
		for (ArrayList<ISciffLogEntry> singleActivity : activity.values()) {
			Collections.sort(singleActivity, new TraceComperator());
		}

		//create timing
		for (Entry<String, ArrayList<ISciffLogEntry>> entry : activity.entrySet()) {
			//check if time already holds corresponding value
			if (time.isEmpty() || time.get(entry.getKey()) == null) {
				time.put(entry.getKey(), new ArrayList<Double>());
			}

			ArrayList<Double> timing = getTiming(entry.getValue());
			if (timing != null)
				time.get(entry.getKey()).addAll(timing);
		}
	}

	private ArrayList<Double> getTiming(ArrayList<ISciffLogEntry> entry) {
		ArrayList<Double> timing = new ArrayList<Double>(4);
		for (int i = 0; i < entry.size() - 1; i++) {
			//search for start and complete. TODO: handle resume, suspend, abort, skip...
			if (entry.get(i).getEventType().toLowerCase().equals(EventType.start.toString().toLowerCase())
					&& entry.get(i + 1).getEventType().toLowerCase().equals(EventType.complete.toString().toLowerCase())) {
				timing.add((double) (entry.get(i + 1).getTimestamp().getTime() - entry.get(i).getTimestamp().getTime()));
			}
		}
		if (timing.isEmpty())
			return null;
		return timing;
	}

	/** extract all available activities out of a log **/
	private String[] getAllActivities() {
		Set<String> set = new HashSet<String>();

		// for all instances: get the trace
		for (ISciffLogTrace trace : originalParser.getInstances()) {
			Iterator<ISciffLogEntry> iter = trace.iterator();
			//traverse all activities within trace
			while (iter.hasNext()) {
				ISciffLogEntry entry = iter.next();
				//add activitiy's name to set (no duplicate entries!)
				set.add(entry.getElement());
			}
		}
		String[] outcome = {};
		return set.toArray(outcome);
	}

	@Override
	public List<Double> getDurations(String activity, int resolution) {
		ArrayList<Double> buffer = time.get(activity);
		//System.out.println("Size of long array: " + buffer.size());
		return buffer;
		//		if (buffer == null || buffer.isEmpty())
		//			return null;
		//		long[] result = new long[buffer.size()];
		//		for (int i = 0; i < buffer.size(); i++) {
		//			result[i] = buffer.get(i);
		//		}
		//		return result;
	}

	@Override
	public List<ISciffLogTrace> getInstances() {
		// TODO Auto-generated method stub
		return originalParser.getInstances();
	}

	@Override
	public ISciffLogTrace getInstance(int index) {
		// TODO Auto-generated method stub
		return originalParser.getInstance(index);
	}

	@Override
	public int traceCount() {
		// TODO Auto-generated method stub
		return originalParser.traceCount();
	}

	@Override
	public ISciffLogSummary getSummary() {
		// TODO Auto-generated method stub
		return originalParser.getSummary();
	}

	@Override
	public String[] getActivities() {
		return activities;
	}

}

class TraceComperator implements Comparator<ISciffLogEntry> {

	@Override
	public int compare(ISciffLogEntry o1, ISciffLogEntry o2) {
		//negativ value: o1 is smaller. positive value: o2 is bigger
		return ((int) (o1.getTimestamp().getTime() - o2.getTimestamp().getTime()));
	}

}
