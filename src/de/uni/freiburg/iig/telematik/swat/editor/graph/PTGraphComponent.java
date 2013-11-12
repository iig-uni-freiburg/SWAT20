package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.unifreiburg.iig.bpworkbench2.editor.PTNetEditor;

public class PTGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public PTGraphComponent(PTGraph graph, PTNetEditor ptEditor) {
		super(graph, ptEditor);
	}

	@Override
	public PTGraph getGraph() {
		return (PTGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		String tokens = JOptionPane.showInputDialog(PTGraphComponent.this, "Input new amount of tokens");
		try {
			Validate.positiveInteger(tokens);
		} catch (ParameterException ex) {
			JOptionPane.showMessageDialog(PTGraphComponent.this, "Input is not a positive integer.", "Invalid parameter", JOptionPane.ERROR_MESSAGE);
		}

		if (tokens != null) {
			PTMarking initialMarking = getGraph().getNetContainer().getPetriNet().getInitialMarking();
			try {
				initialMarking.set(cell.getId(), new Integer(tokens));
				getGraph().getNetContainer().getPetriNet().setInitialMarking(initialMarking);
			} catch (ParameterException e2) {
				JOptionPane.showMessageDialog(PTGraphComponent.this, "Cannot set initial marking for place.\n Reason: " + e2.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
			}
		}
		return true;
	}



	@Override
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		String weight = JOptionPane.showInputDialog(PTGraphComponent.this, "Input new arc weight");
		try {
			Validate.positiveInteger(weight);
		} catch (ParameterException ex) {
			JOptionPane.showMessageDialog(PTGraphComponent.this, "Input is not a positive integer.", "Invalid parameter", JOptionPane.ERROR_MESSAGE);
		}
		
		if (weight != null) {
			try {
				getGraph().getNetContainer().getPetriNet().getFlowRelation(cell.getId()).setConstraint(new Integer(weight));
			} catch (ParameterException e2) {
				JOptionPane.showMessageDialog(PTGraphComponent.this, "Cannot set arc weight.\n Reason: " + e2.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
			}
		}
		return true;
	}
	
	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		return String.valueOf(((PTFlowRelation) relation).getWeight());
	}
	

}
