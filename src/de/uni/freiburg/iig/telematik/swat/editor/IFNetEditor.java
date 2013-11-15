package de.uni.freiburg.iig.telematik.swat.editor;

import java.io.File;

import com.mxgraph.util.mxEventObject;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;

public class IFNetEditor extends AbstractCPNEditor {

	private static final long serialVersionUID = 8612413737377657095L;

	public IFNetEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	public IFNetEditor(GraphicalIFNet netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}
	
	@Override
	protected GraphicalIFNet createNetContainer() {
		return new GraphicalIFNet(new IFNet(), new IFNetGraphics());
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

	@Override
	protected void actOnSelection(Object sender, mxEventObject evt) {
		// TODO Auto-generated method stub
		
	}
	

}
