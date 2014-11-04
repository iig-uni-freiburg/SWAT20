package de.uni.freiburg.iig.telematik.swat.workbench;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;

public class SwatState {

	private static SwatState instance = null;
	
	private OperatingMode operatingMode = null;
	
	private String activeFile = null;

	private HashMap<String, String> activeContext = new HashMap<String, String>();

	private List<SwatStateListener> listeners = new ArrayList<SwatStateListener>();
	
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
	
	public void setActiveFile(String file) {
		this.activeFile = file;
	}

	public String getActiveFile() {
		return activeFile;
	}

	public TimeContext getActiveContext(String netID) {
		String timeContextName = activeContext.get(netID);
		Validate.notNull(timeContextName);
		Validate.notEmpty(timeContextName);
		TimeContext result = SwatComponents.getInstance().getTimeContext(netID, timeContextName);
		Validate.notNull(result);
		return result;
	}

	public void setActiveContext(String netID, String activeContext) {
		if (SwatComponents.getInstance().getTimeContext(netID, activeContext) == null)
			throw new ParameterException(activeContext + " is not a valid timecontext");
		this.activeContext.put(netID, activeContext);
	}

	public void addListener(SwatStateListener listener) throws ParameterException{
		Validate.notNull(listener);
		if(!listeners.contains(listener))
			listeners.add(0, listener);
	}

	public enum OperatingMode {
		EDIT_MODE, ANALYSIS_MODE;
	}

}
