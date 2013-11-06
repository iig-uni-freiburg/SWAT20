package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.util.EventObject;

public class PNChangeEvent extends EventObject{
	
	private static final long serialVersionUID = -4892583337247587700L;
	private PNProperty property = null;
	private Object oldValue = null;
	private Object newValue = null;

	public PNChangeEvent(Object source, PNProperty property, Object oldValue, Object newValue) {
		super(source);
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public PNProperty getProperty() {
		return property;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

}
