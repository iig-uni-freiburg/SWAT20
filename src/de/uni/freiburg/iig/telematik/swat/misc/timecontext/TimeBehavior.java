package de.uni.freiburg.iig.telematik.swat.misc.timecontext;


public interface TimeBehavior {

	/** return random time for given transition **/
	public double getNeededTime();

	/** false if current ressource is not yet used **/
	public boolean isInUse();

	/** set to true if ressource is currently in use **/
	public void setIsInUse(boolean isInUse);

}
