package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

public interface IDistributionView {

	public AbstractRealDistribution getDistribution();

	public JComponent getConfigView();

	public String toString();

	public Icon getDistributionIcon();

	public DistributionType getType();

}