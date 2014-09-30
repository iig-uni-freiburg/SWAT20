package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.jawl.log.LogEntry;
import de.uni.freiburg.iig.telematik.jawl.log.LogSummary;
import de.uni.freiburg.iig.telematik.jawl.log.LogTrace;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParser;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowParser.whichTimestamp;
/**
 * This class implements the interface LogFileReader. It is used
 * to retrieve the necessary information from a logfile.
 * @author bernhard
 *
 */
public class LogFileInformation implements LogFileReader {

	private LogFileViewer logViewer;
	private List<String> activities, subjects, roles;
	public LogFileInformation(LogFileViewer logViewer) {
		this.logViewer = logViewer;
		activities=new ArrayList<String>();
		subjects=new ArrayList<String>();
		roles=new ArrayList<String>();
		update();
	}

	@Override
	public String[] getActivities() {
		// TODO Auto-generated method stub
		return activities.toArray(new String[activities.size()]);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		activities.clear();
		subjects.clear();
		File logFile=logViewer.getFile();
		try {
			List<List<LogTrace<LogEntry>>> logEntries=LogParser.parse(logFile);
			LogSummary<LogEntry> summary=new LogSummary(logEntries);
			activities.addAll(summary.getActivities());
			subjects.addAll(summary.getOriginators());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			//Try AristFlowParser
			try {
				AristaFlowParser aFlow = new AristaFlowParser(logFile);
				aFlow.parse(whichTimestamp.BOTH);
				List<List<LogTrace<LogEntry>>> logEntries = aFlow.getLogEntries();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			//e.printStackTrace();
		}
	}


	@Override
	public String[] getSubjects() {
		// TODO Auto-generated method stub
		return subjects.toArray(new String[subjects.size()]);
	}

	@Override
	public String[] getRoles() {
		// TODO Auto-generated method stub
		return roles.toArray(new String[roles.size()]);
	}

}
