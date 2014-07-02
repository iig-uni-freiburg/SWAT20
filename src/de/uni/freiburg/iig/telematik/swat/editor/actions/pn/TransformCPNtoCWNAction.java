package de.uni.freiburg.iig.telematik.swat.editor.actions.pn;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCWN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CWNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.ConstraintChange;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class TransformCPNtoCWNAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public TransformCPNtoCWNAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "CheckSoundness", IconFactory.getIcon("cwn_transform"));
	}

	public void actionPerformed(ActionEvent e) {
		if (editor != null) {
			AbstractPetriNet pn = editor.getNetContainer().getPetriNet();
			if (pn instanceof CPN) {
				CPN cpn = (CPN) pn;
				CWN cwn = new CWN();

				CWNMarking marking = new CWNMarking();
				CPNMarking cpnMarking = cpn.getInitialMarking();

				Set<String> places = new HashSet<String>();
				for (CPNPlace p : cpn.getPlaces()) {
					places.add(p.getName());
					if (cpnMarking.get(p.getName()) != null)
						marking.set(p.getName(), cpnMarking.get(p.getName()));
				}

				Set<String> transitions = new HashSet<String>();
				for (CPNTransition t : cpn.getTransitions())
					transitions.add(t.getName());

				cwn = new CWN(places, transitions, marking);

				for (CPNPlace p : cpn.getPlaces()){
					for(String c:p.getColorsWithCapacityRestriction())
					cwn.getPlace(p.getName()).setColorCapacity(c, p.getColorCapacity(c));
				}
				for (CPNFlowRelation fr : cpn.getFlowRelations()) {
					Multiset<String> constraint = fr.getConstraint();
					CWNFlowRelation newFR = null;
					if (fr.getSource() instanceof CPNPlace)
						newFR = cwn.addFlowRelationPT(fr.getSource().getName(), fr.getTarget().getName(), false);
					if (fr.getSource() instanceof CPNTransition)
						newFR = cwn.addFlowRelationTP(fr.getSource().getName(), fr.getTarget().getName(), false);
					newFR.setConstraint(constraint);
				}
				
			

				try {
					cwn.checkSoundness();
					String filename = editor.getFileReference().getAbsolutePath();
					CPNGraphics cpnGraphics = (CPNGraphics) editor.getNetContainer().getPetriNetGraphics();
					CWNGraphics cwnGraphics = new CWNGraphics();
					cwnGraphics.setArcAnnotationGraphics(cpnGraphics.getArcAnnotationGraphics());
					cwnGraphics.setArcGraphics(cpnGraphics.getArcGraphics());
					cwnGraphics.setColors(cpnGraphics.getColors());
					cwnGraphics.setPlaceGraphics(cpnGraphics.getPlaceGraphics());
					cwnGraphics.setPlaceLabelAnnotationGraphics(cpnGraphics.getPlaceLabelAnnotationGraphics());
					cwnGraphics.setTokenGraphics(cpnGraphics.getTokenGraphics());
					cwnGraphics.setTransitionGraphics(cpnGraphics.getTransitionGraphics());
					cwnGraphics.setTransitionLabelAnnotationGraphics(cpnGraphics.getTransitionLabelAnnotationGraphics());			
					GraphicalCWN newCWNNetContainer = new GraphicalCWN(cwn, cwnGraphics);
					PNSerialization.serialize(newCWNNetContainer, PNSerializationFormat.PNML, filename);
					JOptionPane.showMessageDialog(editor.getGraphComponent(), "Awesome! You're Coloured Workflow Net is VALID.", "CWN is VALID - Awesome Job!", JOptionPane.INFORMATION_MESSAGE);

				} catch (PNValidationException e1) {
					JOptionPane.showMessageDialog(editor.getGraphComponent(), e1.getMessage(), "CWN Validation Failed", JOptionPane.ERROR_MESSAGE);
				} catch (SerializationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (PNSoundnessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			}
		}
	}
}
