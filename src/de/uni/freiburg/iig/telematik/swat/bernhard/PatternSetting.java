package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
/**
 * This class represents a parameterized pattern with its name,
 * parameters and the result from the analysis.
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
	
	public void reset() {
		this.result = null;
	}
	
	/**
	 * Create a PatternSetting for a given name and parameters
	 * @param name the name of the pattern
	 * @param parameters the parameters that will be set later by the user
	 */
	public PatternSetting(String name, List<Parameter> parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
		//updateParameterAppliedString();
		result=null;
	}
	
	public PatternSetting(String name) {
		this.name = name;
		result=null;
	}

	private String name;
	private List<Parameter> parameters;
	protected PatternResult result;
	

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
	 * Create an exact copy of the PatternSetting, that means that the values of the
	 * parameters will be copied by value.
	 */
	public PatternSetting clone() {
		PatternSetting ps=new PatternSetting(name,parameters);
		ps.setParameters(Helpers.cloneParameterList(parameters));
		ps.setResult(result);
		return ps;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof PatternSetting)) {
			return false;
		}
		PatternSetting ps=(PatternSetting) o;
		if(false==name.equals(ps.getName())) {
			return false;
		}
		if(parameters.size() != ps.getParameters().size()) {
			return false;
		}
		for(int i=0; i < parameters.size(); i++) {
			List<ParamValue> myValues=parameters.get(i).getValue();
			List<ParamValue> otherValues=ps.getParameters().get(i).getValue();
			if(myValues.size() != otherValues.size()) {
				return false;
			}
			for(int j=0; j < myValues.size(); j++) {
				if(false==(myValues.get(j).equals(otherValues.get(j)))) {
					return false;
				}
			}
		}

		return true;
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
		String s=name+"\n";
		for(Parameter p:parameters) {
			s+=p.toString()+", ";
		}
		s=s.substring(0, s.length()-3);
		return s;
	}
}
