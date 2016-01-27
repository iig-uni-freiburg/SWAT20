package de.uni.freiburg.iig.telematik.swat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;

public class ParserTest {
	
	public static void main (String args[]) throws IOException, ParserException{
		LogParser parser = new LogParser();
		List<List<LogTrace<LogEntry>>> log = parser.parse(new File ("/home/richard/SWAT Workbenches/8/SwatWorkingDirectory/logs/P2P-log-v6-anonymized/P2P-log-v6-anonymized.mxml"));
		LogTrace<LogEntry> bla = log.get(0).get(1);
		System.out.println(bla.getDistinctOriginators());
		
	}

}
