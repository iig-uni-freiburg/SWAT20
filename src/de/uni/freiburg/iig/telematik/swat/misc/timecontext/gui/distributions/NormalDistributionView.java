package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

public class NormalDistributionView implements IDistributionView {

	JTextField mean = new JTextField();
	JTextField sd = new JTextField();

	public static void main(String[] args) {
		JDialog dialog = new JDialog();
		dialog.add(new NormalDistributionView().getConfigView());
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new NormalDistribution(getMean(), getSd());
	}

	@Override
	public JPanel getConfigView() {
		mean.setPreferredSize(new Dimension(50, 20));
		sd.setPreferredSize(new Dimension(50, 20));
		JPanel panel = new JPanel();
		panel.add(new JLabel("Mean: "));
		panel.add(mean);
		panel.add(new JLabel("Derivation: "));
		panel.add(sd);
		panel.setVisible(true);
		return panel;
	}

	@Override
	public String toString() {
		return "Normal distributed";
	}

	@Override
	public Icon getDistributionIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.NORMAL;
	}

	private double getMean() {
		return Double.parseDouble(mean.getText());
	}

	private double getSd() {
		return Double.parseDouble(sd.getText());
	}

}
