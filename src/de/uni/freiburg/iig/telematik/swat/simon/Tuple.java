package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.Date;

public class Tuple {

	private Double mSum;
	private Long mKey;
	
	public Tuple(Double sum, Long key) {
		mSum = sum;
		mKey = key;
	}
	
	public Double getSum() {
		return mSum;
	}
	
	public Long getKey() {
		return mKey;
	}
	

}
