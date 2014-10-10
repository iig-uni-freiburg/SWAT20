package de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeBehavior;

public interface IDistributionView extends TimeBehavior {

	public AbstractRealDistribution getDistribution();

	public JComponent getConfigView();

	public String toString();

	public Icon getDistributionIcon();

	public DistributionType getType();

}
