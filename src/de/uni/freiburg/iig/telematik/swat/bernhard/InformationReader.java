package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

public interface InformationReader {
	public List<String> getActivities();
	public String[] getActivitiesArray();
	public String[] getRoleArray();
	public void update();
}
