package de.uni.freiburg.iig.telematik.swat.editor;

import java.io.File;
import java.io.IOException;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AnalysisContextChange;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.menu.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.properties.IFNetProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

public class IFNetEditor extends AbstractIFNetEditor {

	private static final long serialVersionUID = 8612413737377657095L;

	public IFNetEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	public IFNetEditor(GraphicalIFNet netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
		updateAnalysisContextSelection(netContainer);
	}

	private void updateAnalysisContextSelection(GraphicalIFNet netContainer) {
		if (netContainer.getPetriNet().getAnalysisContext() != null) {
			AnalysisContext analysisContext = SwatComponents.getInstance().getAnalysisContext(netContainer.getPetriNet().getName(), netContainer.getPetriNet().getAnalysisContext().getName());
			((mxGraphModel) getGraphComponent().getGraph().getModel()).execute(new AnalysisContextChange(this, analysisContext));
		}
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
		return new IFNetProperties(getNetContainer());

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
		return new IFNetGraphComponent(new IFNetGraph(getNetContainer(), getPNProperties()));

	}

	@Override
	public EditorPopupMenu getPopupMenu() {
		try {
			return new EditorPopupMenu(this);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TransitionPopupMenu getTransitionPopupMenu() {
				try {
					return new TransitionPopupMenu(this);
				} catch (ParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PropertyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;

	}

}
