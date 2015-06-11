package de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeBehavior;

public abstract class AbstractDistributionView implements IDistributionView, TimeBehavior, Cloneable {


	@XStreamOmitField
	protected AbstractRealDistribution distribution;

	@XStreamOmitField
	protected String paramNames[];

	@XStreamOmitField
	boolean isInUse = false;

	@Override
	public boolean isInUse() {
		return isInUse;
	}

	@Override
	public void setIsInUse(boolean isInUse) {
		this.isInUse = isInUse;

	}

	@Override
	public double getNeededTime() {
		if (distribution == null)
			distribution = getDistribution(); //works only for StochasticTimeBahaviour
		return distribution.sample();
	}


	protected double params[];

	protected DistributionType type = DistributionType.UNKNOWN;

	//public abstract AbstractRealDistribution getDistribution();

	@Override
	public JPanel getConfigView() {

		JLabel[] labels = new JLabel[paramNames.length];
		if (params == null)
			params = new double[paramNames.length];
		JPanel panel = new JPanel();
		JTextField[] textFields = new JTextField[params.length];

		for (int i = 0; i < params.length; i++) {
			labels[i] = new JLabel(paramNames[i]);
			panel.add(labels[i]);
			textFields[i] = new JTextField();
			textFields[i].setPreferredSize(new Dimension(60, 20));
			textFields[i].setText(Double.toString(params[i]));
			textFields[i].getDocument().addDocumentListener(new TextAction(i));
			panel.add(textFields[i]);
		}

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


	public void setParamNames(String... names) {
		paramNames = names;
	}

	public void setType(DistributionType type) {
		this.type = type;
	}

	public void setParams(double... parameters) {
		params = parameters.clone();
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
	}

	protected class TextAction implements DocumentListener {
		protected int i;

		public TextAction(int i) {
			this.i = i;
		}

		public void update(DocumentEvent arg0) {
			try {
				params[i] = Double.parseDouble(arg0.getDocument().getText(0, arg0.getDocument().getLength()));
			} catch (NumberFormatException e) {
			}
			//params[i] = Double.parseDouble(evt.getNewValue().toString());
			catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			update(e);

		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			update(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			update(e);

		}
	}

}
