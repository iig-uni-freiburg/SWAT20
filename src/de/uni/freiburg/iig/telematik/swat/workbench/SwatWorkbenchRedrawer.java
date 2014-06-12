package de.uni.freiburg.iig.telematik.swat.workbench;

import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatWorkbenchRedrawListener;

/** to inform the workbench to redraw itself **/
public class SwatWorkbenchRedrawer {
	private static SwatWorkbenchRedrawer mySwatWorkbenchDrawer = new SwatWorkbenchRedrawer();

	private Set<SwatWorkbenchRedrawListener> listener = new HashSet<SwatWorkbenchRedrawListener>();

	public SwatWorkbenchRedrawer getInstance() {
		return mySwatWorkbenchDrawer;
	}

	public void addListener(SwatWorkbenchRedrawListener listener) {
		this.listener.add(listener);
	}

	public void redrawWorkbench(Object sender) {
		for (SwatWorkbenchRedrawListener listener : this.listener) {
			if (listener != sender) {
				listener.redrawWorkbench(sender);
			}
		}

	}

}
