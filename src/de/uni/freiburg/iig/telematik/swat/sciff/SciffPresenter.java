package de.uni.freiburg.iig.telematik.swat.sciff;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

public class SciffPresenter {

	protected JFrame result;
	protected String output;

	public SciffPresenter(String output) {
		//this.output = "<html><body>" + output.replaceAll("(\r\n|\n)", "<br />" + "</body></html>");
		this.output = output;
		makeWindow();
	}

	public SciffPresenter(CheckerReport report) {
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
		result.setSize(new Dimension(600, 600));
		ScrollPane scrollPane = new ScrollPane();
		result.add(scrollPane);
		JTextArea area = new JTextArea(output);
		scrollPane.add(area);
		result.addWindowListener(new CloseListener());
	}

	private class CloseListener implements WindowListener {
		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			result.dispose();
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			result.dispose();
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
}
