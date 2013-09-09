package de.unifreiburg.iig.bpworkbench2.model;

import java.util.Observable;

public class EditAnalyzeModel extends Observable {
	private boolean edit = true;
	private static EditAnalyzeModel myEoAM = new EditAnalyzeModel();

	public boolean isInEditMode() {
		return edit;
	}

	public static EditAnalyzeModel getModel() {
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
