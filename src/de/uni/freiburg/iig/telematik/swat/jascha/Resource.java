package de.uni.freiburg.iig.telematik.swat.jascha;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public abstract class Resource implements IResource {
	
	protected String name;
	
	public String getName(){
		return name; //gilt nun für alle, die von Resource erben
	}
	
	public Resource(String name){
		this.name=name; //ebenfalls für alle, die von Resource erben. Um Namen muss man sich also nicht mehr kümmern.
	}

}
