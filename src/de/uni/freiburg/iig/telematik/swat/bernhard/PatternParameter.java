package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JComponent;
/**
 * an abstract class representing a Parameter used by the choosepatternwindow
 * @author bernhard
 *
 */
public abstract class PatternParameter {

	protected String name, type;
	protected JComponent jComponent;
	public PatternParameter(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}
	public JComponent getjComponent() {
		return jComponent;
	}
	public void setjComponent(JComponent jComponent) {
		this.jComponent = jComponent;
	}
	public abstract String getValue();
}
