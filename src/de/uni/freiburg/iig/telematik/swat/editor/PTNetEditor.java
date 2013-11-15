package de.uni.freiburg.iig.telematik.swat.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraphSelectionModel;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PTGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PTGraphComponent;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PTProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeModel;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreeNode;
import de.uni.freiburg.iig.telematik.swat.editor.tree.PNTreePath;

public class PTNetEditor extends PNEditor {

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
			return new PTGraphComponent(new PTGraph(getNetContainer(), getPNProperties()), this);
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
			e.printStackTrace();
		}
		return null;
	}
	
	  private TreePath find(DefaultMutableTreeNode root, String s) {
		    @SuppressWarnings("unchecked")
		    Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		    while (e.hasMoreElements()) {
		        DefaultMutableTreeNode node = e.nextElement();
		        if (node.toString().equalsIgnoreCase(s)) {
		        	DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(0);
		            return new TreePath(child.getPath());
		        }
		    }
		    return null;
		} 

	@Override
	protected void actOnSelection(Object sender, mxEventObject evt) {
		System.out.println("ROWS:" + getPropertiesView().getTree().getRowCount());
		for (int i = getPropertiesView().getTree().getRowCount(); i > 0; i--) {
			getPropertiesView().getTree().collapseRow(i);
		}
		if (((mxGraphSelectionModel) sender).getCell() instanceof PNGraphCell) {
			PNGraphCell cell = (PNGraphCell) ((mxGraphSelectionModel) sender).getCell();
			TreePath path = find((DefaultMutableTreeNode) getPropertiesView().getTree().getModel().getRoot(), cell.getId());
			getPropertiesView().getTree().setSelectionPath(path);

		}
	}
	

	
	public final static String PNML = PNEditor.class.getResource("/samples/samplePTnet.pnml").getPath();
	public final static String LABELING = PNEditor.class.getResource("/samples/sampleIFnetLabeling01.xml").getPath();

	public static void main(String[] args) throws IOException, ParserException, ParameterException {
		JFrame frame = new JFrame();
		// testEmptyNet();
		// openSampleNet();
		openSampleNetWithProperties(frame);
		
		// show all
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void testEmptyNet(JFrame frame) throws ParameterException {
		String userHome = System.getProperty("user.home");
		File file = new File(userHome + "test");
		JPanel panel = new PTNetEditor(file);
		frame.add(panel);

	}

	private static void openSampleNet(JFrame frame) throws IOException, ParserException, ParameterException {
		
		JPanel panel = createFrame(new JFrame());
		frame.add(panel);
	}

	private static void openSampleNetWithProperties(JFrame frame) throws IOException, ParserException, ParameterException {
		JPanel panel = createFrame(frame);
		PropertiesView pV = ((PNEditor) panel).getPropertiesView();
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getContentPane().add(pV, BorderLayout.LINE_END);

	}
	
	public static JPanel createFrame(JFrame frame) throws IOException, ParserException, ParameterException {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(PNML, false,false);
		JPanel panel = new PTNetEditor(((GraphicalPTNet) netContainer), new File(PNML));
		frame.setTitle("PTNet Editor");
		frame.setSize(800, 500);
		panel.setBackground(Color.black);
		return panel;
	}
	
	


}


