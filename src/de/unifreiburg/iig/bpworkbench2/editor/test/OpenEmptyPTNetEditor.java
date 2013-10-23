package de.unifreiburg.iig.bpworkbench2.editor.test;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.unifreiburg.iig.bpworkbench2.editor.gui.CPNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.gui.CWNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.gui.IFNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.gui.PTNEditor;

public class OpenEmptyPTNetEditor {
	
	public final static String PNML = PTNEditor.class.getResource(
			"/samples/samplePTnet.pnml").getPath();
	public final static String LABELING = PTNEditor.class.getResource(
			"/samples/sampleIFnetLabeling01.xml").getPath();

	public static void main(String[] args) throws IOException, ParserException, ParameterException {
		
		// Create new JFrame
		JFrame frame = new JFrame();
		JPanel panel = null;
		frame.setTitle("PTNet Editor");
		frame.setSize(450, 300);

		/*
		 * LOAD PNML-EDITOR parser partially finished, you may create an empty
		 * editor with "null", or an Editor to Load a PT-Net oder CPN by
		 * inserting its File-Object
		 */
		// panel = new PNMLEditor(null);
		String filePath = PTNEditor.class.getResource(
				"/samples/samplePTnet.pnml").getPath();
		System.out.println(filePath);
		
//		/*
//		 * PetriNet
//		 */
		AbstractGraphicalPN<?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML,
				true, false);
//		AbstractPetriNet<?, ?, ?, ?, ?> petriNet = netContainer.getPetriNet();
		AbstractPetriNet<?, ?, ?, ?, ?> petriNet = new PTNet();
//		ptNet.addPlace("place1");
//		ptNet.addPlace("place2");
//		ptNet.addTransition("trans1");
//		ptNet.addFlowRelationPT("place1", "trans1");
//		ptNet.addFlowRelationTP("trans1", "place2");
//		try {
//			ptNet.checkSoundness();
//		} catch (PNValidationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PNSoundnessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PTGraphics ptNetGraphics = new PTGraphics();
//		ptNetGraphics.setPlaceGraphics(placeGraphics);
		
//		GraphicalPTNet netContainer = new GraphicalPTNet(new PTNet(), new PTGraphics());
		
		//distinguish between different net-types
		if (petriNet instanceof PTNet) {
			panel = new PTNEditor(null);

		}

		if (petriNet instanceof CPN) {
			panel = new CPNEditor(netContainer);
		}

		if (petriNet instanceof CWN) {
			panel = new CWNEditor(netContainer);

		}

		if (petriNet instanceof IFNet) {
			panel = new IFNEditor(netContainer);
		}
		
		
		// set background black
		panel.setBackground(Color.black);

		// add Editor to JFrame
		frame.add(panel);

		// show all
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}