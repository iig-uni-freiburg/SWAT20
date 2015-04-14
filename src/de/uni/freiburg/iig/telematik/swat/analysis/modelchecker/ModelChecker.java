package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker;

import java.util.ArrayList;

import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;

public abstract class ModelChecker {

	public abstract void run(ArrayList<CompliancePattern> patterns) throws Exception;
	
}
