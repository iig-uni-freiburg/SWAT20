package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

public interface LogInformationReader extends InformationReader {
	public List<String> getActivities();
	public List<String> getSubjects();
}
