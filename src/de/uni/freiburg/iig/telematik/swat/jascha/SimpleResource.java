package de.uni.freiburg.iig.telematik.swat.jascha;

public class SimpleResource extends Resource {

	protected boolean isAvailable;

	protected SimpleResource(String name) {
		super(name); // Um den Namen soll sich die Über-Klasse kümmern
		isAvailable=true;
		type=ResourceType.SIMPLE;
	}
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
	public SimpleResource(String name, ResourceStore resourceStore){
		super(name);
		isAvailable=true;
		resourceStore.addResource(this);
		type=ResourceType.SIMPLE;
	}
	
	public boolean isAvailable() {
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
