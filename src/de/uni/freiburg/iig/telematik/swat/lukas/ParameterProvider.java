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
		mThreeParameterCFPatterns = new HashSet<String>(Arrays.asList(ChainPrecedes.NAME, ChainLeadsTo.NAME));
		mTwoTransitionRP = new HashSet<String>(Arrays.asList(SegregatedFrom.NAME, USegregatedFrom.NAME,
				BoundedWith.NAME));
		HashSet<String> mDFPatterns = new HashSet<String>();
		mDFPatterns.addAll(DataflowPattern.getPatternNames());
	}
	
	public ArrayList<Parameter> getParameters(String patternName) {
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		if (mOneParameterCFPatterns.contains(patternName)) {
			
			addControlFlowParameters(parameters, 1);
			
		} else if (mTwoParameterCFPatterns.contains(patternName)) {
			
			addControlFlowParameters(parameters, 2);
			
		} else if (mThreeParameterCFPatterns.contains(patternName)) {
			
			addControlFlowParameters(parameters, 3);
			
		} else if (mDFPatterns.contains(patternName)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TOKEN)),1));
			
		} else if (mTwoTransitionRP.contains(patternName)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1));
			
		} else if (patternName.equals(PerformedBy.NAME)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.ROLE)),1));
			
		} else if (patternName.equals(RBoundedWith.NAME)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)),1));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.ROLE)),1));
			
		} else if (patternName.equals(MSegregated.NAME)) {
			
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.TRANSITION)), -1));
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.ROLE)), -1));
			
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

	private void addControlFlowParameters(ArrayList<Parameter> parameters, int i) {
		
		for (int j = 0; j < i; j++) {
			parameters.add(new Parameter(new HashSet<OperandType>(
					Arrays.asList(OperandType.STATEPREDICATE, OperandType.TRANSITION)), 1));	
		}
	}

}
