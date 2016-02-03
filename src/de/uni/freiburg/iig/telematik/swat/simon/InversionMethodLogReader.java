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
import java.util.concurrent.TimeUnit;

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
	}

	public List<LogTrace<LogEntry>> getLog() {
		return logs.get(0);
	}

	// public List<List<LogTrace<LogEntry>>> parseLog(String path) {
	// try {
	// logs = LogParser.parse(path);
	// for(int i = 0; i<logs.size(); i++) {
	// for(int z = 0; z<logs.get(i).size(); z++) {
	// System.out.println(logs.get(i).get(z).toString());}}
	// return logs;
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// } catch (ParserException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// }
	// }

	public HashMap<Long, Double> createHistogram(String activity) {
		ArrayList<Long> duration = getTimeofActivity(activity);

		HashMap<Long, Double> map = new HashMap<>();
		Collections.sort(duration);
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

	// public Long inversionMethod(HashMap<Long, Double> map) {
	//
	// long a = 0;
	// double random = Math.random();
	// double sum = 0;
	// System.out.println("random " + random);
	// for ( Long key : map.keySet() ) {
	// sum += map.get(key);
	// System.out.println("sum " + sum);
	// if(random <= sum) {
	// a= key;
	// System.out.println(a);
	// break;
	// }
	//
	// }
	// return a;
	//
	//
	// }
	/**
	 * compute proportion of duration occurence in ratio to 1
	 * 
	 * @param map
	 * @return
	 */
	public HashMap<Long, Double> probabilityTimeDiagram(HashMap<Long, Double> map) {
		Collection<Double> c = map.values();
		Iterator<Double> itr = c.iterator();
		double sum = 0;
		while (itr.hasNext()) {
			sum += (double) itr.next();
		}
		for (Long key : map.keySet()) {
			map.put(key, map.get(key) / sum);
		}
		System.out.println("probabiliyTimeDiagramMethod " + map.values().toString());
		return map;
	}

	/**
	 * Get time difference between start and endtime of an activity
	 * 
	 **/
	public static long getDateDiff(Date endTime, Date startTime, TimeUnit timeUnit) {
		long diffInMillies = endTime.getTime() - startTime.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	/**
	 * Get List of Pairs with start and end times of an activity
	 **/
	public ArrayList<Pair> getTimeofActivity2(String activity) {
		ArrayList<Pair> startEndTime = new ArrayList<>();
		// List<List<LogTrace<LogEntry>>> logs = parseLog(logPath);
		int counter = 0;
		if (logs != null) {
			for (int i = 0; i < logs.size(); i++) {
				for (int z = 0; z < logs.get(i).size(); z++) {
					System.out.println(logs.get(i).get(z).getEntriesForActivity(activity));
					for (int t = 0; t < logs.get(i).get(z).getEntriesForActivity(activity).size(); t++) {
						// System.out.println(logs.get(i).get(z).getEntriesForActivity(activity)
						// + "Anzahl: " + t);
						System.out.println(logs.get(i).get(z).getEntriesForActivity(activity).get(t).getTimestamp());
						counter++;
						if (t % 2 == 0) {
							Pair pair = new Pair(
									logs.get(i).get(z).getEntriesForActivity(activity).get(t).getTimestamp(),
									logs.get(i).get(z).getEntriesForActivity(activity).get(t + 1).getTimestamp());
							startEndTime.add(pair);
						}
					}
				}
			}
		}
		for (int i = 0; i < startEndTime.size(); i++) {
			System.out.println(" Startzeitpunkt:" + startEndTime.get(i).getStartTime() + ", Endzeitpunkt:"
					+ startEndTime.get(i).getEndTime() + ", Anzahl: " + i);
		}
		System.out.println(counter);
		return startEndTime;
	}

	public ITimeBehaviour getTimeBehaviourOfActivity(String name) {
		// TODO: was erwartet MeasuredTimeBehaviour?
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
						if (list.get(u).getEventType().equals(EventType.complete)) {
							if (!suspendResume.isEmpty()) {
								for (int i = 0; i < suspendResume.size(); i++) {
									difference += getDateDiff(suspendResume.get(i).getEndTime(), suspendResume.get(i).getStartTime(), TimeUnit.MINUTES);
								}
								time.add((getDateDiff(list.get(l).getTimestamp(), list.get(u).getTimestamp(), TimeUnit.MINUTES)) - difference);
								difference = 0;
								suspendResume.clear();

							}

							else {
								time.add(getDateDiff(list.get(u).getTimestamp(), list.get(l).getTimestamp(), TimeUnit.MINUTES));
							}
							l = u + 1;
						}
						if (list.get(u).getEventType().equals(EventType.suspend)) {
							suspend = list.get(u).getTimestamp();

							if (list.get(u).getEventType().equals(EventType.resume)) {
								resume = list.get(u).getTimestamp();
								suspendResume.add(new Pair(suspend, resume));
							}

						}

					}
				}
			}

		}
		return time;
	}
}