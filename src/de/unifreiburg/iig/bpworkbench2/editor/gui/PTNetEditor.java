package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.io.File;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNProperties;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PTProperties;

public class PTNetEditor extends PNEditor {

	private static final long serialVersionUID = -5130690639223735136L;

	public PTNetEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	public PTNetEditor(GraphicalPTNet netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PNProperties createPNProperties() {
		return new PTProperties(getNetContainer());
	}

	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		return String.valueOf(((PTFlowRelation) relation).getWeight());
	}

	@Override
	protected PNProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}

}
