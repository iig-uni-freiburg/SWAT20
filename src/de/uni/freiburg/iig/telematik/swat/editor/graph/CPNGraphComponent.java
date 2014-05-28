package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.swat.editor.menu.TokenConfigurer;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBarDialog;

public class CPNGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public CPNGraphComponent(CPNGraph cpnGraph) {
		super(cpnGraph);
	}

	@Override
	public CPNGraph getGraph() {
		return (CPNGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		// TODO Table which lists constraints

		try {
			
//			JDialog dialog = 
//			Multiset<String> marking = getGraph().getNetContainer().getPetriNet().getMarking().get(cell.getId());
			getGraph().newTokenConfigurer(cell,this);
//			loadTokenConfigurer(cell, dialog);
		} catch (ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}


	@Override
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		// TODO Table which lists constraints

		try {
			
//			JDialog dialog = 
//			Multiset<String> marking = getGraph().getNetContainer().getPetriNet().getMarking().get(cell.getId());
			getGraph().newTokenConfigurer(cell,this);
//			loadTokenConfigurer(cell, dialog);
		} catch (ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}
	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		// TODO decrementing or incrementing tokennumber of selected color with
		// mousewheel
		return false;
	}

}
