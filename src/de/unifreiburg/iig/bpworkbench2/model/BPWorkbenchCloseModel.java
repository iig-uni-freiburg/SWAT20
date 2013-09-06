package de.unifreiburg.iig.bpworkbench2.model;

import java.util.Observable;

//only for testing
public class BPWorkbenchCloseModel extends Observable {
	public boolean close = false;

	public void closeGui() {
		close = true;
		setChanged();
		notifyObservers("soll geschlossen werden");
	}
}
