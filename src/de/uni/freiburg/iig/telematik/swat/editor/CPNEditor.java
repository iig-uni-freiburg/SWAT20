package de.uni.freiburg.iig.telematik.swat.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.swat.editor.graph.CPNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.CPNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.menu.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.properties.CPNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView;

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
		try {
			return new CPNProperties(getNetContainer());
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
	protected CPNProperties getPNProperties() {
		return (CPNProperties) super.getPNProperties();
	}

	@Override
	protected PNGraphComponent createGraphComponent() {
		try {
			return new CPNGraphComponent(new CPNGraph(getNetContainer(), getPNProperties()));
		} catch (ParameterException e) {
			// Should not happen, since getNetContainer() and getPNProperties() never return null;
			e.printStackTrace();
		}
		return null;
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
	
//	public final static String PNML = PNEditor.class.getResource("/samples/sampleCPnet.pnml").getPath();
//	public final static String LABELING = PNEditor.class.getResource("/samples/sampleIFnetLabeling01.xml").getPath();


	private static void testEmptyNet(JFrame frame) throws IOException, ParserException, ParameterException {
		JPanel panel = createFrameEpmtyNet(frame);
		PropertiesView pV = ((PNEditor) panel).getPropertiesView();
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getContentPane().add(pV, BorderLayout.LINE_END);

	}


//	private static void openSampleNetWithProperties(JFrame frame) throws IOException, ParserException, ParameterException {
//		JPanel panel = createFrame(frame);
//		PropertiesView pV = ((PNEditor) panel).getPropertiesView();
//		frame.setLayout(new BorderLayout());
//		frame.getContentPane().add(panel, BorderLayout.CENTER);
//		frame.getContentPane().add(pV, BorderLayout.LINE_END);
//
//	}
//	

//	public static JPanel createFrame(JFrame frame) throws IOException, ParserException, ParameterException {
//		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML, false,false);
//		JPanel panel = new CPNEditor(((GraphicalCPN) netContainer), new File(PNML));
//		frame.setTitle("CPNet Editor");
//		frame.setSize(800, 500);
//		panel.setBackground(Color.black);
//		return panel;
//	}

	
	public static JPanel createFrameEpmtyNet(JFrame frame) throws IOException, ParserException, ParameterException {
		String userHome = System.getProperty("user.home");
		File file = new File(userHome + "test");
		JPanel panel = new PTNetEditor(file);
		frame.setTitle("PTNet Editor");
		frame.setSize(800, 500);
		panel.setBackground(Color.black);
		return panel;
	}

	@Override
	public TransitionPopupMenu getTransitionPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}


//	public static void main(String[] args) throws IOException, ParserException, ParameterException {
//		JFrame frame = new JFrame();
////		 testEmptyNet(frame);
//		openSampleNetWithProperties(frame);
//		
//		// show all
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}


	
}
