package de.uni.freiburg.iig.telematik.swat.simon;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Pair {
	private Date mStart;
	private Date mEnd;
	
	public Pair(Date start, Date end) {
		mStart = start;
		mEnd = end;
	}
	
	public Date getStartTime() {
		return mStart;
	}
	
	public Date getEndTime() {
		return mEnd;
	}
	

}
