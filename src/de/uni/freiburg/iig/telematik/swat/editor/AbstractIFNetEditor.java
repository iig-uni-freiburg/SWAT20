package de.uni.freiburg.iig.telematik.swat.editor;

import java.io.File;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.swat.editor.properties.IFNetProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;

public abstract class AbstractIFNetEditor extends PNEditor {

	private static final long serialVersionUID = 7463202384539027183L;

	public AbstractIFNetEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	@SuppressWarnings("rawtypes")
	public AbstractIFNetEditor(AbstractGraphicalIFNet netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}

	@Override
	protected PNProperties createPNProperties() {
		//TODO:		return new IFNetProperties(getNetContainer());
		return null;
	}

	@SuppressWarnings("rawtypes") 
	protected String getArcConstraint(AbstractFlowRelation relation) {
		// TODO: Do something
		return null;
	}

	@Override
	protected PNProperties getPNProperties() {
		return (IFNetProperties) super.getPNProperties();
	}
	
	
}
