package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLIFNetLabelingParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.Labeling;
import de.unifreiburg.iig.bpworkbench2.editor.gui.PNMLEditor;

/**
 * @author julius
 *
 */
/**
 * @author julius
 *
 */
public class ParserApp {

	// public final static String PNML =
	// "C:\Users\julius\petrineteditor_wb\SWAT20\xml\samples\sampleIFnet.pnml";
	// public final static String LABELING =
	// "C:\\Users\\Adrian\\Desktop\\grammar-v1.0\\samples\\sampleIFnetLabeling01.xml";

	public final static String PNML = PNMLEditor.class.getResource(
			"/samples/sampleIFnet.pnml").getPath();

	public final static String LABELING = PNMLEditor.class.getResource(
			"/samples/sampleIFnetLabeling01.xml").getPath();

	public static void main(String[] args) throws IOException, ParserException,
			ParameterException {

		/*
		 * PetriNet
		 */
		AbstractGraphicalPN<?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML,
				true, false);

		// System.out.println(n.getPetriNet());
		// System.out.println(n.getPetriNetGraphics());

		// iterate flow relations
		// for (Object oFlowRelation : n.getPetriNet().getFlowRelations()) {
		//
		// IFNetFlowRelation ifFlowRelation = null;
		// if (oFlowRelation instanceof IFNetFlowRelation) {
		// ifFlowRelation = (IFNetFlowRelation) oFlowRelation;
		// AbstractPNNode<?> sourceNode = ifFlowRelation.getSource();
		//
		// //pic for example the source node
		// System.out.println(sourceNode);
		// if (sourceNode instanceof IFNetPlace)
		// System.out.println(n.getPetriNetGraphics().getPlaceGraphics()
		// .get(sourceNode));
		// //--> null
		// System.out.println(n.getPetriNetGraphics().getPlaceGraphics());
		// //--> {p2[end]=[Position( 100.0 / 60.0 )], p1[ready]=[Position( 20.0
		// / 20.0 )]}
		//
		// }
		// }
		for (AbstractFlowRelation<?, ?, ?> oFlowRelation : netContainer
				.getPetriNet().getFlowRelations()) {
			//liste mit schon eryeugen nodes
			AbstractPNNode<?> sourceNode = oFlowRelation.getSource();
			AbstractPNNode<?> targetNode = oFlowRelation.getTarget();

			NodeGraphics nodeGraphics = getNodeGraphics(netContainer,
					sourceNode);

			nodeGraphics = getNodeGraphics(netContainer, targetNode);

		}

		/*
		 * Labeling
		 */
		Labeling l = PNMLIFNetLabelingParser.parse(LABELING, false);
		AnalysisContext ac = new AnalysisContext(l);
		((IFNet) netContainer.getPetriNet()).setAnalysisContext(ac);

		// System.out.println(l);
	}

	
	/**
	 * gets the NodeGraphicAttribute of the given Node. 
	 * 
	 * @param graphicalNet
	 * @param node
	 * @return nodeGraphics 
	 */
	private static NodeGraphics getNodeGraphics(
			AbstractGraphicalPN<?, ?, ?, ?, ?> graphicalNet,
			AbstractPNNode<?> node) {
		NodeGraphics nodeGraphics = null;
		Map<?, NodeGraphics> graphics = null;

		if (node instanceof AbstractPlace)
			graphics = graphicalNet.getPetriNetGraphics().getPlaceGraphics();
		if (node instanceof AbstractTransition)
			graphics = graphicalNet.getPetriNetGraphics()
					.getTransitionGraphics();

		for (Entry<?, ?> place : graphics.entrySet()) {
			if (place.getKey() == node)
				nodeGraphics = (NodeGraphics) place.getValue();
		}
		return nodeGraphics;
	}
}