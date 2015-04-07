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

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.OrganizationalPattern;

/**
 * The SCIFFAdapter is used to provide SCIFF with the necessary input 
 * (a log file and compliance rules) and to capture the output of the audit time 
 * verification with SCIFF.
 *
 * @author Lukas Sättler
 * @version 1.0
 */

public class SCIFFAdapter {
	
	private ISciffLogReader mLogReader;
	
	/**
	 * Constructs a SCIFFAdapter
	 *
	 * @param logReader a reader which is used to parse the log
	 */
	public SCIFFAdapter(ISciffLogReader logReader) {
		mLogReader = logReader;
	}
	
	
	/**
	 * Initiates the verification of a process log with SCIFF. The method returns the result of the 
	 * audit time verification with SCIFF.
	 *
	 * @param logReader a reader which is used to parse the log
	 * @return the result of the verification
	 */
	public ArrayList<SCIFFResult> analyze(ArrayList<CompliancePattern> patterns) {
		
		ArrayList<SCIFFResult> results = new ArrayList<SCIFFResult>();
		
		for (CompliancePattern rp : patterns) {
			
			List<CompositeRule> rules = ((OrganizationalPattern) rp).getRules();
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
