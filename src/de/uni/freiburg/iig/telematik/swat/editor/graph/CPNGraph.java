package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.misc.CircularPointGroup;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.swat.editor.menu.AbstractCPNTokenConfigurer;
import de.uni.freiburg.iig.telematik.swat.editor.properties.CPNProperties;

/**
 * @author julius
 * 
 */
public class CPNGraph extends PNGraph {

	private HashMap<String, AbstractCPNTokenConfigurer> tokenConfigurerWindows = new HashMap<String, AbstractCPNTokenConfigurer>();
	private AbstractCPNTokenConfigurer lastTC;
	public CPNGraph(GraphicalCPN graphicalCPN, CPNProperties cpnProperties) throws ParameterException {
		super(graphicalCPN, cpnProperties);
	}

	@Override
	public GraphicalCPN getNetContainer() {
		return (GraphicalCPN) super.getNetContainer();
	}

	@Override
	protected CPNProperties getPNProperties() {
		return (CPNProperties) super.getPNProperties();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getArcConstraint(AbstractFlowRelation relation) throws ParameterException {
		Map<String, Color> colors = getNetContainer().getPetriNetGraphics().getColors();
		colors.put("black", Color.BLACK);
		String arcString = "";
		for (Entry<String, Color> color : colors.entrySet()) {
			int colorNumber = ((CPNFlowRelation) relation).getConstraint(color.getKey());
			if (colorNumber > 0)
				arcString += color.getKey() + ": " + String.valueOf(((CPNFlowRelation) relation).getConstraint(color.getKey())) + "\n";
		}
		return arcString;
	}

	@Override
	public void updatePlaceState(String name, Multiset<String> state) throws ParameterException {
		CPNMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		initialMarking.set(name, state);
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
	}

	@Override
	public
	/**
	 * @param cell
	 * @param circularPointGroup
	 * @return
	 */ Multiset<String> getPlaceStateForCell(String id, CircularPointGroup circularPointGroup) {
		CPNPlace place = (CPNPlace) getNetContainer().getPetriNet().getPlace(id);
		return place.getState( );
	}



	@Override
	protected String getPlaceToolTip(PNGraphCell cell) {
		CPNPlace ptPlace = getNetContainer().getPetriNet().getPlace(cell.getId());
		return "Cap:" + ((ptPlace.getCapacity() == -1) ? "\u221e" : ptPlace.getCapacity());
	}

	@Override
	protected String getTransitionToolTip(PNGraphCell cell) {
		return "";
	}

	@Override
	protected String getArcToolTip(PNGraphCell cell) {
		return "";
	}

	@Override
	protected void setArcLabel(PNGraph pnGraph, String id, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractMarking inOrDecrementPlaceState(PNGraphCell cell, int wheelRotation) throws ParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getTokenColorForName(String name) {
		Color color = getNetContainer().getPetriNetGraphics().getColors().get(name);
		return color;
	}

	@Override
	public void updateTokenColor(String name, Color value) {
		CPN cpn = getNetContainer().getPetriNet();
		Map<String, Color> colors = getNetContainer().getPetriNetGraphics().getColors();
		if(value != null)
		colors.put(name, value);
		else{
		colors.remove(name);	
		}
		getNetContainer().getPetriNetGraphics().setColors(colors);
		} 
	@Override
	public void updateConstraint(String name, Multiset value) {
		CPNFlowRelation flowrelation = (CPNFlowRelation) getNetContainer().getPetriNet().getFlowRelation(name);
		if(value != null)
			flowrelation.setConstraint(value);	
		else{
				flowrelation.setConstraint(new Multiset<String>());
		}
//		flowrelation.setConstraint(value);		
		PNGraphCell cell = arcReferences.get(name);
		cell.setValue(getArcConstraint(flowrelation));
		refresh();
		
	}

	public void newTokenConfigurer(PNGraphCell cell, CPNGraphComponent cpnGraphComponent) {
		Window window = SwingUtilities.getWindowAncestor(cpnGraphComponent);
double x;
double y;
double height = 0;
double deltaY = 0;
int spacing;
switch(cell.getType()){
case ARC:
	CPNFlowRelation flowRelation = getNetContainer().getPetriNet().getFlowRelation(cell.getId());
	if (!tokenConfigurerWindows.containsKey(cell.getId())) {
		AbstractCPNTokenConfigurer tc = new AbstractCPNTokenConfigurer(window, flowRelation, this);
		spacing = (int) (window.getBounds().getY() + 120);
		if (lastTC != null) {
			height = lastTC.getBounds().getHeight();
			deltaY = lastTC.getBounds().getY();
			spacing = (int) (height + deltaY);
		}

		x = window.getBounds().getX();
		y = spacing;
		tc.setLocation((int) x, (int) y);
		tc.setVisible(true);
		tc.pack();
		tokenConfigurerWindows.put(cell.getId(), tc);
		lastTC = tc;
	} else {
		tokenConfigurerWindows.get(cell.getId()).setVisible(false);
		tokenConfigurerWindows.get(cell.getId()).setVisible(true);
	}
	break;
		case PLACE:
			CPNPlace place = getNetContainer().getPetriNet().getPlace(cell.getId());
			if (!tokenConfigurerWindows.containsKey(cell.getId())) {
				AbstractCPNTokenConfigurer tc = new AbstractCPNTokenConfigurer(window, place, this);
				spacing = (int) (window.getBounds().getY() + 120);
				if (lastTC != null) {
					height = lastTC.getBounds().getHeight();
					deltaY = lastTC.getBounds().getY();
					spacing = (int) (height + deltaY);
				}

				x = window.getBounds().getX();
				y = spacing;
				tc.setLocation((int) x, (int) y);
				tc.setVisible(true);
				tc.pack();
				tokenConfigurerWindows.put(cell.getId(), tc);
				lastTC = tc;
			} else {
				tokenConfigurerWindows.get(cell.getId()).setVisible(false);
				tokenConfigurerWindows.get(cell.getId()).setVisible(true);
			}

	break;
case TRANSITION:
	break;
default:
	break;

}

		
	}

	@Override
	public int getCapacityforPlace(String name, String color) {
		return getNetContainer().getPetriNet().getPlace(name).getColorCapacity(color);
	}

	@Override
	public void updatePlaceCapacity(String name, String color, int newCapacity) {
		CPNPlace place = getNetContainer().getPetriNet().getPlace(name);
		if(newCapacity <= 0)
			place.removeColorCapacity(color);
		else
		place.setColorCapacity(color, newCapacity);			
	}

	@Override
	public void updateViews() {
		for(AbstractCPNTokenConfigurer w:tokenConfigurerWindows.values())
			w.updateView();
	}

	@Override
	public void updateTokenConfigurer(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Multiset<String> getConstraintforArc(String name) {
		return getNetContainer().getPetriNet().getFlowRelation(name).getConstraint();
		}

	@Override
	public Set getAccessModeforTransition(String name, String color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAccessModeTransition(String name, String color, Set newAM) {
		// TODO Auto-generated method stub
		
	}



}
