package de.uni.freiburg.iig.telematik.swat.jascha;

public class SimpleResource extends Resource {

	protected boolean isAvailable;

	public SimpleResource(String name) {
		super(name); // Um den Namen soll sich die Über-Klasse kümmern
		isAvailable=true;
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

}
