package de.uni.freiburg.iig.telematik.swat.jascha;

public class SharedResource extends Resource {
	
	protected float usage=0.0f;
	protected float increment=0.1f; //increment usage by x percent

	public SharedResource(String name) {
		super(name);
	}

	@Override
	public boolean isAvailable() {
		return usage<1.0f;
	}
	
	protected void incrementUsage(){
		this.usage+=increment;
	}
	
	public void decrementUsage() {
		usage = (usage > increment) ? usage - increment : 0.0f;
	}
	
	public float getUsage(){
		return usage;
	}

	@Override
	public void use() {
		incrementUsage();
		
	}

	@Override
	public void unUse() {
		decrementUsage();
	}

	@Override
	public void reset() {
		usage=0f;
		
	}
	
	

}
