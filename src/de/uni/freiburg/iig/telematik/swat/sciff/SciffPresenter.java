package de.uni.freiburg.iig.telematik.swat.sciff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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

public class SciffPresenter implements Serializable {

	private static final long serialVersionUID = 1L;
	protected JFrame result;
	protected String output;
	protected CheckerReport report;

	protected CompositeRule rule;
	protected CompositeRule oldRule;
	

	private String operatorString = "<b>CURRENT RULE: </b><br />";
	private String oldRuleString;

	public SciffPresenter(CheckerReport report, CompositeRule rule, String oldRuleString) {
		this.report = report;
		this.rule = rule;
		this.oldRule = oldRule;
		this.oldRuleString=oldRuleString;
		StringBuilder b = new StringBuilder();

		b.append(currentHeaderDateAndTime());

		b.append("<b>RULE</b> ");
		b.append(getRuleString() + "<br>");

		b.append(getReportOverview(report));
		b.append("<br> <br>");

		b.append(getWrongDetails(report));
		b.append("<br> <br>");

		b.append(getCorrectDetails(report));
		b.append("</body></html>");
		this.output = b.toString();

		makeWindow();
	}


	private StringBuilder currentHeaderDateAndTime() {


		StringBuilder b = new StringBuilder();

		b.append("Analysis from ");
		b.append(System.getProperty("user.name"));
		b.append(", on ");
		b.append(getHostname());
		b.append(" (");
		b.append(new Date());
		b.append(") <br>");
		return b;
	}

	private String getHostname() {
		String localMachine;
		try {
			localMachine = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			try {
				localMachine = java.net.Inet4Address.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				localMachine = "unknown computer";
			}
		}
		return localMachine;
	}

	public SciffPresenter(CheckerReport report, CompositeRule rule) {
		this(report, rule, null);
	}

	public SciffPresenter(String output) {
		//this.output = "<html><body>" + output.replaceAll("(\r\n|\n)", "<br />" + "</body></html>");
		this.output = output;
		makeWindow();
	}

	public SciffPresenter(CheckerReport report) {
		this(report, null);
	}

	public String getRuleString() {
		if (rule == null)
			return "";

		URL css = ResourceHandler.getInstance().getURL(ResourceHandler.CSS);
		HTMLRuleVisitor visitor = new HTMLRuleVisitor(css);
		//return "RULE: " + visitor.visitRule(rule).replace("</body>", "").replace("</html>", "");
		return oldRuleString + "<br /><blockquote>" + operatorString + visitor.visitRule(rule, false) + "</blockquote>";
	}
	
	public CompositeRule getRule() {
		return this.rule;
	}

	public CheckerReport getReport() {
		return report;
	}

	public StringBuilder getReportOverview(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("<b><font size=Report:</b><br>");
		b.append("Number of <b>correct</b> instances: ");
		b.append(report.correctInstances().size());
		b.append("<br>Number of <b>wrong</b> instances: ");
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
	}

	private JPanel getBottomButtons() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 2));

		JButton correct = new JButton("Analyse CORRECT Instances (" + report.correctInstances().size() + ")");
		//correct.addActionListener(new SciffAnalyzeAction(new LogFilePartitioner(report.getLog(), report.correctInstances()), this));
		correct.addActionListener(new FurtherAnalyzeAction(true, this));
		//report.correctInstances();
		pane.add(correct);

		JButton wrong = new JButton("Analyse WRONG Instances (" + report.wrongInstances().size() + ")");
		//wrong.addActionListener(new SciffAnalyzeAction(new LogFilePartitioner(report.getLog(), report.wrongInstances()), this));
		wrong.addActionListener(new FurtherAnalyzeAction(false, this));
		pane.add(wrong);
		return pane;
	}

	private JButton getSaveButton() {
		JButton save = new JButton("Save");

		return null;
	}

	public void setOperatorString(String operator) {
		this.operatorString = operator;
	}
}
	/**
	 * action listener to filter results. boolean is true if positive results
	 * are filtered.
	 **/
	class FurtherAnalyzeAction implements ActionListener {
		private boolean positiveSet;
		private SciffPresenter presenter;

		public FurtherAnalyzeAction(boolean positiveSet, SciffPresenter presenter) {
		this.positiveSet = positiveSet;
		this.presenter = presenter;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//generate LogFile according to pressed button
			List<Integer> reportSet = new LinkedList<Integer>();
			
			//change operator String of this SciffPresenter depending on which button was pressed
			if (positiveSet) {
				presenter.setOperatorString("<b>MUST MATCH: </b><br />");
				reportSet = presenter.getReport().correctInstances();
				System.out.println("MUST MATCH");
			} else if (!positiveSet) {
				presenter.setOperatorString("<i><b>MUST NOT MATCH: </b></i><br />");
				reportSet = presenter.getReport().wrongInstances();
			}
		LogFilePartitioner partitioner = new LogFilePartitioner(presenter.getReport().getLog(), reportSet);

			SciffAnalyzeAction sciffAnalzeAction = new SciffAnalyzeAction(partitioner, this.presenter);
			sciffAnalzeAction.actionPerformed(e);

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