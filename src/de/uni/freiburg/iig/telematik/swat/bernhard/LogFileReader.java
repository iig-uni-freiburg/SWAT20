package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

public interface LogFileReader extends InformationReader {
	public List<String> getSubjects();
	public String[] getSubjectArray();
}
