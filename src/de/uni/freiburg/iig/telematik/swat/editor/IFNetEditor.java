package de.uni.freiburg.iig.telematik.swat.editor;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.menu.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.properties.IFNetProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;

public class IFNetEditor extends AbstractIFNetEditor {

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
		try {
			return new IFNetProperties(getNetContainer());
		} catch (ParameterException e) {
			// Should not happen, since getNetContainer never returns null;
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		// TODO: Do something
		return null;
	}

	@Override
	protected IFNetProperties getPNProperties() {
		return (IFNetProperties) super.getPNProperties();

	}

	@Override
	protected PNGraphComponent createGraphComponent() {
		try {
			return new IFNetGraphComponent(new IFNetGraph(getNetContainer(), getPNProperties()));
		} catch (ParameterException e) {
			// Should not happen, since getNetContainer() and getPNProperties() never return null;
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public EditorPopupMenu getPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransitionPopupMenu getTransitionPopupMenu() {
		try {
			try {
				return new TransitionPopupMenu(this);
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return null;
	}

}
