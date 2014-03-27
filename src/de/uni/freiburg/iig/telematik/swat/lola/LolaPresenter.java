package de.uni.freiburg.iig.telematik.swat.lola;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class LolaPresenter {

	protected JFrame result;
	protected String output;

	public LolaPresenter(String output) {
		//this.output = "<html><body>" + output.replaceAll("(\r\n|\n)", "<br />" + "</body></html>");
		this.output = output;
		makeWindow();
	}

	public void show() {
		result.setVisible(true);
	}

	private void makeWindow() {
		result = new JFrame();
		result.setSize(new Dimension(400, 300));
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
			//result.dispose();
			
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
