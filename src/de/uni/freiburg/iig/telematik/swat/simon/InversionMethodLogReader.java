package de.uni.freiburg.iig.telematik.swat.simon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sewol.log.EventType;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;

public class InversionMethodLogReader {

	List<List<LogTrace<LogEntry>>> logs;

	public InversionMethodLogReader(String pathToLogFile) throws IOException, ParserException {
		logs = LogParser.parse(pathToLogFile);
		
		 for(int i = 0; i<logs.size(); i++) {
		 for(int z = 0; z<logs.get(i).size(); z++) {
		 System.out.println(logs.get(i).get(z).toString());}}
	}

	public List<LogTrace<LogEntry>> getLog() {
		return logs.get(0);
		
	}

	

	public HashMap<Long, Double> createHistogram(String activity) {
		ArrayList<Long> duration = getTimeofActivity(activity);

		HashMap<Long, Double> map = new HashMap<>();
		
		
		// counting number of occurrences of each Integers in the Arraylist
		for (int i = 0; i < duration.size(); i++) {
			if (!map.containsKey(duration.get(i))) {
				map.put(duration.get(i), (double) 1);
			} else if (map.containsKey(duration.get(i))) {
				map.put(duration.get(i), map.get(duration.get(i)) + 1);
			}

		}

		System.out.println(map.values().toString());
		return map;
	}

	
	/**
	 * compute proportion of duration occurence 
	 * 
	 * @param map
	 * @return
	 */
	public Map<Long, Double> probabilityTimeDiagram(HashMap<Long, Double> map) {
		Collection<Double> c = map.values();
		
		Iterator<Double> itr = c.iterator();
		
		double sum = 0;
		while (itr.hasNext()) {
			sum += (double) itr.next();
		}
		for (Long key : map.keySet()) {
			map.put(key, map.get(key) / sum);
		}
		
		Map<Long, Double> treeMap = new TreeMap<Long, Double>(map);
		
		System.out.println("probabiliyTimeDiagramMethod " + "keyset (duration in min): " + treeMap.keySet().toString() + " and the probability: "+ treeMap.values().toString());
		return treeMap;
	}
	
	public void sortKeyMap(HashMap<Long, Double> map) {
		
	}

	/**
	 * Get time difference between start and endtime of an activity
	 * 
	 **/
	public static long getDateDiff(Date endTime, Date startTime, TimeUnit timeUnit) {
		long diffInMillies = endTime.getTime() - startTime.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}


	public ITimeBehaviour getTimeBehaviourOfActivity(String name) {
		return new MeasuredTimeBehaviour(probabilityTimeDiagram(createHistogram(name)));
	}

	public ArrayList<Long> getTimeofActivity(String activity) {
		ArrayList<Long> time = new ArrayList<Long>();
		ArrayList<Pair> suspendResume = new ArrayList<>();
		List<LogEntry> list = new ArrayList<LogEntry>();

		Date suspend = null;
		Date resume = null;

		if (logs != null) {
			for (int i = 0; i < logs.size(); i++) {
				for (int z = 0; z < logs.get(i).size(); z++) {
					for (int t = 0; t < logs.get(i).get(z).getEntriesForActivity(activity).size(); t++) {
						list.add(logs.get(i).get(z).getEntriesForActivity(activity).get(t));

					}
				}
			}
			long difference = 0;
			for (int l = 0; l < list.size(); l++) {
				if (list.get(l).getEventType().equals(EventType.start)) {
					for (int u = l + 1; u < list.size(); u++) {
						if(list.get(u).getTimestamp() != null && list.get(l).getTimestamp() != null) {
						if (list.get(u).getEventType().equals(EventType.complete)) {
							
							if (!suspendResume.isEmpty()) {
								
								for (int i = 0; i < suspendResume.size(); i++) {
									difference += getDateDiff(suspendResume.get(i).getEndTime(), suspendResume.get(i).getStartTime(), TimeUnit.MINUTES);
								}
								time.add((getDateDiff(list.get(u).getTimestamp(), list.get(l).getTimestamp(), TimeUnit.MINUTES)) - difference);
								System.out.println(getDateDiff(list.get(u).getTimestamp(), list.get(l).getTimestamp(), TimeUnit.MINUTES) - difference);
								System.out.println(difference);
								System.out.println(getDateDiff(list.get(u).getTimestamp(), list.get(l).getTimestamp(), TimeUnit.MINUTES));
								difference = 0;
								suspendResume.clear();
							}

							else {
								time.add(getDateDiff(list.get(u).getTimestamp(), list.get(l).getTimestamp(), TimeUnit.MINUTES));
							}
							l = u + 1;
						}
						else if (list.get(u).getEventType().equals(EventType.suspend)) {
							suspend = list.get(u).getTimestamp();
							System.out.println("suspend found");
						}
						else if (list.get(u).getEventType().equals(EventType.resume)) {
								resume = list.get(u).getTimestamp();
								suspendResume.add(new Pair(suspend, resume));
								System.out.println("resume found");
							}
						else if (list.get(u).getEventType().equals(EventType.ate_abort) || list.get(u).getEventType().equals(EventType.pi_abort)) {
							l = u + 1;
							suspendResume.clear();
							System.out.println("abort found");
						}
						}
						}

					}
				}
			}

		
		return time;
	}
}