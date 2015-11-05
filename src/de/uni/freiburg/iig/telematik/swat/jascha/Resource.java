package de.uni.freiburg.iig.telematik.swat.jascha;

public abstract class Resource {
	
	protected String name;
	
	public abstract boolean isAvailable(); // muss implementiert werden
	
	public String getName(){
		return name; //gilt nun für alle, die von Resource erben
	}
	
	public Resource(String name){
		this.name=name; //ebenfalls für alle, die von Resource erben. Um Namen muss man sich also nicht mehr kümmern.
	}

}
