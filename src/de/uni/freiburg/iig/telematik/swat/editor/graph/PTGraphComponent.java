package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
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
			try {
				getGraph().updatePlaceState(cell, tokens);
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
	

}
