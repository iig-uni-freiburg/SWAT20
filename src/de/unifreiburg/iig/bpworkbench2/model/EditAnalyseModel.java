package de.unifreiburg.iig.bpworkbench2.model;

import java.util.Observable;

public class EditAnalyseModel extends Observable {
	private boolean edit = true;
	private static EditAnalyseModel myEoAM = new EditAnalyseModel();

	public boolean isInEditMode() {
		return edit;
	}

	public static EditAnalyseModel getModel() {
		return myEoAM;
	}

	public void setEditMode(boolean isInEditMode) {
		if (edit == isInEditMode) {
			// do nothing
			return;
		}
		edit = isInEditMode;
		setChanged();
		notifyObservers();
	}

}
