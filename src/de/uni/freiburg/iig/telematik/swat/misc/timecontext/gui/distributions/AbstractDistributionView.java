package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

public abstract class AbstractDistributionView implements IDistributionView {

	protected double params[];
	protected String paramNames[];
	protected DistributionType type = DistributionType.UNKNOWN;

	@Override
	public AbstractRealDistribution getDistribution() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getConfigView() {

		JLabel[] labels = new JLabel[paramNames.length];
		params = new double[paramNames.length];
		JPanel panel = new JPanel();
		JTextField[] textFields = new JTextField[params.length];

		for (int i = 0; i < params.length; i++) {
			labels[i] = new JLabel(paramNames[i]);
			panel.add(labels[i]);
			textFields[i] = new JTextField();
			textFields[i].setPreferredSize(new Dimension(40, 20));
			textFields[i].addPropertyChangeListener(new TextAction(i));
			panel.add(textFields[i]);
		}
		//
		//		mean.setPreferredSize(new Dimension(50, 20));
		//		sd.setPreferredSize(new Dimension(50, 20));
		//		JPanel panel = new JPanel();
		//		panel.add(new JLabel("Mean: "));
		//		panel.add(mean);
		//		panel.add(new JLabel("Derivation: "));
		//		panel.add(sd);
		//		panel.setVisible(true);
		//		return panel;

		panel.setVisible(true);
		return panel;
	}

	@Override
	public Icon getDistributionIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DistributionType getType() {
		return type;
	}

	class TextAction implements PropertyChangeListener {
		protected int i;

		public TextAction(int i) {
			this.i = i;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			try {
				params[i] = Double.parseDouble(((JTextField) evt.getSource()).getText());
			} catch (NumberFormatException e) {
			}
			//params[i] = Double.parseDouble(evt.getNewValue().toString());
		}

	}

	public void setParamNames(String... names) {
		paramNames = names;
	}

	public void setType(DistributionType type) {
		this.type = type;
	}

	public String toString() {

		return type.toString() + " distributed";
		//		switch (type) {
		//		case NORMAL:
		//			return "Normal distributed";
		//		case LOG_NORMAL:
		//			return "Log-Normal distributed";
		//		case BETA:
		//			return "Beta distributed";
		//		case EXPONENTIAL:
		//			return "Exponential distributed
		//
		//		default:
		//			return "unknown";
		//		}
	}

}
