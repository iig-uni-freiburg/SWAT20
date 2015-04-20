package de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

public class XESLogInfoProvider implements ModelInfoProvider {
	
	private ArrayList<String> mSubjects;
	private ArrayList<String> mActivities;
	private ArrayList<String> mRoles;

	public XESLogInfoProvider(File xesLogFile) {
		if (isXESLogFile(xesLogFile)) {
		XESLogParser parser = new XESLogParser();
		try {
			parser.parse(xesLogFile, ParsingMode.COMPLETE);
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
		} else if (isAFlog(xesLogFile)) {
			try {
				AristaFlowParser parser = new AristaFlowParser(xesLogFile);
				parser.parse(whichTimestamp.BOTH);
				mActivities = new ArrayList<String>(parser.getActivityCandidates());
				mSubjects = new ArrayList<String>(parser.getOriginatorCandidates());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	private boolean isAFlog(File xesLogFile) {
		try {
			AristaFlowParser parser = new AristaFlowParser(xesLogFile);
			parser.parse(whichTimestamp.BOTH);
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (Exception e) {
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
		XESLogParser parser = new XESLogParser();
		if (parser.canParse(log)) {
			try {
				parser.parse(log, ParsingMode.COMPLETE);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

}
