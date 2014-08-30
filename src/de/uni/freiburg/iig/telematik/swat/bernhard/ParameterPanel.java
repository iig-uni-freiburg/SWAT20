package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

import javax.swing.JComponent;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
/**
 * an abstract class representing a Parameter used by the choosepatternwindow
 * @author bernhard
 *
 */
public abstract class ParameterPanel {

	protected String name;
	protected JComponent content;
	public ParameterPanel(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public JComponent getContent() {
		return content;
	}
	public void setContent(JComponent jComponent) {
		this.content = jComponent;
	}
	public abstract List<ParamValue> getValue();
	public abstract void setValue(List<ParamValue> value);
}
