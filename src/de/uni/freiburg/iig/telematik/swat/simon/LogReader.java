package de.uni.freiburg.iig.telematik.swat.simon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	public void searchDistributionOfActivity(String logPath, String activity) {
		ArrayList<Pair> startEndTime = new ArrayList<Pair>();
		List<List<LogTrace<LogEntry>>> logs = parseLog(logPath);
		if(logs != null) {
			for(int i = 0; i<logs.size(); i++) {
				for(int z = 0; z<logs.get(i).size(); z++) {
					for(int t = 0; t<logs.get(i).get(z).getEntriesForActivity(activity).size(); t++) {
						System.out.println(logs.get(i).get(z).getEntriesForActivity(activity));
						logs.get(i).get(z).getEntriesForActivity(activity).get(t).getTimestamp();
						}
				
			}
		}
	}
	}}

