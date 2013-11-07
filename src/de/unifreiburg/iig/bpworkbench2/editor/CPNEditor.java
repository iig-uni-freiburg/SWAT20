package de.unifreiburg.iig.bpworkbench2.editor;

import java.io.File;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNProperties;

public class CPNEditor extends PNEditor {

	private static final long serialVersionUID = 7463202384539027183L;

	public CPNEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	public CPNEditor(AbstractGraphicalCPN netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}

	@Override
	public AbstractGraphicalCPN getNetContainer() {
		return (AbstractGraphicalCPN) super.getNetContainer();
	}

	@Override
	protected PNProperties createPNProperties() {
		//TODO:		return new CPNProperties(getNetContainer());
		return null;
	}

	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		// TODO: Do something
		return null;
	}

	@Override
	protected PNProperties getPNProperties() {
		//TODO:		return (CPNProperties) super.getPNProperties();
		return null;
	}
	
	
}
