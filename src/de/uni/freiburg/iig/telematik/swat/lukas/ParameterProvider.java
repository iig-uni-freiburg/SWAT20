package de.uni.freiburg.iig.telematik.swat.lukas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ParameterProvider {
	
	
	HashSet<String> mOneParameterCFPatterns;
	HashSet<String> mTwoParameterCFPatterns;
	HashSet<String> mThreeParameterCFPatterns;
	HashSet<String> mDFPatterns;
	HashSet<String> mTwoTransitionRP;
	
	public ParameterProvider() {
		
		mOneParameterCFPatterns = new HashSet<String>(Arrays.asList(Exists.NAME, Universal.NAME, Absent.NAME));
		mTwoParameterCFPatterns = new HashSet<String>(Arrays.asList(Precedes.NAME, LeadsTo.NAME, XLeadsTo.NAME, 
				CoExists.NAME, CoAbsent.NAME, Exclusive.NAME, Corequisite.NAME, MutexChoice.NAME));
		mThreeParameterCFPatterns = new HashSet<String>(Arrays.asList(ChainPrecedes.NAME, ChainLeadsTo.NAME, PrecedesChain.NAME, LeadsToChain.NAME));
		mTwoTransitionRP = new HashSet<String>(Arrays.asList(SegregatedFrom.NAME, USegregatedFrom.NAME,
				BoundedWith.NAME));
		mDFPatterns = new HashSet<String>();
		mDFPatterns.addAll(DataflowPattern.getPatternDescription().keySet());
	}
	
	public ArrayList<Parameter> getParameters(String patternName) {
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		if (mOneParameterCFPatterns.contains(patternName)) {
			
			ArrayList<String> paramNames = new ArrayList<String>(Arrays.asList("P"));
			addControlFlowParameters(parameters, 1, paramNames);
			
		} else if (mTwoParameterCFPatterns.contains(patternName)) {
			
			ArrayList<String> paramNames = new ArrayList<String>(Arrays.asList("P", "Q"));
			addControlFlowParameters(parameters, 2, paramNames);
			
		} else if (mThreeParameterCFPatterns.contains(patternName)) {
			ArrayList<String> paramNames = new ArrayList<String>(Arrays.asList("P", "Q", "R"));
			addControlFlowParameters(parameters, 3, paramNames);
			
		} else if (mDFPatterns.contains(patternName)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TOKEN)),1, "D"));
			
		} else if (mTwoTransitionRP.contains(patternName)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1, "T1"));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1, "T2"));
			
		} else if (patternName.equals(PerformedBy.NAME)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1, "T"));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.ROLE)), 1 , "R"));
			
		} else if (patternName.equals(RBoundedWith.NAME)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1, "T1"));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1, "T2"));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.ROLE)),1, "R"));
			
		} else if (patternName.equals(MSegregated.NAME)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)), -1, "(T1, ..., TN)"));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.ROLE)), -1, "(R1, ..., RM)"));
			
		} else {
			try {
				throw new UnsupportedPattern("The given pattern is not supported! "
						+ "Extend the Implementation of the ParameterProvider class.");
			} catch (UnsupportedPattern e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return parameters;
		
	}

	private void addControlFlowParameters(ArrayList<Parameter> parameters, int i, ArrayList<String> paramNames) {
		
		for (int j = 0; j < i; j++) {
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.STATEPREDICATE, OperandType.TRANSITION)), 1, paramNames.get(j)));	
		}
	}

}
