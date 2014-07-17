package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
/**
 * the pattern class represents are pattern with its name and parameters
 * @author bernhard
 *
 */
public class PatternSetting {

	public PatternResult getResult() {
		return result;
	}

	public void setResult(PatternResult result) {
		this.result = result;
	}

	public PatternSetting(String name, List<Parameter> parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
		result=null;
	}
	private String name;
	private String parameterAppliedString;
	private List<Parameter> parameters;
	private PatternResult result;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Parameter> getParameters() {
		return parameters;
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
	/*
	 * replace several things at once
	 */
	public void updateParameterAppliedString() {
		parameterAppliedString=new String(name);
		for(Parameter p: parameters) {
			// take this random name to help for replace
			String random="__#12345#__";
			parameterAppliedString.replaceFirst(p.getName(), random);
			parameterAppliedString=parameterAppliedString.replaceFirst(random, Helpers.getFirst(p.getValue()).getOperandName());
		}
	}
}
