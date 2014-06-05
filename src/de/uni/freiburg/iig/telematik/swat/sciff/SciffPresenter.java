package de.uni.freiburg.iig.telematik.swat.sciff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import org.processmining.analysis.sciffchecker.gui.html.HTMLRuleVisitor;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.properties.ResourceHandler;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;

public class SciffPresenter {

	protected JFrame result;
	protected String output;
	protected CheckerReport report;
	protected CompositeRule rule;

	public SciffPresenter(String output) {
		//this.output = "<html><body>" + output.replaceAll("(\r\n|\n)", "<br />" + "</body></html>");
		this.output = output;
		makeWindow();
	}

	public SciffPresenter(CheckerReport report) {
		this(report, null);

	}

	public SciffPresenter(CheckerReport report, CompositeRule rule) {
		this.report = report;
		this.rule = rule;
		StringBuilder b = new StringBuilder();
		b.append(generateRuleOverview() + "<br>");
		b.append(getReportOverview(report));
		b.append("<br> <br>");
		b.append(getWrongDetails(report));
		b.append("<br> <br>");
		b.append(getCorrectDetails(report));
		b.append("</body></html>");
		this.output = b.toString();
		makeWindow();
	}

	public String generateRuleOverview() {
		if (rule == null)
			return "";
		URL css = ResourceHandler.getInstance().getURL(ResourceHandler.CSS);
		HTMLRuleVisitor visitor = new HTMLRuleVisitor(css);
		System.out.println("Rule: " + visitor.visitRule(rule));
		return "RULE: " + visitor.visitRule(rule).replace("</body>", "").replace("</html>", "");
	}

	public CheckerReport getReport() {
		return report;
	}

	public StringBuilder getReportOverview(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("<b><font size=Report:</b><br>");
		b.append("Number of correct instances: ");
		b.append(report.correctInstances().size());
		b.append("<br><b>Number of wrong instances: </b>");
		b.append(report.wrongInstances().size());
		return b;
	}

	private StringBuilder getWrongDetails(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("Wrong Instances: <br>");
		for (int i : report.wrongInstances()) {
			b.append(report.getLog().getInstances().get(i).getName());
			b.append("<br>");
		}
		return b;
	}

	private StringBuilder getCorrectDetails(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("Correct Instances: <br>");
		for (int i : report.correctInstances()) {
			b.append(report.getLog().getInstances().get(i).getName());
			b.append("<br>");
		}
		return b;
	}

	public void show() {
		result.setLocationByPlatform(true);
		result.setVisible(true);
	}

	private void makeWindow() {
		result = new JFrame();
		//JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		//result.setLayout(new GridLayout(2, 1));
		//result.add(new Spl)
		result.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		result.setSize(new Dimension(600, 600));
		ScrollPane scrollPane = new ScrollPane();
		JTextPane area = new JTextPane();
		area.setContentType("text/html");
		area.setText(output);
		area.setEditable(false);
		scrollPane.add(area);
		result.add(scrollPane, BorderLayout.CENTER);
		result.add(getBottomButtons(), BorderLayout.PAGE_END);
		//result.addWindowListener(new CloseListener());
		//split.add(getBottomButtons());
		//split.setDividerLocation(0.05);
		//result.add(split);
	}

	private JPanel getBottomButtons() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 2));
		JButton correct = new JButton("Analyse " + report.correctInstances().size() + " CORRECT Instances");
		//correct.addActionListener(new SciffAnalyzeAction(report.getLog().getInstances(report.correctInstances())));
		//correct.addActionListener(new SciffAnalyzeAction(new LogBuilder(report.getLog(), report.correctInstances())));
		correct.addActionListener(new SciffAnalyzeAction(new LogFilePartitioner(report.getLog(), report.correctInstances())));
		report.correctInstances();
		pane.add(correct);
		JButton wrong = new JButton("Analyse " + report.wrongInstances().size() + " WRONG Instances");
		// wrong.addActionListener(new SciffAnalyzeAction(new LogBuilder(report.getLog(), report.wrongInstances())));
		wrong.addActionListener(new SciffAnalyzeAction(new LogFilePartitioner(report.getLog(), report.wrongInstances())));
		pane.add(wrong);
		//pane.pack();
		return pane;
	}

}

class LogBuilder implements ISciffLogReader {
	ArrayList<ISciffLogTrace> traces = new ArrayList<ISciffLogTrace>();

	public LogBuilder(List<ISciffLogTrace> listOfTraces) {
		addTraces(listOfTraces);
	}

	public LogBuilder(ISciffLogReader log, List<Integer> tracesWithinLog) {
		for (int i : tracesWithinLog) {
			addTrace(log.getInstance(i));
		}
	}

	@Override
	public List<ISciffLogTrace> getInstances() {
		// TODO Auto-generated method stub
		return traces;
	}

	@Override
	public ISciffLogTrace getInstance(int index) {
		// TODO Auto-generated method stub
		return traces.get(index);
	}

	@Override
	public int traceCount() {
		// TODO Auto-generated method stub
		return traces.size();
	}

	@Override
	public ISciffLogSummary getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addTrace(ISciffLogTrace trace) {
		traces.add(trace);
	}

	public void addTraces(List<ISciffLogTrace> listOfTraces) {
		for (ISciffLogTrace trace : listOfTraces) {
			addTrace(trace);
		}
	}

}
