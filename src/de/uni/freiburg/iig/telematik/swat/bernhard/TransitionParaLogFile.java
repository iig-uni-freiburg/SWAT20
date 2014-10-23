package de.uni.freiburg.iig.telematik.swat.bernhard;


public class TransitionParaLogFile extends TransitionParameter {


	public TransitionParaLogFile(String name, LogFileInformation componentInfo) {
		super(name, componentInfo.getActivities());
	}

}
