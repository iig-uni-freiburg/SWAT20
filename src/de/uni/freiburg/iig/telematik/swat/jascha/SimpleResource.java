package de.uni.freiburg.iig.telematik.swat.jascha;

import de.invation.code.toval.validate.ParameterException;

public class SimpleResource extends Resource {

	protected boolean isAvailable;
	//public boolean isPartOfResourceSet;
	protected int associatedSets;

	protected SimpleResource(String name) {
		super(name); // Um den Namen soll sich die Ueber-Klasse kuemmern
		isAvailable=true;
		//isPartOfResourceSet = false;
		associatedSets = 0;
		type=ResourceType.SIMPLE;
	}
	
	// Konstruktor, falls die Ressource als Teil eines ResourceSet erstellt wird
	public SimpleResource(String name, boolean fromSet){
		super(name);
		isAvailable=true;
		type=ResourceType.SIMPLE;
		if(fromSet){
			//isPartOfResourceSet = true;
			updateAssociatedSets(UpdateType.INCREASE);
		}
		else {
			//isPartOfResourceSet = false;
			associatedSets = 0;
		}		
	}	
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
	public SimpleResource(String name, ResourceStore resourceStore){
		super(name);
		isAvailable=true;
		//isPartOfResourceSet = false;
		associatedSets = 0;
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
	
	public int getAssociatedResourceSets(){
		return associatedSets;
	}
	
	public void updateAssociatedSets(UpdateType type){
		switch (type) {
		case DECREASE:
			if(associatedSets >= 1){
				associatedSets--;
			} 
			/*if(associatedSets == 1){
				associatedSets --;
				isPartOfResourceSet = false;			
			}*/
			if (associatedSets < 1){
				throw new ParameterException("Can't decrease because the SimpleResource is not part of a ResourceSet");
			}
			break;

		case INCREASE:
			associatedSets++;
			//isPartOfResourceSet = true;
			
			break;
		}
	}

}
