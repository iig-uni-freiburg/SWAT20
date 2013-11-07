package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.util.EventObject;

import de.unifreiburg.iig.bpworkbench2.editor.properties.PNProperties.FieldType;

public class PNChangeEvent extends EventObject{
	
	private static final long serialVersionUID = -4892583337247587700L;
	private PNProperty property = null;
	private FieldType fieldType = null;
	private String name = null;
	private Object oldValue = null;
	private Object newValue = null;

	public PNChangeEvent(Object source, FieldType fieldType, String name, PNProperty property, Object oldValue, Object newValue) {
		super(source);
		this.fieldType = fieldType;
		this.property = property;
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public String getName(){
		return name;
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

	public FieldType getFieldType() {
		return fieldType;
	}

}
