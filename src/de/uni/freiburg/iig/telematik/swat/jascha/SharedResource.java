package de.uni.freiburg.iig.telematik.swat.jascha;

public class SharedResource extends Resource {
	
	protected float usage=0.0f;

	public SharedResource(String name) {
		super(name);
	}

	@Override
	public boolean isAvailable() {
		return usage<1.0f;
	}
	
	public void setUsage(float newUsage){
		this.usage=newUsage;
	}
	
	public void incrementUsage(float increment){
		this.usage+=increment;
	}
	
	public void decrementUsage(float decrement) {
		usage = (usage > decrement) ? usage - decrement : 0.0f;
	}
	
	public float getUsage(){
		return usage;
	}
	
	

}
