package de.uni.freiburg.iig.telematik.swat.workbench;

import java.io.File;
import java.util.Random;

/** Holds the model for all SwatComponents **/
public abstract class SwatContainer {

	File fileReference;
	String name;
	long uniqueID;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SwatContainer(File fileReference) {
		this(fileReference, fileReference.getName());
	}

	public SwatContainer(File fileReference, String name) {
		this.fileReference = fileReference;
		this.name = name;
		generateID();
	}

	private void generateID() {
		uniqueID = new Random().nextLong();
	}

	public File getFileReference() {
		return fileReference;
	}

	public void store() {
		System.out.println("Not Implemented");
	}

}
