package de.uni.freiburg.iig.telematik.swat.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.swat.editor.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.swat.editor.graph.CPNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.CPNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.menu.popup.TransitionPopupMenu;
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
			return new CPNProperties(getNetContainer());
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
			return new CPNGraphComponent(new CPNGraph(getNetContainer(), getPNProperties()));
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

	private static void testEmptyNet(JFrame frame) throws IOException, ParserException, ParameterException {
		JPanel panel = createFrameEpmtyNet(frame);
		PropertiesView pV = ((PNEditor) panel).getPropertiesView();
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getContentPane().add(pV, BorderLayout.LINE_END);

	}

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
	
	@Override
	protected ToolBar createNetSpecificToolbar() throws EditorToolbarException {
		return new ToolBar(this, JToolBar.HORIZONTAL);
	}

}
