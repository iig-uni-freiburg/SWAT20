package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;
/**
 * This interface defines all functions that are necessary to
 * retrieve informations from a logfile and from a ptnet.
 * @author bernhard
 *
 */
public interface LogFileReader {
	public String[] getActivities();
	public String[] getSubjects();
	public String[] getRoles();
	public void update();
}
