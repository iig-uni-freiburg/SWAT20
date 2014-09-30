package de.uni.freiburg.iig.telematik.swat.misc.timecontext.logAdapter;

import java.util.List;

public interface TimeAwareAdapter {

	public List<Double> getDurations(String activity, int resolution);

	public String[] getActivities();

}
