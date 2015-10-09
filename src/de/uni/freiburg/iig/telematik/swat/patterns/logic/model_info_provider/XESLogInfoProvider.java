package de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

public class XESLogInfoProvider implements ModelInfoProvider {
	
	private ArrayList<String> mSubjects;
	private ArrayList<String> mActivities;
	private ArrayList<String> mRoles;
	
	public XESLogInfoProvider (LogFileViewer viewer){
		
		switch (viewer.getModel().getType()) {
		case Aristaflow:
			try {
				AristaFlowParser parser = (AristaFlowParser) viewer.getModel().getLogReader();
				mActivities = new ArrayList<String>(parser.getActivityCandidates());
				mSubjects = new ArrayList<String>(parser.getOriginatorCandidates());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case MXML:
		case XES:
			try {
				ISciffLogSummary summary = viewer.getModel().getLogReader().getSummary();
				mActivities = new ArrayList<String>(Arrays.asList(summary.getModelElements()));
				mSubjects = new ArrayList<String>(Arrays.asList(summary.getOriginators()));
				mRoles = new ArrayList<String>(Arrays.asList(summary.getRoles()));
			} catch (Exception e) {
				e.printStackTrace();
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
			mActivities = new ArrayList<String>(Arrays.asList(summary.getModelElements()));
			mSubjects = new ArrayList<String>(Arrays.asList(summary.getOriginators()));
			mRoles = new ArrayList<String>(Arrays.asList(summary.getRoles()));
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
	}

	private void parseAsMxml(File fileReference) {
		MXMLLogParser mxmlParser = new MXMLLogParser();
		try {
			mxmlParser.parse(fileReference, ParsingMode.COMPLETE);
			LogParserAdapter adapter = new LogParserAdapter(mxmlParser);
			ISciffLogSummary summary = adapter.getSummary();
			mActivities = new ArrayList<String>(Arrays.asList(summary.getModelElements()));
			mSubjects = new ArrayList<String>(Arrays.asList(summary.getOriginators()));
			mRoles = new ArrayList<String>(Arrays.asList(summary.getRoles()));
		} catch (ParameterException | ParserException e) {
			e.printStackTrace();
		}
		
	}

	private void parseAsAFLog(File fileReference) {
		try {
			AristaFlowParser parser = new AristaFlowParser(fileReference);
			parser.parse(whichTimestamp.BOTH);
			mActivities = new ArrayList<String>(parser.getActivityCandidates());
			mSubjects = new ArrayList<String>(parser.getOriginatorCandidates());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			return new AristaFlowParser(xesLogFile).canParse(xesLogFile);
		} catch (FileNotFoundException e) {
			return false;
		}
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
