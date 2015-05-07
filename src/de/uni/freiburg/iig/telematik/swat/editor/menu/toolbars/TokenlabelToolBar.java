package de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.SwatIFNetEditorComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TokenSecurityLevelChange;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CirclePanel;

public class TokenlabelToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private PNEditorComponent editor;

	private Map<String, Color> colors;

	private JPanel panel;

	private PNGraph graph;

	private Labeling labeling;

	private String tokenLabel;

	public TokenlabelToolBar(final PNEditorComponent pnEditor, int horizontal) {
		Validate.notNull(pnEditor);
		this.editor = pnEditor;
		panel = new JPanel();
		panel.setLayout(new SpringLayout());
		graph = (PNGraph) editor.getGraphComponent().getGraph();
		setFloatable(false);
		updateView();
	}

	private void addRow(final String tokenLabel) {

		Color tokenColor = colors.get(tokenLabel);
		CirclePanel circle = null;
		try {
			circle = new CirclePanel(tokenColor);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(editor.getGraphComponent(), "Circle-Panel could not be created. \nReason: "+ e.getMessage(), e.getClass().toString(), JOptionPane.ERROR);
		}

		panel.add(circle);
		panel.add(new JLabel(tokenLabel));
		panel.add(new JLabel(":  "));
		if (tokenLabel != "black") {

			String[] secLevels = { SecurityLevel.LOW.toString(), SecurityLevel.HIGH.toString() };
			JComboBox combo = new JComboBox(secLevels);
			if (labeling != null) {

				switch (labeling.getAttributeClassification(tokenLabel)) {
				case HIGH:
					combo.setSelectedIndex(1);
					break;
				case LOW:
					combo.setSelectedIndex(0);
					break;
				}

			}
			ItemListener itemListener = new ItemListener() {
				public void itemStateChanged(ItemEvent itemEvent) {
					int state = itemEvent.getStateChange();

					if ((state == ItemEvent.SELECTED)) {
						((mxGraphModel) graph.getModel()).beginUpdate();
						//TODO: Adapt to new ACStructure 
//						if(!labeling.getAttributes().contains(tokenLabel)){
//							AnalysisContext ac = ((IFNetGraph)graph).getCurrentAnalysisContext();
//							Set<String> attributes = labeling.getAttributes();
//							
//							 HashMap<String, SecurityLevel> newAttributeMapping = new HashMap<String, SecurityLevel>();
//							for(String a:attributes){
//								newAttributeMapping.put(a, labeling.getAttributeClassification(a));
//							}
//							newAttributeMapping.put(tokenLabel, SecurityLevel.LOW);
//							//FIXME:
////							labeling.
////							labeling.addAttributes(attributes);
////							Labeling newLabeling = new Labeling(labeling.getActivities(), newAttributeMapping.keySet(), labeling.getSubjects());
////							for(String a:newAttributeMapping.keySet()){
////								newLabeling.setAttributeClassification(a, newAttributeMapping.get(a));}
//							
////							((mxGraphModel) graph.getModel()).execute(new AnalysisContextChange(editor,new AnalysisContext(newLabeling)));
//
//						}
					
						if (itemEvent.getItem().equals(SecurityLevel.LOW.toString()))
							((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenSecurityLevelChange((SwatIFNetEditorComponent) editor, tokenLabel, SecurityLevel.LOW));
						if (itemEvent.getItem().equals(SecurityLevel.HIGH.toString()))
							((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenSecurityLevelChange((SwatIFNetEditorComponent) editor, tokenLabel, SecurityLevel.HIGH));
						}
					((mxGraphModel) graph.getModel()).endUpdate();
						
				}
			};
			combo.addItemListener(itemListener);

			if (labeling == null)
				combo.setEnabled(false);
			panel.add(combo);

		} else {
			JComboBox cf = new JComboBox();
			cf.addItem("Control Flow");
			panel.add(cf);
			cf.setEnabled(false);
		}

	}

	public void updateView() {
		panel.removeAll();
		panel.add(new JLabel("Token"));
		panel.add(new JLabel("Label"));
		panel.add(Box.createGlue());
		panel.add(new JLabel("Security Level"));
		colors = ((AbstractCPNGraphics)graph.getNetContainer().getPetriNetGraphics()).getColors();
		graph = (PNGraph) editor.getGraphComponent().getGraph();
		if (getTokenColors().contains("black"))
			colors.put("black", Color.BLACK);
		if(graph instanceof IFNetGraph){
			//TODO: Adapt to new ACStructure 
//		if (((IFNetGraph)graph).getCurrentAnalysisContext() != null) {
//			labeling = ((IFNetGraph)graph).getCurrentAnalysisContext().getLabeling();
//		}
		}
		int size = 0;
		Set<String> colorsTemp = ((AbstractCPN)graph.getNetContainer().getPetriNet()).getTokenColors();
		for (String colorName : colors.keySet()) {
			addRow(colorName);
			size++;
		}

		SpringUtilities.makeCompactGrid(panel, size + 1, 4, 6, 6, 6, 6);
		add(panel);
		setVisible(false);
		setVisible(true);
	}

	protected Set<String> getTokenColors() {
		AbstractCPN net = (AbstractCPN) editor.getGraphComponent().getGraph().getNetContainer().getPetriNet();
		return net.getTokenColors();
	}
}
