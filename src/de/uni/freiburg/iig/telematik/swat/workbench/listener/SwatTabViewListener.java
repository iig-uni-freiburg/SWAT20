package de.uni.freiburg.iig.telematik.swat.workbench.listener;

import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchComponent;

public interface SwatTabViewListener {

	public void activeTabChanged(int index, WorkbenchComponent component);

}
