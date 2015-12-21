package de.uni.freiburg.iig.telematik.swat.jascha;

public class SharedResource extends Resource {
	
	protected float usage=0.0f;
	protected float increment=0.1f; //increment usage by x percent

	public float getIncrement() {
		return increment;
	}

	public void setIncrement(float increment) {
		this.increment = increment;
	}

	public SharedResource(String name) {
		super(name);
		type=ResourceType.SHARED;
	}
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
	public SharedResource(String name, ResourceStore resourceStore){
		super(name);
		resourceStore.addResource(this);
		type=ResourceType.SHARED;
	}

	@Override
	public boolean isAvailable() {
		return usage<1.0f;
	}
	
	protected void incrementUsage(){
		this.usage+=increment;
	}
	
	// Benutzung um bestimmten Anteil erh�hen, nicht nur um feste 10%
	public void incrementUsageBy(float f){
		if (usage + f <= 1.0){
			this.usage+= f;
		}
		else {
			//Error: not enough free usage available.		
		}
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
	
	//Override?
	public void use(float f){
		incrementUsageBy(f);
	}

	@Override
	public void unUse() {
		decrementUsage();
	}

	@Override
	public void reset() {
		usage=0f;
		
	}
	
	public String getDetailString(){
		String result = super.getDetailString();
		result+=", max. simultaneous usage: "+1/increment;
		return result;
	}

}
