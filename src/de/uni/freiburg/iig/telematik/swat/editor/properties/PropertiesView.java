package de.uni.freiburg.iig.telematik.swat.editor.properties;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import de.invation.code.toval.graphic.RestrictedTextField;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;



public class PropertiesView extends JPanel implements PNPropertiesListener{

	private static final long serialVersionUID = 1L;
	
	protected Map<String, PropertiesField> placeFields = new HashMap<String,PropertiesField>();
	protected Map<String, PropertiesField> transitionFields = new HashMap<String,PropertiesField>();
	protected Map<String, PropertiesField> arcFields = new HashMap<String,PropertiesField>();
	
	protected PNProperties properties =  null;

	public PropertiesView(PNProperties properties) throws ParameterException{
		super();
		Validate.notNull(properties);
		this.properties = properties;
		setUpGUI();
	}
	
	protected void setUpGUI() throws ParameterException{
		for(String placeName: properties.getPlaceNames()){
			createFieldsForPlace(placeName);
		}
		for(String transitionName: properties.getTransitionNames()){
			for(PNProperty transitionProperty: properties.getTransitionProperties()){
				createTransitionField(transitionName, transitionProperty);
			}
		}
		for(String arcName: properties.getArcNames()){
			for(PNProperty arcProperty: properties.getArcProperties()){
				createArcField(arcName, arcProperty);
			}
		}
		setBackground(Color.green);
	}
	
	private void createFieldsForPlace(String placeName) throws ParameterException{
		for(PNProperty placeProperty: properties.getPlaceProperties()){
			createPlaceField(placeName, placeProperty);
		}
	}
	
	private void createFieldsForTransition(String transitionName) throws ParameterException{
		for(PNProperty transitionProperty: properties.getTransitionProperties()){
			createTransitionField(transitionName, transitionProperty);
		}
	}
	
	private void createFieldsForArc(String arcName) throws ParameterException{
		for(PNProperty arcProperty: properties.getArcProperties()){
			createArcField(arcName, arcProperty);
		}
	}
		
	private void createPlaceField(String placeName, PNProperty placeProperty) throws ParameterException{
		placeFields.put(placeName, new PropertiesField(PNComponent.PLACE, placeName, properties.getValue(PNComponent.PLACE, placeName, placeProperty), placeProperty));
	}
	
	private void createTransitionField(String transitionName, PNProperty transitionProperty) throws ParameterException{
		transitionFields.put(transitionName, new PropertiesField(PNComponent.TRANSITION, transitionName, properties.getValue(PNComponent.TRANSITION, transitionName, transitionProperty), transitionProperty));
	}
	
	private void createArcField(String arcName, PNProperty arcProperty) throws ParameterException{
		arcFields.put(arcName, new PropertiesField(PNComponent.ARC, arcName, properties.getValue(PNComponent.ARC, arcName, arcProperty), arcProperty));	
	}
		
		
	
	/**
	 * This method is called each time a value is changed within one of the textfields.
	 * @param fieldType
	 * @param name
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	protected void propertiesFieldValueChanged(PNComponent fieldType, String name, PNProperty property, String oldValue, String newValue) {
		try {
			properties.setValue(this, fieldType, name, property, newValue);
		} catch (ParameterException e1) {
			switch(fieldType){
			case PLACE:
				placeFields.get(name).setText(oldValue);
				break;
			case TRANSITION:
				transitionFields.get(name).setText(oldValue);
				break;
			case ARC:
				arcFields.get(name).setText(oldValue);
				break;
			}
		}
	}
	
	@Override
	public void propertyChange(PNPropertyChangeEvent event) {
		if(event.getSource() != this){
			switch(event.getFieldType()){
			case PLACE:
				placeFields.get(event.getName()).setText(event.getNewValue().toString());
				break;
			case TRANSITION:
				transitionFields.get(event.getName()).setText(event.getNewValue().toString());
				break;
			case ARC:
				arcFields.get(event.getName()).setText(event.getNewValue().toString());
				break;
			}
		}
	}
	
	private class PropertiesField extends RestrictedTextField {
		
		private static final long serialVersionUID = -2791152505686200734L;
		
		private PNComponent type = null;
		private PNProperty property = null;
		private String name = null;

		public PropertiesField(PNComponent type, String name, String text, PNProperty property) {
			super(property.getRestriction(), text);
			this.type = type;
			this.property = property;
			this.name = name;
		}

		@Override
		protected void valueChanged(String oldValue, String newValue) {
			propertiesFieldValueChanged(type, name, property, oldValue, newValue);
		}
		
	}

	@Override
	public void componentAdded(PNComponent component, String name) {
		try {
			switch (component) {
			case PLACE:
				createFieldsForPlace(name);
				// TODO: Add place fields to GUI
				break;
			case TRANSITION:
				createFieldsForTransition(name);
				// TODO: Add transition fields to GUI
				break;
			case ARC:
				createFieldsForArc(name);
				// TODO: Add arc fields to GUI
				break;
			}
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void componentRemoved(PNComponent component, String name) {
		switch(component){
		case PLACE:
			placeFields.remove(name);
			//TODO: Remove place fields from GUI
			break;
		case TRANSITION:
			transitionFields.remove(name);
			//TODO: Remove transition fields from GUI
			break;
		case ARC:
			arcFields.remove(name);
			//TODO: Remove arc fields from GUI
			break;
		}
	}
	
   
}
