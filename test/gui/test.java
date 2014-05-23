package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Anonyme Inline Klasse
		Runnable gui = new Runnable() {

			@Override
			public void run() {
				JFrame fenster = new JFrame("Fenster Titel");
				fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JLabel text = new JLabel("Dies ist ein Label");
				final JLabel observer = new JLabel("Observer");
				fenster.getContentPane().add(text);
				fenster.setSize(300, 300);
				fenster.setVisible(true);

				text.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						observer.setText("mouse release");

					}

					@Override
					public void mousePressed(MouseEvent e) {
						observer.setText("mouse pressed");

					}

					@Override
					public void mouseExited(MouseEvent e) {
						observer.setText("mouse exited");

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						observer.setText("mouse entered");

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						observer.setText("mouse clicked");

					}
				});
				fenster.getContentPane().add(observer);
			}
		};
		SwingUtilities.invokeLater(gui);
		// Thread test = new Thread(gui);
		// test.start();

	}
}
