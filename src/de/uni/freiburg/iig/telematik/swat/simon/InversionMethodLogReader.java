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
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;


public class InversionMethodLogReader {

	List<List<LogTrace<LogEntry>>> logs;
	
	public InversionMethodLogReader(String pathToLogFile) throws IOException, ParserException {
		logs = LogParser.parse(pathToLogFile);
		
		
	}
	
//	public  List<List<LogTrace<LogEntry>>>  parseLog(String path) {
//		try {
//			logs = LogParser.parse(path);
//			for(int i = 0; i<logs.size(); i++) {
//				for(int z = 0; z<logs.get(i).size(); z++) {
//			System.out.println(logs.get(i).get(z).toString());}}
//			return logs;
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (ParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public HashMap<Long, Double> createHistogram(String logPath, String activity) {
		ArrayList<Pair> timePairs = getTimeofActivity(logPath, activity);
		ArrayList<Long> duration = new ArrayList<Long>();
		for(int i = 0; i< timePairs.size(); i++) {
			System.out.println(getDateDiff(timePairs.get(i).getEndTime(), timePairs.get(i).getStartTime(), TimeUnit.MINUTES));
			duration.add(getDateDiff(timePairs.get(i).getEndTime(), timePairs.get(i).getStartTime(), TimeUnit.MINUTES));	
		}
		HashMap<Long, Double> map = new HashMap<>();
		Collections.sort(duration);
		// counting number of occurrences of each Integers in the Arraylist
		for(int i = 0; i< timePairs.size(); i++) {
			if (!map.containsKey(duration.get(i))) {
			map.put(duration.get(i), (double) 1);}
			else if (map.containsKey(duration.get(i))) {
				map.put(duration.get(i), map.get(duration.get(i)) + 1);
			}
			
		}
		
		System.out.println(map.values().toString());
		return map;
	}
	
//	public Long inversionMethod(HashMap<Long, Double> map) {
//
//		long a = 0;
//		double random = Math.random();
//		double sum = 0;
//		System.out.println("random " + random);
//		for ( Long key : map.keySet() ) {
//			sum += map.get(key);
//			System.out.println("sum " + sum);
//		if(random <= sum) {
//			a=  key;
//			System.out.println(a);
//			break;
//		}
//		
//	}
//		return a;
//		
//		
//	}
	
	
	
	public HashMap<Long, Double> probabilityTimeDiagram(HashMap<Long, Double> map) {
		Collection c = map.values();
	    Iterator itr = c.iterator();
	    double sum = 0;
	    while (itr.hasNext()) {
	      sum +=  (double)itr.next();
	    }
	    for ( Long key : map.keySet() ) {
	    	map.put(key, map.get(key)/ sum);
	    }
	   System.out.println(map.values().toString());
	    return map;
	}
	/**
	 * Get time difference between start and endtime of an activity
	 * 
	 **/
	public static long getDateDiff(Date endTime, Date startTime, TimeUnit timeUnit) {
	    long diffInMillies = endTime.getTime() - startTime.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	/**
	 * Get List of Pairs with start and end times of an activity
	 **/
	public ArrayList<Pair> getTimeofActivity(String logPath, String activity) {
		ArrayList<Pair> startEndTime = new ArrayList<>();
		//List<List<LogTrace<LogEntry>>> logs = parseLog(logPath);
		int counter = 0;
		if(logs != null) {
			for(int i = 0; i<logs.size(); i++) {
				for(int z = 0; z<logs.get(i).size(); z++) {
					for(int t = 0; t<logs.get(i).get(z).getEntriesForActivity(activity).size(); t++) {
						System.out.println(logs.get(i).get(z).getEntriesForActivity(activity) + "Anzahl: " + t);
						System.out.println(logs.get(i).get(z).getEntriesForActivity(activity).get(t).getTimestamp());
						counter++;
						if(t % 2 == 0) {
							 Pair pair = new Pair(logs.get(i).get(z).getEntriesForActivity(activity).get(t).getTimestamp(), logs.get(i).get(z).getEntriesForActivity(activity).get(t+1).getTimestamp()); 
							 startEndTime.add(pair);
						}
					}}}
	}
		for(int i = 0; i< startEndTime.size(); i++) {
		System.out.println("Startzeitpunkt:" + startEndTime.get(i).getStartTime() +  ", Endzeitpunkt:" + startEndTime.get(i).getEndTime() + ", Anzahl: " + i ); 
		}
		System.out.println(counter);
		return startEndTime;
	}}

