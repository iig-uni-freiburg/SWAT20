package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.event.MouseEvent;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxGraphModel.mxValueChange;

import de.invation.code.toval.graphic.misc.CircularPointGroup;
import de.invation.code.toval.graphic.misc.PColor;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PTProperties;

/**
 * @author julius
 *
 */
public class PTGraph extends PNGraph {

	public PTGraph(GraphicalPTNet netContainer, PTProperties properties) throws ParameterException {
		super(netContainer, properties);
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PTProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}
	
	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		return String.valueOf(((PTFlowRelation) relation).getWeight());
	}

	@Override
	public void updatePlaceState(PNGraphCell cell, Multiset<String> state) throws ParameterException {
		Integer tokens =  state.multiplicity("black");
		PTMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		initialMarking.set(cell.getId(), new Integer(tokens));
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
	}
	
	@Override
	/**
	 * @param cell
	 * @param circularPointGroup
	 * @return
	 */
	protected Multiset<String> getPlaceStateForCell(PNGraphCell cell, CircularPointGroup circularPointGroup) {
		PTPlace place = (PTPlace) getNetContainer().getPetriNet().getPlace(cell.getId());
		if(place!= null){
			if(circularPointGroup != null)
		circularPointGroup.addPoints(PColor.black, place.getState());
		Multiset<String> multiSet = new Multiset<String>();
		multiSet.setMultiplicity("black", place.getState());
		return multiSet;}
		return null;
	}
	
@Override
	/** Method for incrementing or decrementing the current #PTMarking of the given #PTPlace
	 * @param cell
	 * @param wheelRotation
	 * @return 
	 * @throws ParameterException
	 */
	public AbstractMarking inOrDecrementPlaceState(PNGraphCell cell, int wheelRotation) throws ParameterException {
		PTMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		PTMarking oldValue = initialMarking;
		Integer current = initialMarking.get(cell.getId());
		int capacity = getNetContainer().getPetriNet().getPlace(cell.getId()).getCapacity();	
		if(current != null && (capacity<0 ^ capacity>=(current - wheelRotation))) // null means here that the value "zero" has been set before / capacity -1 means infinite
		initialMarking.set(cell.getId(),  current - wheelRotation);
		else if(current == null && wheelRotation < 0 ) // create new Marking
		initialMarking.set(cell.getId(),  1);
		
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
		return oldValue;
	}



	@Override
	protected String getPlaceToolTip(PNGraphCell cell) {
		PTPlace ptPlace = getNetContainer().getPetriNet().getPlace(cell.getId());

		return  "Cap:"+ ((ptPlace.getCapacity() == -1)?"\u221e":ptPlace.getCapacity());
	}

	
	@Override
	protected String getTransitionToolTip(PNGraphCell cell) {
		return "";
	}

	@Override
	protected String getArcToolTip(PNGraphCell cell) {
		return "";
	}

	public void fireTransition(PNGraphCell cell) throws ParameterException, PNException {
			getNetContainer().getPetriNet().fire(cell.getId());
	
	}

	@Override
	protected void setArcLabel(PNGraph sender, String arcName, String weight) {
		try {
			getPNProperties().setArcWeight(sender, arcName, weight);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



		  

}
