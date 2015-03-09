package de.uni.freiburg.iig.telematik.swat.workbench;

import java.util.Comparator;

import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

public class SwatComponentComparator implements Comparator<ViewComponent> {

	@Override
	public int compare(ViewComponent o1, ViewComponent o2) {
		return o1.getName().compareTo(o2.getName());
	}



}
