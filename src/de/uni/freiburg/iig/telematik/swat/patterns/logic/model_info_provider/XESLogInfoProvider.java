package de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogViewViewer;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;
import java.io.IOException;
import java.text.ParseException;

public class XESLogInfoProvider implements ModelInfoProvider {
	
	private ArrayList<String> mSubjects;
	private ArrayList<String> mActivities;
	private ArrayList<String> mRoles;
	
	public XESLogInfoProvider (LogFileViewer viewer){
		
		switch (viewer.getModel().getType()) {
		case Aristaflow:
			try {
				AristaFlowParser parser = (AristaFlowParser) viewer.loadLogReader();
				mActivities = new ArrayList<>(parser.getActivityCandidates());
				mSubjects = new ArrayList<>(parser.getOriginatorCandidates());
			} catch (ParseException | IOException | ParameterException | ParserException e) {
				throw new RuntimeException(e);
			}
			break;
			
		case MXML:
		case XES:
			try {
				ISciffLogSummary summary = viewer.loadLogReader().getSummary();
				mActivities = new ArrayList<>(Arrays.asList(summary.getModelElements()));
				mSubjects = new ArrayList<>(Arrays.asList(summary.getOriginators()));
				mRoles = new ArrayList<>(Arrays.asList(summary.getRoles()));
			} catch (ParseException | IOException | ParameterException | ParserException e) {
				throw new RuntimeException(e);
			}
		default:
			break;
		}
	}
	
	public XESLogInfoProvider (LogViewViewer viewer){
		
		switch (viewer.getModel().getType()) {
		case Aristaflow:
			try {
				AristaFlowParser parser = (AristaFlowParser) viewer.loadLogReader();
				mActivities = new ArrayList<>(parser.getActivityCandidates());
				mSubjects = new ArrayList<>(parser.getOriginatorCandidates());
			} catch (ParseException | IOException | ParameterException | ParserException e) {
				throw new RuntimeException(e);
			}
			break;
			
		case MXML:
		case XES:
			try {
				ISciffLogSummary summary = viewer.loadLogReader().getSummary();
				mActivities = new ArrayList<>(Arrays.asList(summary.getModelElements()));
				mSubjects = new ArrayList<>(Arrays.asList(summary.getOriginators()));
				mRoles = new ArrayList<>(Arrays.asList(summary.getRoles()));
			} catch (ParseException | IOException | ParameterException | ParserException e) {
				throw new RuntimeException(e);
			}
		default:
			break;
		}
	}

	private void parseAsXes(File fileReference) {
		XESLogParser parser = new XESLogParser();
		try {
			parser.parse(fileReference, ParsingMode.COMPLETE);
			LogParserAdapter adapter = new LogParserAdapter(parser);
			ISciffLogSummary summary = adapter.getSummary();
			mActivities = new ArrayList<>(Arrays.asList(summary.getModelElements()));
			mSubjects = new ArrayList<>(Arrays.asList(summary.getOriginators()));
			mRoles = new ArrayList<>(Arrays.asList(summary.getRoles()));
		} catch (ParameterException | ParserException e) {
			throw new RuntimeException(e);
		}
		
	}

	private void parseAsMxml(File fileReference) {
		MXMLLogParser mxmlParser = new MXMLLogParser();
		try {
			mxmlParser.parse(fileReference, ParsingMode.COMPLETE);
			LogParserAdapter adapter = new LogParserAdapter(mxmlParser);
			ISciffLogSummary summary = adapter.getSummary();
			mActivities = new ArrayList<>(Arrays.asList(summary.getModelElements()));
			mSubjects = new ArrayList<>(Arrays.asList(summary.getOriginators()));
			mRoles = new ArrayList<>(Arrays.asList(summary.getRoles()));
		} catch (ParameterException | ParserException e) {
			throw new RuntimeException(e);
		}
		
	}

	private void parseAsAFLog(File fileReference) {
		try {
			AristaFlowParser parser = new AristaFlowParser(fileReference);
			parser.parse(whichTimestamp.BOTH);
			mActivities = new ArrayList<>(parser.getActivityCandidates());
			mSubjects = new ArrayList<>(parser.getOriginatorCandidates());
		} catch (ParseException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public XESLogInfoProvider(File xesLogFile) {
            boolean isXESLog = false;
                isXESLog=isXESLogFile(xesLogFile);
		if (isXESLog) {
			parseAsXes(xesLogFile);

		} else if (isAFlog(xesLogFile)) {
			parseAsAFLog(xesLogFile);
		}
		else if(isMXMLlog(xesLogFile)){
			parseAsMxml(xesLogFile);	
		}
	}
	
	private boolean isMXMLlog(File xesLogFile) {
		return xesLogFile.toString().endsWith("mxml");
	}

	private boolean isAFlog(File xesLogFile) {
		return AristaFlowParser.canParse(xesLogFile);
	}

	public ArrayList<String> getSubjects() {
		return mSubjects;
	}
	
	public ArrayList<String> getRoles() {
		return mRoles;
		
	}
	
	public ArrayList<String> getActivities() {
		return mActivities;
		
	}
	
	private boolean isXESLogFile(File log) {
		return log.toString().endsWith("xes");
	}

}
