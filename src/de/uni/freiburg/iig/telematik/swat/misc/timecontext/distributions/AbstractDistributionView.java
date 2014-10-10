package de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeBehavior;

public abstract class AbstractDistributionView implements IDistributionView, TimeBehavior {

	@XStreamOmitField
	protected AbstractRealDistribution distribution;

	@Override
	public double getNeededTime() {
		if (distribution == null)
			distribution = getDistribution();
		return distribution.sample();
	}

	protected double params[];
	protected String paramNames[];
	protected DistributionType type = DistributionType.UNKNOWN;

	@Override
	public AbstractRealDistribution getDistribution() {
		// must be implemented by overriding classes
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
			textFields[i].setText(Double.toString(params[i]));
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

	public void setParams(double... parameters) {
		params = parameters;
	}

	public double[] getParams() {
		return params;
	}

	public String toString() {
		StringBuilder paramString = new StringBuilder();
		if (params != null && params.length != 0) {
			paramString.append("(");
			try {
				for (int i = 0; i < params.length; i++) {
					paramString.append(params[i]);
					paramString.append(", ");
				}
				paramString.delete(paramString.length() - 2, paramString.length() - 1);
			} catch (NullPointerException e) {
			}
			paramString.append(")");
		}


		return type.toString() + paramString.toString() + " distributed";
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
