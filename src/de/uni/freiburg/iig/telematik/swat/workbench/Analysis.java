package de.uni.freiburg.iig.telematik.swat.workbench;

import java.util.List;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.bernhard.PatternSetting;

public class Analysis {

	private String name = null;
	private List<PatternSetting> patternSetting = null;
	private int hashCode = 0;
	
	public Analysis(String name, List<PatternSetting> patternSetting) {
		super();
		Validate.notNull(name);
		Validate.notNull(patternSetting);
		this.name = name;
		this.patternSetting = patternSetting;
	}

	public String getName() {
		return name;
	}

	public List<PatternSetting> getPatternSetting() {
		return patternSetting;
	}
	
	public String toString() {
		return name;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		try{
		this.hashCode = hashCode;
		} catch (RuntimeException e) {
			hashCode = 0;
		}
	}
	
}
