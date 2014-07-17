package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.List;

public interface LoadSave {
	public boolean load(List<PatternSetting> patternSettings);
	public boolean save();
}
