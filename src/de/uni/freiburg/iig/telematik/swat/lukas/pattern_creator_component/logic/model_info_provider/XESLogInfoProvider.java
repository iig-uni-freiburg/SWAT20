package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

public class XESLogInfoProvider implements ModelInfoProvider {
	
	private ArrayList<String> mSubjects;
	private ArrayList<String> mActivities;
	private ArrayList<String> mRoles;

	public XESLogInfoProvider(File xesLogFile) {
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
	

}
