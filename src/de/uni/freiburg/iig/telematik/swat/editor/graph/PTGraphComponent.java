package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxCellOverlay;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class PTGraphComponent extends PNGraphComponent {

	private boolean isExecution;

	@Override
	protected boolean singleClickOnTransition(PNGraphCell cell, MouseEvent e) {
		isExecution = getGraph().isExecution();
		if(isExecution){
			try {
				getGraph().fireTransition(cell);
				highlightEnabledTransitions();
			} catch (ParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (PNException e1) {
				JOptionPane.showMessageDialog(PTGraphComponent.this, e1.getMessage(), "Execution Error", JOptionPane.ERROR_MESSAGE);

			}
		}
		return true;
	}
	@Override
	public void highlightEnabledTransitions() {
		Set<String> nameSet = null;
		try {
			nameSet = PNUtils.getNameSetFromTransitions(getGraph().getNetContainer().getPetriNet().getEnabledTransitions(), true);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		removeCellOverlays();
		for(String n:nameSet){
			PNGraphCell cell =getGraph().nodeReferences.get(n);
			Rectangle geo = cell.getGeometry().getRectangle();
//			enabledTransitionsPanel =
			mxCellOverlay overlay = null;
			try {
				overlay = new mxCellOverlay(IconFactory.getIcon("playgreen"), null);
				overlay.setAlign(mxConstants.ALIGN_CENTER);
				overlay.setVerticalAlign(mxConstants.ALIGN_MIDDLE);
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
			addCellOverlay(cell, overlay);
//			getGraphics().fillRect(geo.x, geo.y, geo.width, geo.height);;
			
		}
		
		
	}


	private static final long serialVersionUID = -1698182711658593407L;

	public PTGraphComponent(PTGraph graph) {
		super(graph);
	}

	@Override
	public PTGraph getGraph() {
		return (PTGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		String tokens = JOptionPane.showInputDialog(PTGraphComponent.this, "Input new amount of tokens");
		try {
			Validate.notNegativeInteger(tokens);
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
	protected boolean doubleClickOnArcLabel(PNGraphCell cell, MouseEvent e) {
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
