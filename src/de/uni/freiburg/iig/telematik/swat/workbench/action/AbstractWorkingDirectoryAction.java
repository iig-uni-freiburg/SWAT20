package de.uni.freiburg.iig.telematik.swat.workbench.action;


import java.awt.Window;
import java.io.IOException;

import javax.swing.AbstractAction;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatProperties;


public abstract class AbstractWorkingDirectoryAction extends AbstractAction {

	private static final long serialVersionUID = 6658565129248580915L;

	public static final String PROPERTY_NAME_WORKING_DIRECTORY = "workingDirectory";
	public static final String PROPERTY_NAME_SUCCESS = "success";
	
	protected Window parent = null;
	
	public AbstractWorkingDirectoryAction(Window parentWindow, String name){
		super(name);
		this.parent = parentWindow;
	}
	
	protected void addKnownWorkingDirectory(String workingDirectory) throws PropertyException{
		try {
			SwatProperties.getInstance().addKnownWorkingDirectory(workingDirectory);
			SwatProperties.getInstance().setWorkingDirectory(workingDirectory);
			SwatProperties.getInstance().store();
		} catch (ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        putValue(PROPERTY_NAME_WORKING_DIRECTORY, workingDirectory);
        putValue(PROPERTY_NAME_SUCCESS, true);
	}

}