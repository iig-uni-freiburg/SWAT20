package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public abstract class Resource implements IResource {
	
	protected String name;
	protected ResourceType type;
	
	public String getName(){
		return name; //gilt nun für alle, die von Resource erben
	}
	
	public Resource(String name){
		this.name=name; //ebenfalls für alle, die von Resource erben. Um Namen muss man sich also nicht mehr kümmern.
	}
	
	public ResourceType getType(){
		return type;
	}
	
	public Resource(){}
	
	public String toString(){
		return name;
	}
	
	public String getDetailString(){
		return "Name: "+name+", Type: "+type;
	}

}
