package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class PTProperties {

	private PNChangeSupport changes = new PNChangeSupport();
private GraphicalPTNet ptNet = null;
	public PTProperties(GraphicalPTNet ptNet){
		this.ptNet = ptNet;
	}
	
	public List<String> getPlaceNames(){
		List<String> result = new ArrayList<String>();
		for(PTPlace place : ptNet.getPetriNet().getPlaces()){
			result.add(place.getName());
		}
		Collections.sort(result);
		return result;
		
	}
	
	public List<String> getTransitionNames(){
		List<String> result = new ArrayList<String>();
		for(PTTransition transition : ptNet.getPetriNet().getTransitions()){
			result.add(transition.getName());
		}
		Collections.sort(result);
		return result;
		
	}
	
	public List<String> getArcNames(){
		List<String> result = new ArrayList<String>();
		for(PTFlowRelation arc : ptNet.getPetriNet().getFlowRelations()){
			result.add(arc.getName());
		}
		Collections.sort(result);
		return result;
		
	}
	
	public String getPlaceLabel(String placeName) throws ParameterException{
		Validate.notNull(placeName);
		return ptNet.getPetriNet().getPlace(placeName).getLabel();		
	}
	
	public String getTransitionLabel(String transitionName) throws ParameterException{
		Validate.notNull(transitionName);
		return ptNet.getPetriNet().getTransition(transitionName).getLabel();		
	}
	
	public int getPlaceSize(String placeName) throws ParameterException{
		Validate.notNull(placeName);
		if(!ptNet.getPetriNet().containsPlace(placeName)){
			throw new ParameterException("Unknown Place");
		}
		return (int) ptNet.getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().getX();
	}
	
	public int getTransitionSize(String transitionName) throws ParameterException{
		Validate.notNull(transitionName);
		if(!ptNet.getPetriNet().containsTransition(transitionName)){
			throw new ParameterException("Unknown Transition");
		}
		return (int) ptNet.getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getX();
	}
	
	public void setPlaceLabel(Object sender, String placeName, String label) throws ParameterException {
		Validate.notNull(label);
		Validate.notNull(placeName);
		String oldLabel = ptNet.getPetriNet().getPlace(placeName).getLabel();
		ptNet.getPetriNet().getPlace(placeName).setLabel(label);
		PNChangeEvent event = new PNChangeEvent(sender, PNProperty.PLACE_LABEL, oldLabel , label);
		changes.fireChangeEvent(event);
	}
	
	public void setTransitionLabel(Object sender, String transitionName, String label) throws ParameterException {
		Validate.notNull(label);
		Validate.notNull(transitionName);
		String oldLabel = ptNet.getPetriNet().getTransition(transitionName).getLabel();
		ptNet.getPetriNet().getTransition(transitionName).setLabel(label);
		PNChangeEvent event = new PNChangeEvent(sender, PNProperty.TRANSITION_LABEL, oldLabel , label);
		changes.fireChangeEvent(event);
	}
	
	public void setPlaceSize(Object sender, String placeName, Integer size) throws ParameterException{
		Validate.notNull(placeName);
		Validate.notNull(size);
		Validate.bigger(size, 0);
		if(!ptNet.getPetriNet().containsPlace(placeName)){
			throw new ParameterException("Unknown Place");
		}
		Integer oldSize = (int) ptNet.getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().getX();
		ptNet.getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().setX(size);
		PNChangeEvent event = new PNChangeEvent(sender, PNProperty.PLACE_SIZE, oldSize , size);
		changes.fireChangeEvent(event);
	}
	
	public void setTransitionSize(Object sender, String transitionName, Integer size) throws ParameterException{
		Validate.notNull(transitionName);
		Validate.notNull(size);
		Validate.bigger(size, 0);
		if(!ptNet.getPetriNet().containsTransition(transitionName)){
			throw new ParameterException("Unknown Transition");
		}
		Integer oldSize = (int) ptNet.getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getX();
		ptNet.getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().setX(size);
		PNChangeEvent event = new PNChangeEvent(sender, PNProperty.TRANSITION_SIZE, oldSize , size);
		changes.fireChangeEvent(event);
	}


	public void addPNPropertiesListener(PNPropertiesListener l) {
		changes.addListener(l);
	}

	public void removePNPropertiesListener(PNPropertiesListener l) {
		changes.removeListener(l);
	}
}
