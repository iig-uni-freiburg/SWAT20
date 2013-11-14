package de.uni.freiburg.iig.telematik.swat.workbench;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;

public class SwatState {

	private static SwatState instance = null;
	
	private OperatingMode operatingMode = null;
	
	private Set<SwatStateListener> listeners = new HashSet<SwatStateListener>();
	
	public static SwatState getInstance(){
		if(instance == null){
			instance = new SwatState();
		}
		return instance;
	}
	
	public OperatingMode getOperatingMode(){
		return operatingMode;
	}
	
	public void setOperatingMode(Object sender, OperatingMode operatingMode) throws ParameterException {
		Validate.notNull(sender);
		Validate.notNull(operatingMode);
		if(getOperatingMode() == operatingMode)
			return;

		this.operatingMode = operatingMode;
		for(SwatStateListener listener: listeners){
			if(listener != sender){
				listener.operatingModeChanged();
			}
		}
	}
	
	public void addListener(SwatStateListener listener) throws ParameterException{
		Validate.notNull(listener);
		listeners.add(listener);
	}

	public enum OperatingMode {
		EDIT_MODE, ANALYSIS_MODE;
	}
}
