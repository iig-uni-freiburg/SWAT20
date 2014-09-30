package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions;

public class DistributionViewFactory {

	public static IDistributionView getDistributionView(DistributionType type) {
		switch (type) {
		case NORMAL:
			return new NormalDistributionView();
		case LOG_NORMAL:
			return new LogNormalDistributionView();
		default:
			return null;
		}
	}

}
