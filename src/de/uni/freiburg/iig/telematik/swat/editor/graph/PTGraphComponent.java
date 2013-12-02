package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import javax.swing.JOptionPane;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;

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
				Multiset<String> multiSet = new Multiset<String>();
				multiSet.setMultiplicity("black", new Integer(tokens));
				getGraph().updatePlaceState(cell, multiSet);
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
				getGraph().getPNProperties().setArcWeight(this, cell.getId(), weight);
			} catch (ParameterException e2) {
				JOptionPane.showMessageDialog(PTGraphComponent.this, "Cannot set arc weight.\n Reason: " + e2.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
			}
		}
		return true;
	}

	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		try {
			
		 getGraph().inOrDecrementPlaceState(cell, e.getWheelRotation());
		} catch (ParameterException e1) {
			System.out.println("Error while changing number of via mouseWheel");
			e1.printStackTrace();
		}
		return true;
	}


	/**
	 * Resets the control points of the given edge.
	 */
	public Object resetEdge(Object edge)
	{
		mxGeometry geo = getGraph().getModel().getGeometry(edge);

		if (geo != null)
		{
			// Resets the control points
			List<mxPoint> points = geo.getPoints();

			if (points != null && !points.isEmpty())
			{
				geo = (mxGeometry) geo.clone();
				geo.setPoints(null);
				getGraph().getModel().setGeometry(edge, geo);
			}
		}

		return edge;
	}
	


}
