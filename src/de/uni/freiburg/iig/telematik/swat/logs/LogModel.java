package de.uni.freiburg.iig.telematik.swat.logs;

import java.io.File;

import de.invation.code.toval.file.FileUtils;

public class LogModel {

	private File fileReference;
	private String name;
	private SwatLog type = null;
	
	public LogModel(File fileReference, SwatLog type) {
		super();
		setFileReference(fileReference);
		setName(FileUtils.getName(fileReference));
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFileReference() {
		return fileReference;
	}

	public void setFileReference(File fileReference) {
		this.fileReference = fileReference;
	}

	public SwatLog getType() {
		return type;
	}
}
