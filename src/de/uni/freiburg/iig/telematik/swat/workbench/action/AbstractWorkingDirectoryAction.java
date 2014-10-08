package de.uni.freiburg.iig.telematik.swat.workbench.action;


import java.awt.Window;
import java.io.IOException;

import javax.swing.AbstractAction;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


public abstract class AbstractWorkingDirectoryAction extends AbstractAction {

	private static final long serialVersionUID = 6658565129248580915L;

	public static final String PROPERTY_NAME_WORKING_DIRECTORY = "workingDirectory";
	public static final String PROPERTY_NAME_SUCCESS = "success";
	
	protected Window parent = null;
	
	public AbstractWorkingDirectoryAction(Window parentWindow, String name){
		super(name);
		this.parent = parentWindow;
	}
	
	protected void addKnownWorkingDirectory(String workingDirectory) {


			try {
				SwatProperties.getInstance().addKnownWorkingDirectory(workingDirectory, false);
				SwatProperties.getInstance().setWorkingDirectory(workingDirectory, false);
				SwatProperties.getInstance().store();
			} catch (SwatComponentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        putValue(PROPERTY_NAME_WORKING_DIRECTORY, workingDirectory);
        putValue(PROPERTY_NAME_SUCCESS, true);
	}

}
