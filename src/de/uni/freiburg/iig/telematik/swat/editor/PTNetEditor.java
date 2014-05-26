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
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PTGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PTGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.menu.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PTProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView;

public class PTNetEditor extends PNEditor {
	
//	public final static String PNML = PNEditor.class.getResource("/samples/samplePTnet.pnml").getPath();
//	public final static String LABELING = PNEditor.class.getResource("/samples/sampleIFnetLabeling01.xml").getPath();

	private static final long serialVersionUID = -5130690639223735136L;

	public PTNetEditor(File fileReference) throws ParameterException {
		super(fileReference);
	}

	public PTNetEditor(GraphicalPTNet netContainer, File fileReference) throws ParameterException {
		super(netContainer, fileReference);
	}

	@Override
	protected GraphicalPTNet createNetContainer() {
		return new GraphicalPTNet(new PTNet(), new PTGraphics());
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PTProperties createPNProperties() {
		try {
			return new PTProperties(getNetContainer());
		} catch (ParameterException e) {
			// Should not happen, since getNetContainer never returns null;
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected PTProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}

	@Override
	protected PNGraphComponent createGraphComponent() {
		try {
			return new PTGraphComponent(new PTGraph(getNetContainer(), getPNProperties()));
		} catch (ParameterException e) {
			// Should not happen, since getNetContainer() and getPNProperties() never return null;
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public EditorPopupMenu getPopupMenu() {
		try {
			try {
				return new EditorPopupMenu(this);
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
	

//	public static JPanel createFrame(JFrame frame) throws IOException, ParserException, ParameterException {
//		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML, false,false);
//		JPanel panel = new PTNetEditor(((GraphicalPTNet) netContainer), new File(PNML));
//		frame.setTitle("PTNet Editor");
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

	
//	public static void main(String[] args) throws IOException, ParserException, ParameterException {
//		JFrame frame = new JFrame();
////		 testEmptyNet(frame);
//		openSampleNetWithProperties(frame);
//		
//		// show all
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
//		frame.setVisible(true);
//	}

}


