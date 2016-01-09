package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.ArrayList;
import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;

public class InversionMethod implements ITimeBehaviour{
	
	ArrayList<Pair> pairs;

	public InversionMethod(ArrayList<Pair> inversionValues) {
		pairs = inversionValues;
	}
	
	

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAvailability(boolean isAvailable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getNeededTime() {
		

		
		return 0;
	}
}
