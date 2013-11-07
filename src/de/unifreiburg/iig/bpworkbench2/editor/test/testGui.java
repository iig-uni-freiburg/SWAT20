package de.unifreiburg.iig.bpworkbench2.editor.test;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.unifreiburg.iig.bpworkbench2.editor.CPNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.IFNetEditor;
import de.unifreiburg.iig.bpworkbench2.editor.PNEditor;

public class testGui {
	
	public final static String PNML = PNEditor.class.getResource(
			"/samples/samplePTnet2.pnml").getPath();
	public final static String LABELING = PNEditor.class.getResource(
			"/samples/sampleIFnetLabeling01.xml").getPath();

	public static void main(String[] args) throws IOException, ParserException, ParameterException {
		
		// Create new JFrame
		JFrame frame = new JFrame();
		JPanel panel = null;
		frame.setTitle("PNML Editor");
		frame.setSize(450, 300);

		/*
		 * LOAD PNML-EDITOR parser partially finished, you may create an empty
		 * editor with "null", or an Editor to Load a PT-Net oder CPN by
		 * inserting its File-Object
		 */
		// panel = new PNMLEditor(null);
		String filePath = PNEditor.class.getResource(
				"/samples/samplePTnet2.pnml").getPath();
		System.out.println(filePath);
		
		/*
		 * PetriNet
		 */
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML,
				true, false);
		AbstractPetriNet<?, ?, ?, ?, ?> petriNet = netContainer.getPetriNet();
System.out.println(filePath);
//		PNSerialization.serialize(netContainer, PNSerializationFormat.PNML, "/C:/Users/julius/petrineteditor_wb/SWAT20/xml/samples/samplePTnet3.pnml");
		
		//distinguish between different net-types
//		if (petriNet instanceof PTNet) {
//			panel = new PNEditor(netContainer);
//
//		}
//
//		if (petriNet instanceof CPN) {
//			panel = new CPNEditor(netContainer);
//		}
//
//		if (petriNet instanceof CWN) {
//			panel = new CWNEditor(netContainer);
//
//		}
//
//		if (petriNet instanceof IFNet) {
//			panel = new IFNetEditor(netContainer);
//		}
		
		
		// set background black
		panel.setBackground(Color.black);

		// add Editor to JFrame
		frame.add(panel);

		// show all
		frame.setVisible(true);
		

	}
}