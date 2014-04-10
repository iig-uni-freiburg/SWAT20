package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;

import org.jdom.JDOMException;
import org.processmining.analysis.sciffchecker.logic.SCIFFChecker;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;
import org.processmining.analysis.sciffchecker.logic.util.TimeGranularity;

import adapter.LogParserAdapter;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParser;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParserInterface;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParsingFormat;
import de.uni.freiburg.iig.telematik.swat.sciff.AristFlowParser;
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

				CompositeRule rule = org.processmining.analysis.sciffchecker.gui.SciffRuleDialog.showRuleDialog(null);
				System.out.println("Total Entries in Log: " + reader.getInstances().size());
				SCIFFChecker checker = new SCIFFChecker();
				CheckerReport report = checker.analyse(reader, rule, TimeGranularity.MILLISECONDS);
				System.out.println("Wrong: " + report.wrongInstances().size() + " - Right: " + report.correctInstances().size()
						+ " - Exceptions: " + report.exceptionInstances().size());
				SciffPresenter sciff = new SciffPresenter(getReportDetails(report));
				sciff.show();
				//System.out.println("Wrong: " + reader.getInstance(report.wrongInstances().get(0)).getName());
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO: implement system call to sciff
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ISciffLogReader getReader() throws ParserException, ParameterException, IOException {
		if (file.getName().endsWith("mxml"))
			return createMxmlParser();
		return createAristaFlowParser();
	}

	private ISciffLogReader createAristaFlowParser() throws IOException {
		AristFlowParser parser = new AristFlowParser(file);
		parser.parse();
		return parser;
	}

	private ISciffLogReader createMxmlParser() throws ParserException, ParameterException, IOException {
		LogParserInterface parser = LogParser.getParser(file, LogParsingFormat.XES);
		parser.parse(file, true);
		return new LogParserAdapter(parser);

	}

	public String getReportDetails(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("Report:\r\n");
		b.append("Number of correct instances: ");
		b.append(report.correctInstances().size());
		b.append("\r\n Number of wrong instances: ");
		b.append(report.wrongInstances().size());
		return b.toString();
	}

}
