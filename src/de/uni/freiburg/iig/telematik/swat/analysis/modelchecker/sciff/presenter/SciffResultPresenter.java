package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.presenter;

import java.awt.Dimension;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

public class SciffResultPresenter {

	public enum resultType {
		CORRECT, WRONG
	}

	private CheckerReport report;

	public SciffResultPresenter(CheckerReport report) {
		this.report = report;
	}

	public void show(resultType resulttype) {
		JFrame result = new JFrame();
		result.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		result.setSize(new Dimension(600, 600));
		String resultText = getResultString(resulttype);
		JTextPane textArea = new JTextPane();
		textArea.setContentType("text/html");
		textArea.setText(resultText);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		//scrollPane.add(textArea);
		result.add(scrollPane);
		result.setLocationByPlatform(true);
		result.setVisible(true);

	}

	public String getResultString(resultType type) {
		LinkedList<Integer> result = new LinkedList<Integer>();
		StringBuilder b = new StringBuilder();
		switch (type) {
		case CORRECT:
			result.addAll(report.correctInstances());
			break;
		case WRONG:
			result.addAll(report.wrongInstances());
			break;
		}
		b.append(type.toString() + " examples: <br>");
		for (int i : result) {
			ISciffLogTrace trace = report.getLog().getInstance(i);
			//b.append(report.getLog().getInstances().get(i).getName());
			b.append("<b>");
			for (int entry=0;entry<trace.size();entry++){
				try {
					b.append(trace.get(entry).getElement());
				} catch (IndexOutOfBoundsException | IOException e) {
				}
			}
			b.append("</b>");
			try {
				b.append(" from: " + trace.get(0).getTimestamp()+" to "+trace.get(trace.size()-1).getTimestamp());
			} catch (Exception e) {
			}

			b.append("<br>");
		}
		
		//System.out.println("ResultText: " + b.toString());

		return b.toString();
	}

}
