package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.ArrayList;
import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;

public class MeasuredTimeBehaviour implements ITimeBehaviour {
	
	HashMap<Long, Double> map = new HashMap<>();
	// This ArrayList is finally the chart of the inversion method with on one axis the Probability 
	//and on the other axis the Value
	private ArrayList<Tuple> inversionArray = new ArrayList<Tuple>();
	
	public MeasuredTimeBehaviour(HashMap<Long, Double> map){
		this.map=map;
		
		double sum = 0;
		for ( Long key : map.keySet() ) {
			sum += map.get(key);
			//System.out.println("sum " + sum);
			inversionArray.add(new Tuple(sum, key));
		}
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
		long result = 0;
		double random = Math.random();
		System.out.println("getNeededTime(),  random = " + random);
		for ( int i = 0; i< inversionArray.size(); i++ ) {
			if(random <= inversionArray.get(i).getSum()) {
			result=  inversionArray.get(i).getKey();
			System.out.println("getNeededTime " + result);
			break;
		}
		
	}
		return result;
	}
	
	public String toString(){
		return "measured "+inversionArray.size()+" entries";
	}

}
