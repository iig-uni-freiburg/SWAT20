package de.uni.freiburg.iig.telematik.swat.logs;

import java.util.Comparator;

public class LogModelComparator implements Comparator<LogModel> {

	@Override
	public int compare(LogModel o1, LogModel o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
