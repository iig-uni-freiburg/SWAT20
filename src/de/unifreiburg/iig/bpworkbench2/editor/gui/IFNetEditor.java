package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.io.File;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNProperties;

public class IFNetEditor extends CPNEditor {

	public IFNetEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	public IFNetEditor(GraphicalIFNet netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}

	@Override
	public GraphicalIFNet getNetContainer() {
		return (GraphicalIFNet) super.getNetContainer();
	}

	@Override
	protected PNProperties createPNProperties() {
		//TODO:		return new IFNetProperties(getNetContainer());
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
		//TODO:		return (IFNetProperties) super.getPNProperties();
		return null;
	}
	

}
