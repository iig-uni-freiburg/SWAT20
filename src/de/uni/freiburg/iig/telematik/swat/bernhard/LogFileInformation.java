package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;

public class LogFileInformation implements LogFileReader {

	private LogFileViewer logViewer;
	private List<String> activities, subjects, roles;
	public LogFileInformation(LogFileViewer logViewer) {
		this.logViewer = logViewer;
		activities=new ArrayList<String>();
		subjects=new ArrayList<String>();
		roles=new ArrayList<String>();
		update();
	}

	@Override
	public List<String> getActivities() {
		// TODO Auto-generated method stub
		return activities;
	}

	@Override
	public String[] getActivitiesArray() {
		// TODO Auto-generated method stub
		return activities.toArray(new String[activities.size()]);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		//logViewer.get
	}

	@Override
	public List<String> getSubjects() {
		// TODO Auto-generated method stub
		return subjects;
	}

	@Override
	public String[] getSubjectArray() {
		// TODO Auto-generated method stub
		return subjects.toArray(new String[subjects.size()]);
	}

	@Override
	public String[] getRoleArray() {
		// TODO Auto-generated method stub
		return roles.toArray(new String[roles.size()]);
	}

}
