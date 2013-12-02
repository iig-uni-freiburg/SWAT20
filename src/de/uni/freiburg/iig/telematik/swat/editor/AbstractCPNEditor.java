package de.uni.freiburg.iig.telematik.swat.editor;

import java.io.File;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.swat.editor.properties.CPNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;

public abstract class AbstractCPNEditor extends PNEditor {

	private static final long serialVersionUID = 7463202384539027183L;

	public AbstractCPNEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	@SuppressWarnings("rawtypes")
	public AbstractCPNEditor(AbstractGraphicalCPN netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}

	@Override
	protected PNProperties createPNProperties() {
		//TODO:		return new CPNProperties(getNetContainer());
		return null;
	}

	@SuppressWarnings("rawtypes") 
	protected String getArcConstraint(AbstractFlowRelation relation) {
		// TODO: Do something
		return null;
	}

	@Override
	protected PNProperties getPNProperties() {
		return (CPNProperties) super.getPNProperties();
	}
	
	
}
