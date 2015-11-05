package de.uni.freiburg.iig.telematik.swat.jascha;

public class SimpleResource extends Resource{
	public SimpleResource(String name) {
		super(name);
		// Um den Namen soll sich die Über-Klasse kümmern
	}

	boolean isAvailable;
	String name;
	boolean isSharedResource;
	int destroyCounter;
	
	//Alternativ: ResourcenObjekt hat eine Liste von Resourcen
	
	
	public boolean isAvailable(){
		return destroyCounter>0 && isAvailable;
	}

}
