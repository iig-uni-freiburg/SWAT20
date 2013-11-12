package de.uni.freiburg.iig.telematik.swat.editor.properties;

import de.invation.code.toval.graphic.RestrictedTextField.Restriction;

public enum PNProperty {
	
	PLACE_LABEL(Restriction.NOT_EMPTY), 
	TRANSITION_LABEL(Restriction.NOT_EMPTY), 
	PLACE_SIZE(Restriction.POSITIVE_INTEGER), 
	TRANSITION_SIZE(Restriction.POSITIVE_INTEGER), 
	ARC_WEIGHT(Restriction.POSITIVE_INTEGER);
	
	Restriction restriction = null;
	
	private PNProperty(Restriction restriction){
		this.restriction = restriction;
	}
	
	public Restriction getRestriction(){
		return restriction;
	}

}
