package de.uni.freiburg.iig.telematik.swat.logs;

import java.io.File;

public class LogModel {

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

	public LogModel(File fileReference, String name) {
		this.fileReference = fileReference;
		this.name = name;
	}

	public void setFileReference(File fileReference) {
		this.fileReference = fileReference;
	}

	public LogModel(File fileReference) {
		this(fileReference, fileReference.getName());
	}

}
