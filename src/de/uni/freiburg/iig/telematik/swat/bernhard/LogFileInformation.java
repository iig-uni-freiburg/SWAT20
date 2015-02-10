package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.io.File;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jawl.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.jawl.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.jawl.log.LogEntry;
import de.uni.freiburg.iig.telematik.jawl.log.LogSummary;
import de.uni.freiburg.iig.telematik.jawl.log.LogTrace;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParser;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

/**
 * This class implements the interface LogFileReader. It is used
 * to retrieve the necessary information from a logfile.
 * @author bernhard
 *
 */
public class LogFileInformation implements AnalysisComponentInfoProvider {

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
		return activities.toArray(new String[activities.size()]);
	}

	@Override
	public void update() {
		activities.clear();
		subjects.clear();
		File logFile=logViewer.getFile();
		XESLogParser parser = new XESLogParser();
		try {
			List<List<LogTrace<LogEntry>>> logEntries=LogParser.parse(logFile);
			LogSummary<LogEntry> summary=new LogSummary(logEntries);
			activities.addAll(summary.getActivities());
			subjects.addAll(summary.getOriginators());
		} catch (IOException e) {
			Workbench.errorMessageWithNotification("could not access analysis panel file");
			e.printStackTrace();
		} catch (ParserException e) {
			//Try AristFlowParser
			try {
				AristaFlowParser aFlow = new AristaFlowParser(logFile);
				aFlow.parse(whichTimestamp.BOTH);
				//List<List<LogTrace<LogEntry>>> logEntries = aFlow.getLogEntries(); //this fails
				// LogSummary<LogEntry> summary = new LogSummary(logEntries); //this fails because of above fail
				activities.addAll(aFlow.getActivityCandidates()); //this works
				//activities.addAll(summary.getActivities());
				subjects.addAll(aFlow.getOriginatorCandidates()); //and this works
				//subjects.addAll(summary.getOriginators());
			} catch (FileNotFoundException e1) {
				Workbench.errorMessageWithNotification("could not access analysis panel file");
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
		return subjects.toArray(new String[subjects.size()]);
	}

	@Override
	public String[] getRoles() {
		return roles.toArray(new String[roles.size()]);
	}

}
