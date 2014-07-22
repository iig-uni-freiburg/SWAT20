package de.uni.freiburg.iig.telematik.swat.logs;

import java.io.File;

public abstract class AbstractLogModel {

	private File fileReference;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFileReference() {
		return fileReference;
	}

	public AbstractLogModel(File fileReference, String name) {
		this.fileReference = fileReference;
		this.name = name;
	}

	public void setFileReference(File fileReference) {
		this.fileReference = fileReference;
	}

	public AbstractLogModel(File fileReference) {
		this(fileReference, fileReference.getName());
	}

}
