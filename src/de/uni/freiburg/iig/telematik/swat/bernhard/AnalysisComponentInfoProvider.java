package de.uni.freiburg.iig.telematik.swat.bernhard;

/**
 * This interface defines all functions that are necessary to
 * retrieve informations from a logfile and from a ptnet.
 * @author bernhard
 *
 */
public interface AnalysisComponentInfoProvider {
	/**
	 * this method returns an array of all activities
	 * @return an array containing all activities
	 */
	public String[] getActivities();
	/**
	 * this method returns an array of all subjects
	 * @return an array containing all subjects
	 */
	public String[] getSubjects();
	/**
	 * this method returns an array of all roles
	 * @return an array containing all roles
	 */
	public String[] getRoles();
	/**
	 * update the lists
	 */
	public void update();
}
