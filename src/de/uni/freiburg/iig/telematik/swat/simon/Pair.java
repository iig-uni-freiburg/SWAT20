package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.Date;

public class Pair {
	private Date mStart;
	private Date mEnd;
	
	public Pair(Date start, Date end) {
		mStart = start;
		mEnd = end;
	}
	
	public Date getStart() {
		return mStart;
	}
	
	public Date getEnd() {
		return mEnd;
	}
	
	
}
