package de.uni.freiburg.iig.telematik.swat.bernhard;

import javax.swing.JComponent;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
/**
 * an abstract class representing a Parameter used by the choosepatternwindow
 * @author bernhard
 *
 */
public abstract class PatternParameterPanel {

	protected String name;
	protected OperandType type;
	protected JComponent jComponent;
	public PatternParameterPanel(String name, OperandType type) {
		super();
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public OperandType getType() {
		return type;
	}
	public void setType(OperandType type) {
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
