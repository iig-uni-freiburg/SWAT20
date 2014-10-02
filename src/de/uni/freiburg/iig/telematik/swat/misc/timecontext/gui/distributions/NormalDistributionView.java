package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

public class NormalDistributionView extends AbstractDistributionView {

	//JTextField mean = new JTextField();
	//JTextField sd = new JTextField();

	public static void main(String[] args) {
		JDialog dialog = new JDialog();
		dialog.add(new NormalDistributionView().getConfigView());
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public NormalDistributionView() {
		setParamNames("mean", "sd");
		setType(DistributionType.NORMAL);
	}

	@Override
	public AbstractRealDistribution getDistribution() {
		return new NormalDistribution(params[0], params[1]);
	}

	@Override
	public JPanel getConfigView() {
		//		mean.setPreferredSize(new Dimension(50, 20));
		//		sd.setPreferredSize(new Dimension(50, 20));
		//		JPanel panel = new JPanel();
		//		panel.add(new JLabel("Mean: "));
		//		panel.add(mean);
		//		panel.add(new JLabel("Derivation: "));
		//		panel.add(sd);
		//		panel.setVisible(true);
		//		return panel;
		return super.getConfigView();
	}

	//	@Override
	//	public String toString() {
	//		return "Normal distributed";
	//	}

	@Override
	public Icon getDistributionIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.NORMAL;
	}

	//	protected double getMean() {
	//		return Double.parseDouble(mean.getText());
	//	}
	//
	//	protected double getSd() {
	//		return Double.parseDouble(sd.getText());
	//	}

}
