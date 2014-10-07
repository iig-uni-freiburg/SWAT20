package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;

public class SwatTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 8746333990209477776L;
	private SwatComponentType objectType = null;
	private String objectID = null;
	private String displayName = null;
	private File fileReference;

	public SwatTreeNode(Object userObject, SwatComponentType objectType, File fileReference) {
		super(userObject, false);
		this.objectType = objectType;
		this.fileReference = fileReference;
		setDisplayName();
		setObjectID();
	}

	public File getFileReference() {
		return fileReference;
	}

	//TODO: Display name für AC, etc.
	@SuppressWarnings("rawtypes")
	private void setDisplayName() {
		switch (objectType) {
		case LABELING:
			//TODO:
			break;
		case PETRI_NET:
			displayName = ((AbstractGraphicalPN) getUserObject()).getPetriNet().getName();
			break;
		case PETRI_NET_ANALYSIS:
			displayName = (String) this.getUserObject();
			break;
		case LOG_FILE:
			// userObject is of instance LogFileViewer
			displayName = ((LogModel) this.getUserObject()).getName();
			break;
		case XML_FILE:
			displayName = ((LogModel) this.getUserObject()).getName();

		}
	}

	@SuppressWarnings("rawtypes")
	private void setObjectID() {
		switch (objectType) {
		case LABELING:
			//TODO:
			break;
		case PETRI_NET:
			objectID = ((AbstractGraphicalPN) getUserObject()).getPetriNet().getName();
			break;
		case PETRI_NET_ANALYSIS:
			objectID = (String) this.getUserObject();
			break;
		case LOG_FILE:
			// userObject is of instance LogFileViewer
			objectID = ((LogModel) this.getUserObject()).getName();
			break;
		case XML_FILE:
			objectID = ((LogModel) this.getUserObject()).getName();

		}
	}
	
	public String getObjectID(){
		return objectID;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	/** Update the displayName for JTree if Filename changed **/
	public void updateDisplayName() {
		setDisplayName();
	}

	public SwatComponentType getObjectType() {
		return objectType;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

}
