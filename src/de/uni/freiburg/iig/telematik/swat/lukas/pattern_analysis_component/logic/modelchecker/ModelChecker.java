package de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker;

import java.util.ArrayList;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.CompliancePattern;

public abstract class ModelChecker {

	public abstract void run(ArrayList<CompliancePattern> patterns) throws Exception;
	
}
