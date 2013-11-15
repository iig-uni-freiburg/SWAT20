package de.unifreiburg.iig.bpworkbench2.editor.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView;
import de.unifreiburg.iig.bpworkbench2.editor.PNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.PTNetEditor;

public class PTNetEditorTests {
	
	public final static String PNML = PNEditor.class.getResource("/samples/samplePTnet.pnml").getPath();
	public final static String LABELING = PNEditor.class.getResource(
			"/samples/sampleIFnetLabeling01.xml").getPath();

	public static void main(String[] args) throws IOException, ParserException, ParameterException {
//		testEmptyNet();
//		openSampleNet();
		openSampleNetWithProperties();
	}
	
	private static void testEmptyNet() throws ParameterException{
		String userHome = System.getProperty("user.home");
		File file = new File(userHome + "test");
//		new DisplayFrame(new PTNetEditor(file), true);
		JFrame frame = new JFrame();
		JPanel panel = new PTNetEditor(file);
		frame.add(panel);
		frame.setTitle("PTNet Editor");
		frame.setSize(450, 300);
		
		// set background black
		panel.setBackground(Color.black);

		// add Editor to JFrame
		frame.add(panel);

		// show all
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	private static void openSampleNet() throws IOException, ParserException, ParameterException {
		// Create new JFrame
		JFrame frame = new JFrame();
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML);

//		GraphicalPTNet graphicalPTNet = (GraphicalPTNet) netContainer;
//		for (PTFlowRelation relation : graphicalPTNet.getPetriNet().getFlowRelations()) {
//			System.out.println("NAME:" + relation.getName() + "\nWEIGHT:" + relation.getWeight());
//			System.out.println(graphicalPTNet.getPetriNet().getFlowRelation(relation.getName()));
////			System.out.println(graphicalPTNet.getPetriNet().getFlowRelation(relation.getName()).getWeight());
//		}

		JPanel panel = new PTNetEditor(((GraphicalPTNet) netContainer), new File(PNML));
		frame.setTitle("PTNet Editor");
		frame.setSize(450, 300);

		// set background black
		panel.setBackground(Color.black);

		// add Editor to JFrame
		frame.add(panel);

		// show all
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	private static void openSampleNetWithProperties() throws IOException, ParserException, ParameterException {
		// Create new JFrame
		JFrame frame = new JFrame();
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML, false,false);


		// GraphicalPTNet graphicalPTNet = (GraphicalPTNet) netContainer;
		// for (PTFlowRelation relation : graphicalPTNet.getPetriNet().getFlowRelations()) {
		// System.out.println("NAME:" + relation.getName() + "\nWEIGHT:" + relation.getWeight());
		// System.out.println(graphicalPTNet.getPetriNet().getFlowRelation(relation.getName()));
		//// System.out.println(graphicalPTNet.getPetriNet().getFlowRelation(relation.getName()).getWeight());
		// }


		JPanel panel = new PTNetEditor(((GraphicalPTNet) netContainer), new File(PNML));
		PropertiesView pV = ((PNEditor) panel).getPropertiesView();
		frame.setTitle("PTNet Editor");
		frame.setSize(450, 300);


		// set background black
		panel.setBackground(Color.black);
		frame.setLayout(new BorderLayout());
		// add Editor to JFrame
		frame.getContentPane().add(panel, BorderLayout.LINE_START);
		frame.getContentPane().add(pV, BorderLayout.LINE_END);
		// frame.add(pV);
		// frame.add(panel);


		// show all
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		}
	
	
}