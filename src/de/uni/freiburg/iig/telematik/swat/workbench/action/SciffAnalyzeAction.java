package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.AbstractAction;

import org.jdom.JDOMException;
import org.processmining.analysis.sciffchecker.gui.SciffRuleDialog;
import org.processmining.analysis.sciffchecker.logic.SCIFFChecker;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogEntry;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;
import org.processmining.analysis.sciffchecker.logic.util.TimeGranularity;

import adapter.LogParserAdapter;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParser;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParserInterface;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParsingFormat;
import de.uni.freiburg.iig.telematik.jawl.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.sciff.SciffPresenter;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;

public class SciffAnalyzeAction extends AbstractAction {

	private static final long serialVersionUID = 9111775745565090191L;
	private File file;

	public SciffAnalyzeAction(File file) {
		this.file = file;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			SwatState.getInstance().setOperatingMode(this, OperatingMode.ANALYSIS_MODE);
			System.out.println("Analayze " + file.getCanonicalPath());

			try {
				
				ISciffLogReader reader = getReader();

				//CompositeRule rule = org.processmining.analysis.sciffchecker.gui.SciffRuleDialog.showRuleDialog(null);
				CompositeRule rule;
				try {
					rule = SciffRuleDialog.showRuleDialog(null, null, getActivityCandidates(reader), getOriginatorCandidates(reader));
				} catch (Exception e) {
					rule = SciffRuleDialog.showRuleDialog(null);
				}

				System.out.println("Total Entries in Log: " + reader.getInstances().size());
				SCIFFChecker checker = new SCIFFChecker();
				CheckerReport report = checker.analyse(reader, rule, TimeGranularity.MILLISECONDS);
				System.out.println("Wrong: " + report.wrongInstances().size() + " - Right: " + report.correctInstances().size()
						+ " - Exceptions: " + report.exceptionInstances().size());
				SciffPresenter sciff = new SciffPresenter(report);
				sciff.show();
				//System.out.println("Wrong: " + reader.getInstance(report.wrongInstances().get(0)).getName());
			} catch (ParserException e) {
				e.printStackTrace();
			} catch (JDOMException e) {
				e.printStackTrace();
			}
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Collection<String> getOriginatorCandidates(ISciffLogReader reader) {
		HashSet<String> result = new HashSet<String>();
		ISciffLogEntry entry;

		for (ISciffLogTrace trace : reader.getInstances()) {
			Iterator<ISciffLogEntry> iter = trace.iterator();
			while (iter.hasNext()) {
				entry = iter.next();
				result.add(entry.getOriginator());
			}
		}
		return Collections.unmodifiableSet(result);
	}

	private Collection<String> getActivityCandidates(ISciffLogReader reader) {
		
		HashSet<String> result = new HashSet<String>();
		ISciffLogEntry entry;
		
		for (ISciffLogTrace trace:reader.getInstances()){
			Iterator<ISciffLogEntry> iter = trace.iterator();
			while (iter.hasNext()){
				entry=iter.next();
				result.add(entry.getElement());
			}
		}
		return Collections.unmodifiableSet(result);
	}

	private ISciffLogReader getReader() throws ParserException, ParameterException, IOException {
		if (file.getName().endsWith("mxml"))
			return createMxmlParser();
		return createAristaFlowParser();
	}

	private ISciffLogReader createAristaFlowParser() throws IOException {
		AristaFlowParser parser = new AristaFlowParser(file);
		parser.parse(whichTimestamp.BOTH);
		return parser;
	}

	private ISciffLogReader createMxmlParser() throws ParserException, ParameterException, IOException {
		LogParserInterface parser = LogParser.getParser(file, LogParsingFormat.XES);
		parser.parse(file, ParsingMode.DISTINCT_TRACES);
		return new LogParserAdapter(parser);

	}



}
