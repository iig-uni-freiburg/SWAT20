package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.List;

public class CompoundResource extends Resource{

	public CompoundResource(String name) {
		super(name);
		// Name in Über-Klasse speichern lassen.
	}

	List<Resource> resources = new ArrayList<>();

	@Override
	public boolean isAvailable() {
		for (Resource o:resources){
			if (!o.isAvailable()) return false;
		}
		return true;
	}
	
	
	
	
}
