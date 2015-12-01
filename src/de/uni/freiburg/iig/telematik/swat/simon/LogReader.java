package de.uni.freiburg.iig.telematik.swat.simon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;


public class LogReader {

	
	
	public LogReader() {
		
	}
	
	public  List<List<LogTrace<LogEntry>>>  parseLog(String path) {
		try {
			List<List<LogTrace<LogEntry>>> logs = LogParser.parse(path);
			for(int i = 0; i<logs.size(); i++) {
				for(int z = 0; z<logs.get(i).size(); z++) {
			System.out.println(logs.get(i).get(z).toString());}}
			return logs;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * Get time difference between start and endtime of an activity
	 * 
	 */
	public void createHistogram(String logPath, String activity) {
		ArrayList<Pair> timePairs = getTimeofActivity(logPath, activity);
		for(int i = 0; i< timePairs.size(); i++) {
			System.out.println(getDateDiff(timePairs.get(i).getEndTime(), timePairs.get(i).getStartTime(), TimeUnit.MINUTES));
			long timeDiff = getDateDiff(timePairs.get(i).getEndTime(), timePairs.get(i).getStartTime(), TimeUnit.MINUTES);
		}
	}
	

	public static long getDateDiff(Date endTime, Date startTime, TimeUnit timeUnit) {
	    long diffInMillies = endTime.getTime() - startTime.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	/*
	 * Get List of Pairs with start and end times of an activity
	 */
	public ArrayList<Pair> getTimeofActivity(String logPath, String activity) {
		ArrayList<Pair> startEndTime = new ArrayList<>();
		List<List<LogTrace<LogEntry>>> logs = parseLog(logPath);
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

