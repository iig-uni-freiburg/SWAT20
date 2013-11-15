package de.uni.freiburg.iig.telematik.swat.editor;

import java.io.File;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;

public class CPNEditor extends AbstractCPNEditor {

	private static final long serialVersionUID = 7463202384539027183L;

	public CPNEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	public CPNEditor(GraphicalCPN netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}

	@Override
	public GraphicalCPN getNetContainer() {
		return (GraphicalCPN) super.getNetContainer();
	}
	
	@Override
	public GraphicalCPN createNetContainer() {
		return new GraphicalCPN(new CPN(), new CPNGraphics());
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

	@Override
	protected PNGraphComponent createGraphComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EditorPopupMenu getPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
