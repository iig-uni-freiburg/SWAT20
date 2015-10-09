package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.execution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.JDOMException;
import org.processmining.analysis.sciffchecker.logic.SCIFFChecker;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;
import org.processmining.analysis.sciffchecker.logic.util.TimeGranularity;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.ModelChecker;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.ModelCheckerResult;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLogType;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SCIFF extends ModelChecker {

	private ISciffLogReader mLogReader;

	public SCIFF(File xesLogFile) {

		if (AristaFlowParser.canParse(xesLogFile)) {
			try {
				AristaFlowParser parser = new AristaFlowParser(xesLogFile);
				parser.parse(whichTimestamp.BOTH);
				mLogReader = parser;
			} catch (FileNotFoundException ex) {
				Logger.getLogger(SCIFF.class.getName()).log(Level.SEVERE, null, ex);
			} catch (Exception ex) {
				Logger.getLogger(SCIFF.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else if (xesLogFile.toString().endsWith("mxml")) {
			MXMLLogParser parser = new MXMLLogParser();
			try {
				parser.parse(xesLogFile, ParsingMode.COMPLETE);
				mLogReader = new LogParserAdapter(parser);
			} catch (ParameterException | ParserException e) {
				Workbench.errorMessage("Could not parse and analyze " + xesLogFile.getName(), e, true);
			}
		}

		else if (new XESLogParser().canParse(xesLogFile)) {
			XESLogParser parser = new XESLogParser();
			try {
				parser.parse(xesLogFile, ParsingMode.COMPLETE);
				mLogReader = new LogParserAdapter(parser);
			} catch (ParameterException e) {
				Workbench.errorMessage("Could not parse and analyze " + xesLogFile.getName(), e, true);
			} catch (ParserException e) {
				Workbench.errorMessage("Could not parse and analyze " + xesLogFile.getName(), e, true);
			}
		}
	}

	public SCIFF(LogFileViewer logFileViewer) {
		try {
			mLogReader = logFileViewer.getModel().getLogReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(ArrayList<CompliancePattern> patterns) throws Exception {

		CheckerReport report = null;

		for (CompliancePattern pattern : patterns) {
			if (!pattern.isInstantiated())
				continue;
			List<CompositeRule> rules = (List<CompositeRule>) pattern.getFormalization();
			for (CompositeRule rule : rules) {
				try {
					SCIFFChecker checker = new SCIFFChecker();
					report = checker.analyse(mLogReader, rule, TimeGranularity.MILLISECONDS);
					double prob = report.correctInstances().size()
							/ ((double) (report.correctInstances().size() + report.wrongInstances().size()));
					pattern.setProbability(prob);
					if (prob == 1.0) {
						pattern.setSatisfied(true);
					}
				} catch (JDOMException e) {
					e.printStackTrace();
					throw new Exception(e);
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e);
				}
			}
		}
		if (onlyOneInstantiatedPattern(patterns))
			ModelCheckerResult.setResult(report);
	}

	private boolean onlyOneInstantiatedPattern(ArrayList<CompliancePattern> patterns) {
		int i = 0;
		for (CompliancePattern p : patterns) {
			if (p.isInstantiated()) {
				i++;
			}
		}
		return i == 1;
	}

}
