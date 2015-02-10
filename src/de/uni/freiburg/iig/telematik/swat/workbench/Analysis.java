package de.uni.freiburg.iig.telematik.swat.workbench;

import java.util.List;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.bernhard.PatternSetting;

public class Analysis {

	private String name = null;
	private List<PatternSetting> patternSetting = null;
	
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
	
}
