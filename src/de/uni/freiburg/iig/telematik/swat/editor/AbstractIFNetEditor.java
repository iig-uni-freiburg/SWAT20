package de.uni.freiburg.iig.telematik.swat.editor;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.swat.editor.properties.IFNetProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties;

public abstract class AbstractIFNetEditor extends PNEditorComponent {

	private static final long serialVersionUID = 7463202384539027183L;

	public AbstractIFNetEditor() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public AbstractIFNetEditor(AbstractGraphicalIFNet netContainer) {
		super(netContainer);
	}

	@Override
	protected PNProperties createPNProperties() {
		//TODO:		return new IFNetProperties(getNetContainer());
		return null;
	}

	@SuppressWarnings("rawtypes") 
	protected String getArcConstraint(AbstractFlowRelation relation) {
		// TODO: Do something
		return null;
	}

	@Override
	protected PNProperties getPNProperties() {
		return (IFNetProperties) super.getPNProperties();
	}
	
	
}
