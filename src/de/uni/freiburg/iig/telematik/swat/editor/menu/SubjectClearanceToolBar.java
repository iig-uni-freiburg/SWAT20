package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AnalysisContextChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.SubjectSecurityLevelChange;

public class SubjectClearanceToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private PNEditor editor;

	private JPanel panel;

	private IFNetGraph graph;

	private Labeling labeling;

	public SubjectClearanceToolBar(final PNEditor pnEditor, int horizontal) throws ParameterException {
		Validate.notNull(pnEditor);
		this.editor = pnEditor;
		panel = new JPanel();
		panel.setLayout(new SpringLayout());
		graph = (IFNetGraph) editor.getGraphComponent().getGraph();
		setFloatable(false);
		updateView();

	}

	private void addRow(final String subjectDescriptor) {

		panel.add(new JLabel(subjectDescriptor));
		panel.add(new JLabel(":  "));
		if (subjectDescriptor != "black") {

			String[] secLevels = { SecurityLevel.LOW.toString(), SecurityLevel.HIGH.toString() };
			JComboBox combo = new JComboBox(secLevels);
			if (labeling != null) {

				switch (labeling.getSubjectClearance(subjectDescriptor)) {
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
						if(!labeling.getSubjects().contains(subjectDescriptor)){
							AnalysisContext ac = graph.getCurrentAnalysisContext();
							Set<String> subjects = labeling.getSubjects();
							
							 HashMap<String, SecurityLevel> newSubjectMapping = new HashMap<String, SecurityLevel>();
							for(String s:subjects){
								newSubjectMapping.put(s, labeling.getSubjectClearance(s));
							}
							newSubjectMapping.put(subjectDescriptor, SecurityLevel.LOW);
							//FIXME:
//							labeling.addSubjects(subjects);
//							Labeling newLabeling = new Labeling(labeling.getActivities(), newSubjectMapping.keySet(), labeling.getSubjects());
//							for(String s:newSubjectMapping.keySet()){
//								newLabeling.setSubjectClearance(s, newSubjectMapping.get(s));}
							
//							((mxGraphModel) graph.getModel()).execute(new AnalysisContextChange(editor,new AnalysisContext(newLabeling)));

						}
					
						if (itemEvent.getItem().equals(SecurityLevel.LOW.toString()))
							((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new SubjectSecurityLevelChange(editor, subjectDescriptor, SecurityLevel.LOW));
						if (itemEvent.getItem().equals(SecurityLevel.HIGH.toString()))
							((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new SubjectSecurityLevelChange(editor, subjectDescriptor, SecurityLevel.HIGH));
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
//		panel.add(new JLabel("Subject"));
		panel.add(new JLabel("Subject"));
		panel.add(Box.createGlue());
		panel.add(new JLabel("Security Level"));
		graph = (IFNetGraph) editor.getGraphComponent().getGraph();

		if (graph.getCurrentAnalysisContext() != null) {
			labeling = graph.getCurrentAnalysisContext().getLabeling();
		}
		int size = 0;
		if(labeling != null)
		for (String subject : labeling.getSubjects()) {
			addRow(subject);
			size++;
		}

		SpringUtilities.makeCompactGrid(panel, size + 1, 3, 6, 6, 6, 6);
		add(panel);
		setVisible(false);
		setVisible(true);
	}

}
