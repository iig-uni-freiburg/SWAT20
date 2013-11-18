package de.uni.freiburg.iig.telematik.swat.editor.properties;

import de.invation.code.toval.graphic.RestrictedTextField.Restriction;

public enum PNProperty {
	
	PLACE_LABEL(Restriction.NOT_EMPTY),
	TRANSITION_LABEL(Restriction.NOT_EMPTY), 
	PLACE_SIZE(Restriction.POSITIVE_INTEGER) {
		public String toString() {
			return "size";
		}
	}
	, 
	TRANSITION_SIZE(Restriction.POSITIVE_INTEGER) {
		public String toString() {
			return "size";
		}
	}, 
	ARC_WEIGHT(Restriction.POSITIVE_INTEGER) {
		public String toString() {
			return "weight";
		}
	};
	
	Restriction restriction = null;
	
	private PNProperty(Restriction restriction){
		this.restriction = restriction;
	}
	
	public Restriction getRestriction(){
		return restriction;
	}
	

//	public enum MyType {
//		ONE {
//		    public String toString() {
//		        return "this is one";
//		    }
//		},
//		 
//		TWO {
//		    public String toString() {
//		        return "this is two";
//		    }
//		}
//		}

}
