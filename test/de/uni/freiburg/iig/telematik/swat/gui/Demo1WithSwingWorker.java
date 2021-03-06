package de.uni.freiburg.iig.telematik.swat.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Demo1WithSwingWorker extends JFrame implements ActionListener {
	JLabel jlabe_text, jlabe_result;
	JTextField jtext_input;
	JButton jbutt_calc;
	SwingWorker<String, Void> worker;
	String theresult;

	/**
	 * Init the GUI elements
	 */
	public void initComponents() {
		jlabe_text = new JLabel("Calculate prime:");
		jtext_input = new JTextField();
		jlabe_result = new JLabel();
		jbutt_calc = new JButton("Calc");
		jbutt_calc.addActionListener(this);
		jbutt_calc.setActionCommand("calc");

		getContentPane().setLayout(new GridLayout(1, 4));
		getContentPane().add(jlabe_text);
		getContentPane().add(jtext_input);
		getContentPane().add(jbutt_calc);
		getContentPane().add(jlabe_result);

		pack();
		setSize(400, 60);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if (cmd.equalsIgnoreCase("calc")) { // calc button pressed
			// Construct a new SwingWorker
			worker = new SwingWorker<String, Void>() {

				@Override
				protected String doInBackground() {
					return getXPrime(Integer.parseInt(jtext_input.getText()));
				}

				@Override
				protected void done() {
					try {
						jlabe_result.setText(get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			};
			// Execute the SwingWorker; the GUI will not freeze
			worker.execute();
		}
	}

	public static void main(String[] args) {
		new Demo1WithSwingWorker().initComponents();
	}

	/**
	 * The time consuming method. For this example it's not important how this
	 * method works. Given a number n this method calculates the n th prime e.g.
	 * given n = 5 the 5th prime is 11
	 * 
	 * @param the
	 *            n th prime to be calculated
	 * @return the prime
	 */
	public String getXPrime(int n) {
		int primes = 1;
		int zahl = 2;
		while (primes != n) {
			zahl++;
			for (int i = 2; i < zahl; i++) {
				if (!(zahl % i == 0) && i + 1 == zahl) {
					primes++;
					i = zahl + 1;
				} else if (zahl % i == 0) {
					i = zahl + 1;
				}
			}
		}
		return String.valueOf(zahl);
	}
}
