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
		switch (resulttype) {
		case CORRECT:
			result.setTitle("Correct Log Examples");
			break;
		case WRONG:
			result.setTitle("Wrong Log Examples");
			break;
		default:
			break;
		}
		result.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		result.setSize(new Dimension(600, 600));
		String resultText = getResultString(resulttype, 100);
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

	public String getResultString(resultType type, int maxLength) {
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
		System.out.println("Traversing");
		b.append(type.toString() + " examples: <br>");
		int currentIndex=0;
		for (int i : result) {
			currentIndex++;
			if(currentIndex>maxLength) break;
			ISciffLogTrace trace = report.getLog().getInstance(i);
//			b.append(i+": ");
//			b.append(report.getLog().getInstances().get(i).getName());
			for (int entry = 0; entry < trace.size(); entry++) {
				try {
					b.append("<b>");
					b.append(" "+trace.get(entry).getElement());
					b.append("</b>");
					if(trace.get(entry).getOriginator()!=null) b.append("(" + trace.get(entry).getOriginator()+") ");
				} catch (IndexOutOfBoundsException | IOException e) {
				}
			}
			try {
				b.append(" from: " + trace.get(0).getTimestamp() + " to " + trace.get(trace.size() - 1).getTimestamp());
				b.append("<br><br>");
			} catch (Exception e) {
			}

		}

		return b.toString();
	}

}
