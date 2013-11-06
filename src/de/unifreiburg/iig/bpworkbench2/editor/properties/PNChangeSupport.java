package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.util.HashSet;
import java.util.Set;

public class PNChangeSupport {
	private Set<PNPropertiesListener> listeners = new HashSet<PNPropertiesListener>();
	
	public void addListener(PNPropertiesListener listener){
		listeners.add(listener);
	} 
	public void removeListener(PNPropertiesListener listener){
		listeners.remove(listener);
	} 
	public void fireChangeEvent(PNChangeEvent event){
		for(PNPropertiesListener listener : listeners){
			listener.propertyChange(event);
		}
	}

}
