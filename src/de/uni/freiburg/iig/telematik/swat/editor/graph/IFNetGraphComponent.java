package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import de.invation.code.toval.validate.ParameterException;

public class IFNetGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public IFNetGraphComponent(IFNetGraph IFNetGraph) {
		super(IFNetGraph);
	}

	@Override
	public IFNetGraph getGraph() {
		return (IFNetGraph) super.getGraph();
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
	protected boolean doubleClickOnTransition(PNGraphCell cell, MouseEvent e) {
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
