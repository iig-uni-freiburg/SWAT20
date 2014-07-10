package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;
/**
 * the pattern class represents are pattern with its name and parameters
 * @author bernhard
 *
 */
public class Pattern {

	private String name;
	private String parameterAppliedString;
	public Pattern(String name, List<PatternParameter> parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
	}
	private List<PatternParameter> parameters;
	
	public Pattern clone() {
		List<PatternParameter> para=new ArrayList<PatternParameter>(parameters);
		return new Pattern(new String(name), para);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PatternParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<PatternParameter> parameters) {
		this.parameters = parameters;
	}
	/**
	 * returns the String representation of the pattern e.g:
	 * Q precedes P
	 * Q check document
	 * P print document
	 * then the presentation is check document precedes print document
	 */
	@Override
	public String toString() {
		return parameterAppliedString;
		
	}
	public void updateParameterAppliedString() {
		parameterAppliedString=new String(name);
		for(PatternParameter p: parameters) {
			parameterAppliedString=parameterAppliedString.replaceFirst(p.name, p.value);
		}
		//System.out.println(parameterAppliedString);
	}
}
