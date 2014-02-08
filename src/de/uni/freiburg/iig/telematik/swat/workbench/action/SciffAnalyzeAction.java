package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;

import org.jdom.JDOMException;
import org.processmining.analysis.sciffchecker.logic.SCIFFChecker;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;
import org.processmining.analysis.sciffchecker.logic.util.TimeGranularity;

import adapter.LogParserAdapter;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jawl.parser.LogParser;
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
				LogParser parser = new LogParser();
				parser.parse(file);
				LogParserAdapter adapter = new LogParserAdapter(parser);
				CompositeRule rule = org.processmining.analysis.sciffchecker.gui.SciffRuleDialog.showRuleDialog(null);
				System.out.println("Total Entries in Log: " + adapter.getInstances().size());
				SCIFFChecker checker = new SCIFFChecker();
				CheckerReport report = checker.analyse(adapter, rule, TimeGranularity.MILLISECONDS);
				System.out.println("Wrong: " + report.wrongInstances().size() + " - Right: " + report.correctInstances().size()
						+ " - Exceptions: " + report.exceptionInstances().size());
				SciffPresenter sciff = new SciffPresenter(getReportDetails(report));
				System.out.println("Showing SCIFF result with " + getReportDetails(report));
				sciff.show();
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

	public String getReportDetails(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("Report:\r\n");
		b.append("Number of correct entries: ");
		b.append(report.correctInstances().size());
		b.append("\r\n Number of wrong instances: ");
		b.append(report.wrongInstances().size());
		return b.toString();
	}

}
