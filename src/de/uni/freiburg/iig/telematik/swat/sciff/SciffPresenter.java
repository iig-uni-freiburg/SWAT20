package de.uni.freiburg.iig.telematik.swat.sciff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;

public class SciffPresenter {

	protected JFrame result;
	protected String output;
	protected CheckerReport report;

	public SciffPresenter(String output) {
		//this.output = "<html><body>" + output.replaceAll("(\r\n|\n)", "<br />" + "</body></html>");
		this.output = output;
		makeWindow();
	}

	public SciffPresenter(CheckerReport report) {
		this.report = report;
		StringBuilder b = new StringBuilder();
		b.append(getReportOverview(report));
		b.append("\r\n \r\n");
		b.append(getWrongDetails(report));
		b.append("\r\n \r\n");
		b.append(getCorrectDetails(report));
		this.output = b.toString();
		makeWindow();
	}


	public StringBuilder getReportOverview(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("Report:\r\n");
		b.append("Number of correct instances: ");
		b.append(report.correctInstances().size());
		b.append("\r\n Number of wrong instances: ");
		b.append(report.wrongInstances().size());
		return b;
	}

	private StringBuilder getWrongDetails(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("Wrong Instances: \r\n");
		for (int i : report.wrongInstances()) {
			b.append(report.getLog().getInstances().get(i).getName());
			b.append("\r\n");
		}
		return b;
	}

	private StringBuilder getCorrectDetails(CheckerReport report) {
		StringBuilder b = new StringBuilder();
		b.append("Corect Instances: \r\n");
		for (int i : report.correctInstances()) {
			b.append(report.getLog().getInstances().get(i).getName());
			b.append("\r\n");
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
		JTextArea area = new JTextArea(output);
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
		correct.addActionListener(new SciffAnalyzeAction(new LogBuilder(report.getLog(), report.correctInstances())));
		report.correctInstances();
		pane.add(correct);
		JButton wrong = new JButton("Analyse " + report.wrongInstances().size() + " WRONG Instances");
		wrong.addActionListener(new SciffAnalyzeAction(new LogBuilder(report.getLog(), report.wrongInstances())));
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
