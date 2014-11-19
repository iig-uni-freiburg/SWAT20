package de.uni.freiburg.iig.telematik.swat.workbench.action;


import java.awt.Window;

import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


public abstract class AbstractWorkingDirectoryAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 6658565129248580915L;

	public static final String PROPERTY_NAME_WORKING_DIRECTORY = "workingDirectory";
	public static final String PROPERTY_NAME_SUCCESS = "success";
	
	protected Window parent = null;
	
	public AbstractWorkingDirectoryAction(Window parentWindow, String name){
		super(name);
		this.parent = parentWindow;
	}
	
	protected void addKnownWorkingDirectory(String workingDirectory, boolean createSubfolders) throws Exception {
		SwatProperties.getInstance().addKnownWorkingDirectory(workingDirectory, createSubfolders);
		SwatProperties.getInstance().setWorkingDirectory(workingDirectory, true);
		SwatProperties.getInstance().store();
		putValue(PROPERTY_NAME_WORKING_DIRECTORY, workingDirectory);
		putValue(PROPERTY_NAME_SUCCESS, true);
	}

}
