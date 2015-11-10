package de.uni.freiburg.iig.telematik.swat.jascha;

public class SimpleResource extends Resource {

	int destroyCounter;
	protected boolean isAvailable;

	public SimpleResource(String name) {
		super(name); // Um den Namen soll sich die Über-Klasse kümmern
		setAvailability(true);
	}
	
	public void setAvailability(boolean isAvailable){
		this.isAvailable=isAvailable;
	}

	public boolean isAvailable() {
		return destroyCounter > 0 && isAvailable;
	}

}
