package de.uni.freiburg.iig.telematik.swat.jascha;

import de.invation.code.toval.validate.ParameterException;

public class SimpleResource extends Resource {

	protected boolean isAvailable;
	protected int associatedSets;

	//This one is being used
	protected SimpleResource(String name) {
		super(name);
		isAvailable=true;
		associatedSets = 0;
		type=ResourceType.SIMPLE;
	}
	
	//This one is being used for construction as part of a ResourceSet
	public SimpleResource(String name, boolean fromSet){
		super(name);
		isAvailable=true;
		type=ResourceType.SIMPLE;
		if(fromSet){			
			associatedSets = 1;
		}
		else {
			associatedSets = 0;
		}		
	}	
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
	public SimpleResource(String name, ResourceStore resourceStore){
		super(name);
		isAvailable=true;
		associatedSets = 0;
		type=ResourceType.SIMPLE;
		resourceStore.addResource(this);
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
	
	public int getAssociatedResourceSets(){
		return associatedSets;
	}
	
	public void updateAssociatedSets(UpdateType type){
		int nr = associatedSets;
		switch (type) {
		case DECREASE:
			if (associatedSets < 1){
				throw new ParameterException("Can't decrease because the SimpleResource is not part of a ResourceSet");
			}
			if(associatedSets >= 1){
				nr--;
			} 
			break;

		case INCREASE:
			nr++;
			
			break;
		}
		associatedSets = nr;
	}

}
