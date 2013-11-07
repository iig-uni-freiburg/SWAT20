package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;

public class PTProperties extends PNProperties {
	
	public PTProperties(GraphicalPTNet ptNet){
		super(ptNet);
	}
	
	@Override
	protected GraphicalPTNet getNet() {
		return (GraphicalPTNet) super.getNet();
	}
	
	@Override
	public Set<PNProperty> getArcProperties(){
		Set<PNProperty> result = super.getArcProperties();
		result.add(PNProperty.ARC_WEIGHT);
		return result;
	}
	
	public Integer getArcWeight(String arcName) throws ParameterException {
		Validate.notNull(arcName);
		return getNet().getPetriNet().getFlowRelation(arcName).getWeight();
	}
	
	public void setArcWeight(Object sender, String arcName, String weight) throws ParameterException{
		Validate.positiveInteger(weight);
		setArcWeight(sender, arcName, Integer.parseInt(weight));
	}
	
	public void setArcWeight(Object sender, String arcName, Integer weight) throws ParameterException{
		Validate.notNull(arcName);
		Validate.notNull(weight);
		Validate.bigger(weight, 0);
		if(!getNet().getPetriNet().containsFlowRelation(arcName))
			throw new ParameterException("Unknown Transition");
		
		Integer oldWeight = getArcWeight(arcName);
		getNet().getPetriNet().getFlowRelation(arcName).setWeight(weight);
		PNChangeEvent event = new PNChangeEvent(sender, FieldType.ARC, arcName, PNProperty.ARC_WEIGHT, oldWeight , weight);
		changes.fireChangeEvent(event);
	}
	
	

	@Override
	protected String getArcProperty(String name, PNProperty property) throws ParameterException {
		String superResult = super.getArcProperty(name, property);
		if(superResult == null){
			switch (property) {
			case ARC_WEIGHT:
				return getArcWeight(name).toString();
			}
			return null;
		} else {
			return superResult;
		}
	}

	@Override
	protected boolean setArcProperty(Object sender, String name, PNProperty property, String value) throws ParameterException {
		if(!super.setArcProperty(sender, name, property, value)){
			switch (property) {
			case ARC_WEIGHT:
				setArcWeight(sender, name, value);
				return true;
			}
			return false;
		} else {
			return true;
		}
	}
	
}
