package de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.sciff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.JDOMException;
import org.processmining.analysis.sciffchecker.logic.SCIFFChecker;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;
import org.processmining.analysis.sciffchecker.logic.util.TimeGranularity;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.ModelChecker;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

public class SCIFF extends ModelChecker {
	
	private LogParserAdapter mLogReader;

	public SCIFF(File xesLogFile) {
		XESLogParser parser = new XESLogParser();	
		try {
			parser.parse(xesLogFile, ParsingMode.COMPLETE);
			mLogReader = new LogParserAdapter(parser);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run(ArrayList<CompliancePattern> patterns) {
		
		for (CompliancePattern pattern : patterns) {
			if (!pattern.isInstantiated()) continue;
			List<CompositeRule> rules = (List<CompositeRule>) pattern.getFormalization();
			for (CompositeRule rule : rules) {
				try{
					SCIFFChecker checker = new SCIFFChecker();
					CheckerReport report = checker.analyse(mLogReader, rule, TimeGranularity.MILLISECONDS);
					double prob = report.correctInstances().size() / (
							(double) (report.correctInstances().size() 
									+ report.wrongInstances().size()));
					pattern.setProbability(prob);
					if (prob == 1.0) {
						pattern.setSatisfied(true);
					}
				} catch (JDOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
