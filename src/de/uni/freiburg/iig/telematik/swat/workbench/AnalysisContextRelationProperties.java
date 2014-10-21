package de.uni.freiburg.iig.telematik.swat.workbench;

import de.invation.code.toval.properties.AbstractProperties;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.Validate;

public class AnalysisContextRelationProperties extends AbstractProperties {
	
	//------- Property setting -------------------------------------------------------------
	
	private void setProperty(AnalysisContextRelationProperty contextProperty, Object value){
		props.setProperty(contextProperty.toString(), value.toString());
	}
	
	private String getProperty(AnalysisContextRelationProperty contextProperty){
		return props.getProperty(contextProperty.toString());
	}
	
	//-- Analysis Context name
	
	public void setAnalysisContextName(String name) {
		Validate.notNull(name);
		Validate.notEmpty(name);
		setProperty(AnalysisContextRelationProperty.CONTEXT_NAME, name);
	}
	
	public String getAnalysisContextName() throws PropertyException {
		String propertyValue = getProperty(AnalysisContextRelationProperty.CONTEXT_NAME);
		if(propertyValue == null)
			throw new PropertyException(AnalysisContextRelationProperty.CONTEXT_NAME, propertyValue);
		return propertyValue;
	}
	
	//-- AC Model name
	
	public void setACModelName(String name) {
		Validate.notNull(name);
		Validate.notEmpty(name);
		setProperty(AnalysisContextRelationProperty.ACMODEL_NAME, name);
	}

	public String getACModelName() throws PropertyException {
		String propertyValue = getProperty(AnalysisContextRelationProperty.ACMODEL_NAME);
		if (propertyValue == null)
			throw new PropertyException(AnalysisContextRelationProperty.ACMODEL_NAME, propertyValue);
		return propertyValue;
	}

}
