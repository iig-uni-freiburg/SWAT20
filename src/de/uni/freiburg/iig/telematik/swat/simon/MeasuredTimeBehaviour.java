package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;

public class MeasuredTimeBehaviour implements ITimeBehaviour {
	
	HashMap<Long, Double> map = new HashMap<>();
	
	public MeasuredTimeBehaviour(HashMap<Long, Double> map){
		this.map=map;
		//hier inversions-array erstellen, dann muss bei getNeededTime nicht ständig die Summe neu berechnet werden.
	}

	public HashMap<Long, Double> getMap() {
		return map;
	}

	public void setMap(HashMap<Long, Double> map) {
		this.map = map;
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
		long a = 0;
		double random = Math.random();
		double sum = 0;
		System.out.println("random " + random);
		for ( Long key : map.keySet() ) {
			sum += map.get(key);
			System.out.println("sum " + sum);
		if(random <= sum) {
			a=  key;
			System.out.println(a);
			break;
		}
		
	}
		return a;
	}

}
