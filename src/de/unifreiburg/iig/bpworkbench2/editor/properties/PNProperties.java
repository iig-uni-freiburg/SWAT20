package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class PNProperties {

	protected PNChangeSupport changes = new PNChangeSupport();
	private AbstractGraphicalPN<?,?,?,?,?,?,?> net = null;
	
	public PNProperties(AbstractGraphicalPN<?,?,?,?,?,?,?> ptNet){
		this.net = ptNet;
	}
	
	protected AbstractGraphicalPN<?,?,?,?,?,?,?> getNet(){
		return net;
	}
	
	@SuppressWarnings("rawtypes")
	public List<String> getPlaceNames(){
		List<String> result = new ArrayList<String>();
		for(AbstractPlace place : getNet().getPetriNet().getPlaces()){
			result.add(place.getName());
		}
		Collections.sort(result);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public List<String> getTransitionNames(){
		List<String> result = new ArrayList<String>();
		for(AbstractTransition transition : getNet().getPetriNet().getTransitions()){
			result.add(transition.getName());
		}
		Collections.sort(result);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public List<String> getArcNames(){
		List<String> result = new ArrayList<String>();
		for(AbstractFlowRelation arc : getNet().getPetriNet().getFlowRelations()){
			result.add(arc.getName());
		}
		Collections.sort(result);
		return result;
	}
	
	public Set<PNProperty> getPlaceProperties(){
		Set<PNProperty> result = new HashSet<PNProperty>();
		result.add(PNProperty.PLACE_LABEL);
		result.add(PNProperty.PLACE_SIZE);
		return result;
	}
	
	public Set<PNProperty> getTransitionProperties(){
		Set<PNProperty> result = new HashSet<PNProperty>();
		result.add(PNProperty.TRANSITION_LABEL);
		result.add(PNProperty.TRANSITION_SIZE);
		return result;
	}
	
	public Set<PNProperty> getArcProperties(){
		return new HashSet<PNProperty>();
	}
	
	public String getPlaceLabel(String placeName) throws ParameterException{
		Validate.notNull(placeName);
		return getNet().getPetriNet().getPlace(placeName).getLabel();		
	}
	
	public String getTransitionLabel(String transitionName) throws ParameterException{
		Validate.notNull(transitionName);
		return getNet().getPetriNet().getTransition(transitionName).getLabel();		
	}
	
	public Integer getPlaceSize(String placeName) throws ParameterException{
		Validate.notNull(placeName);
		if(!getNet().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		
		return (int) getNet().getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().getX();
	}
	
	public Integer getTransitionSize(String transitionName) throws ParameterException{
		Validate.notNull(transitionName);
		if(!getNet().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		
		return (int) getNet().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getX();
	}
	
	public void setPlaceLabel(Object sender, String placeName, String label) throws ParameterException {
		Validate.notNull(label);
		Validate.notNull(placeName);
		String oldLabel = getNet().getPetriNet().getPlace(placeName).getLabel();
		getNet().getPetriNet().getPlace(placeName).setLabel(label);
		PNChangeEvent event = new PNChangeEvent(sender, FieldType.PLACE, placeName, PNProperty.PLACE_LABEL, oldLabel , label);
		changes.fireChangeEvent(event);
	}
	
	public void setTransitionLabel(Object sender, String transitionName, String label) throws ParameterException {
		Validate.notNull(label);
		Validate.notNull(transitionName);
		String oldLabel = getNet().getPetriNet().getTransition(transitionName).getLabel();
		getNet().getPetriNet().getTransition(transitionName).setLabel(label);
		PNChangeEvent event = new PNChangeEvent(sender, FieldType.TRANSITION, transitionName, PNProperty.TRANSITION_LABEL, oldLabel , label);
		changes.fireChangeEvent(event);
	}
	
	public void setPlaceSize(Object sender, String placeName, String size) throws ParameterException{
		Validate.positiveInteger(size);
		setPlaceSize(sender, placeName, Integer.parseInt(size));
	}
	
	public void setPlaceSize(Object sender, String placeName, Integer size) throws ParameterException{
		Validate.notNull(placeName);
		Validate.notNull(size);
		Validate.bigger(size, 0);
		if(!getNet().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		
		Integer oldSize = (int) getNet().getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().getX();
		getNet().getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().setX(size);
		PNChangeEvent event = new PNChangeEvent(sender, FieldType.PLACE, placeName, PNProperty.PLACE_SIZE, oldSize , size);
		changes.fireChangeEvent(event);
	}
	
	public void setTransitionSize(Object sender, String transitionName, String size) throws ParameterException{
		Validate.positiveInteger(size);
		setTransitionSize(sender, transitionName, Integer.parseInt(size));
	}
	
	public void setTransitionSize(Object sender, String transitionName, Integer size) throws ParameterException{
		Validate.notNull(transitionName);
		Validate.notNull(size);
		Validate.bigger(size, 0);
		if(!getNet().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		
		Integer oldSize = (int) getNet().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getX();
		getNet().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().setX(size);
		PNChangeEvent event = new PNChangeEvent(sender, FieldType.TRANSITION, transitionName, PNProperty.TRANSITION_SIZE, oldSize , size);
		changes.fireChangeEvent(event);
	}

	public void addPNPropertiesListener(PNPropertiesListener l) {
		changes.addListener(l);
	}

	public void removePNPropertiesListener(PNPropertiesListener l) {
		changes.removeListener(l);
	}
	
	public String getValue(FieldType fieldType, String name, PNProperty property) throws ParameterException {
		switch (fieldType) {
		case PLACE:
			return getPlaceProperty(name, property);
		case TRANSITION:
			return getTransitionProperty(name, property);
		case ARC:
			return getArcProperty(name, property);
		}
		return null;
	}

	protected String getPlaceProperty(String name, PNProperty property) throws ParameterException {
		switch(property){
		case PLACE_LABEL:
			return getPlaceLabel(name);
		case PLACE_SIZE:
			return getPlaceSize(name).toString();
		}
		return null;
	}
	
	protected String getTransitionProperty(String name, PNProperty property) throws ParameterException {
		switch(property){
		case TRANSITION_LABEL:
			return getTransitionLabel(name);
		case TRANSITION_SIZE:
			return getTransitionSize(name).toString();
		}
		return null;
	}
	
	protected String getArcProperty(String name, PNProperty property) throws ParameterException {
		return null;
	}

	public void setValue(Object sender, FieldType fieldType, String name, PNProperty property, String value) throws ParameterException {
		switch (fieldType) {
		case PLACE:
			setPlaceProperty(sender, name, property, value);
			break;
		case TRANSITION:
			setTransitionProperty(sender, name, property, value);
			break;
		case ARC:
			setArcProperty(sender, name, property, value);
			break;
		}
	}
	
	protected boolean setPlaceProperty(Object sender, String name, PNProperty property, String value) throws ParameterException{
		switch(property){
		case PLACE_LABEL:
			setPlaceLabel(sender, name, value);
			return true;
		case PLACE_SIZE:
			setPlaceSize(sender, name, value);
			return true;
		}
		return false;
	}
	
	protected boolean setTransitionProperty(Object sender, String name, PNProperty property, String value) throws ParameterException{
		switch(property){
		case TRANSITION_LABEL:
			setTransitionLabel(sender, name, value);
			return true;
		case TRANSITION_SIZE:
			setTransitionSize(sender, name, value);
			return true;
		}
		return false;
	}
	
	protected boolean setArcProperty(Object sender, String name, PNProperty property, String value) throws ParameterException{
		return false;
	}
	
	public enum FieldType {
		PLACE, TRANSITION, ARC;
	}
}
