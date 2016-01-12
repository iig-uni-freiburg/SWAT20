package de.uni.freiburg.iig.telematik.swat.jascha;

public class SimpleResource extends Resource {

	protected boolean isAvailable;
	public boolean isPartOfResourceSet;

	protected SimpleResource(String name) {
		super(name); // Um den Namen soll sich die Ueber-Klasse kuemmern
		isAvailable=true;
		isPartOfResourceSet = false;
		type=ResourceType.SIMPLE;
	}
	
	// Konstruktor, falls die Ressource als Teil eines ResourceSet erstellt wird
	public SimpleResource(String name, boolean fromSet){
		super(name);
		isAvailable=true;
		if(fromSet){
			isPartOfResourceSet = true;
		}
		else isPartOfResourceSet = false;
			
		type=ResourceType.SIMPLE;
	}	
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
	public SimpleResource(String name, ResourceStore resourceStore){
		super(name);
		isAvailable=true;
		isPartOfResourceSet = false;
		resourceStore.addResource(this);
		type=ResourceType.SIMPLE;
	}
	
	
	public boolean isAvailable() {
		
		if(isDisabled)
			return false;
		
		return isAvailable;
	}

	@Override
	public void use() {
		isAvailable=false;
		
	}

	@Override
	public void unUse() {
		isAvailable=true;
	}

	@Override
	public void reset() {
		isAvailable=true;
	}

}
