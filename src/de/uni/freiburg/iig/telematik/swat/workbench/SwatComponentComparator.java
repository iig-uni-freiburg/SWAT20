package de.uni.freiburg.iig.telematik.swat.workbench;

import java.util.Comparator;

public class SwatComponentComparator implements Comparator<WorkbenchComponent> {

	@Override
	public int compare(WorkbenchComponent o1, WorkbenchComponent o2) {
		return o1.getName().compareTo(o2.getName());
	}



}
