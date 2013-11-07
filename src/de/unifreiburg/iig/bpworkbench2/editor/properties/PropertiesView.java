package de.unifreiburg.iig.bpworkbench2.editor.properties;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import de.invation.code.toval.graphic.RestrictedTextField;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.unifreiburg.iig.bpworkbench2.editor.properties.PNProperties.FieldType;



public class PropertiesView extends JPanel implements PNPropertiesListener{

	private static final long serialVersionUID = 1L;
	
	protected Map<String, PropertiesField> placeFields = new HashMap<String,PropertiesField>();
	protected Map<String, PropertiesField> transitionFields = new HashMap<String,PropertiesField>();
	protected Map<String, PropertiesField> arcFields = new HashMap<String,PropertiesField>();
	
	protected PNProperties properties =  null;

	public PropertiesView(PNProperties properties) throws ParameterException{
		Validate.notNull(properties);
		this.properties = properties;
		setUpGUI();
	}
	
	protected void setUpGUI() throws ParameterException{
		for(String placeName: properties.getPlaceNames()){
			for(PNProperty placeProperty: properties.getPlaceProperties()){
				placeFields.put(placeName, new PropertiesField(FieldType.PLACE, placeName, properties.getValue(FieldType.PLACE, placeName, placeProperty), placeProperty));
			}
		}
		for(String transitionName: properties.getTransitionNames()){
			for(PNProperty transitionProperty: properties.getTransitionProperties()){
				transitionFields.put(transitionName, new PropertiesField(FieldType.TRANSITION, transitionName, properties.getValue(FieldType.TRANSITION, transitionName, transitionProperty), transitionProperty));
			}
		}
		for(String arcName: properties.getArcNames()){
			for(PNProperty arcProperty: properties.getArcProperties()){
				arcFields.put(arcName, new PropertiesField(FieldType.ARC, arcName, properties.getValue(FieldType.ARC, arcName, arcProperty), arcProperty));
			}
		}
	}
	
	protected void propertiesFieldValueChanged(FieldType fieldType, String name, PNProperty property, String oldValue, String newValue) {
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
	public void propertyChange(PNChangeEvent event) {
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
		
		private FieldType type = null;
		private PNProperty property = null;
		private String name = null;

		public PropertiesField(FieldType type, String name, String text, PNProperty property) {
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
	
	
   
}
