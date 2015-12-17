package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceType;

public class ResourceDialog {
	ResourceType type;
	
	public ResourceDialog(ResourceType type){
		this.type=type;
	}
	
	public void show(){
		switch (type) {
		case SIMPLE:
			
			break;

		default:
			break;
		}
	}
	

}
