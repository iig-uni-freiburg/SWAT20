package de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.sciff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.JDOMException;
import org.processmining.analysis.sciffchecker.logic.SCIFFChecker;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;
import org.processmining.analysis.sciffchecker.logic.util.TimeGranularity;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ResourcePattern;

public class SCIFFExecutor {
	
	private ISciffLogReader mLogReader;
	
	public SCIFFExecutor(ISciffLogReader logReader) {
		mLogReader = logReader;
	}
	
	
	public ArrayList<SCIFFResult> analyze(ArrayList<ResourcePattern> patterns) {
		
		ArrayList<SCIFFResult> results = new ArrayList<SCIFFResult>();
		
		for (ResourcePattern rp : patterns) {
			
			List<CompositeRule> rules = rp.getRules();
			SCIFFResult sr = new SCIFFResult();
			
			for (CompositeRule rule : rules) {
				try{
					SCIFFChecker checker = new SCIFFChecker();
					CheckerReport report = checker.analyse(mLogReader, rule, TimeGranularity.MILLISECONDS);
					sr.addReport(report);
				} catch (JDOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			results.add(sr);
		}
		return results;
	}

}
