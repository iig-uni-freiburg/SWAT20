package de.uni.freiburg.iig.telematik.swat.sciff;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

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
		result.add(scrollPane);
		JTextArea area = new JTextArea(output);
		area.setEditable(false);
		scrollPane.add(area);
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
		pane.add(correct);
		JButton wrong = new JButton("Analyse " + report.wrongInstances().size() + " WRONG Instances");
		pane.add(wrong);
		//pane.pack();
		return pane;
	}

	/** TODO: get new LogReader with specific entries **/
	private ISciffLogReader extractLog(List<Integer> instances) {
		report.getLog().getInstances();

		return null;

	}

	private class CloseListener extends WindowAdapter {
		@Override
		public void windowDeactivated(WindowEvent arg0) {
			result.dispose();
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			result.dispose();
		}
	}
}
