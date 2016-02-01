package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public abstract class Resource implements IResource {
	
	protected String name;
	protected ResourceType type;
	protected boolean isDisabled=false;
	
	public String getName(){
		return name; //gilt nun fuer alle, die von Resource erben
	}
	
	public Resource(String name){
		this.name=name; //ebenfalls fuer alle, die von Resource erben. Um Namen muss man sich also nicht mehr kuemmern.
	}
	
	public ResourceType getType(){
		return type;
	}
	
	protected Resource(){}
	
	public String toString(){
		return name;
	}
	
	public String getDetailString(){
		String available = "(UNavailable)";
		if(isAvailable()) available ="(available)";
		return "Name: "+name+" ["+type+"] "+available;
	}

	
	public void disable(boolean setDisable){
		isDisabled=setDisable;
	}

}
