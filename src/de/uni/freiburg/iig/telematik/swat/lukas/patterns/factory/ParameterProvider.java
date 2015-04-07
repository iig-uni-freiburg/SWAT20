package de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Absent;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.BoundedWith;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainLeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainPrecedes;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CoAbsent;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CoExists;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Corequisite;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.DataflowAntiPattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Else;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Exclusive;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Exists;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsToChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.MSegregated;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.MutexChoice;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PBNI;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PLeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PerformedBy;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Precedes;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PrecedesChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.RBoundedWith;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ReadUp;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.SegregatedFrom;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.USegregatedFrom;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Universal;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.WriteDown;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.XLeadsTo;

public class ParameterProvider {
	
	HashSet<String> mNoParameterIFPatterns;
	HashSet<String> mOneParameterCFPatterns;
	HashSet<String> mTwoParameterCFPatterns;
	HashSet<String> mThreeParameterCFPatterns;
	HashSet<String> mDFPatterns;
	HashSet<String> mTwoTransitionRP;
	
	public ParameterProvider() {
		
		mNoParameterIFPatterns = new HashSet<String>(Arrays.asList(ReadUp.NAME, WriteDown.NAME, 
				PBNI.NAME)); 
		mOneParameterCFPatterns = new HashSet<String>(Arrays.asList(Exists.NAME, Absent.NAME));
		mTwoParameterCFPatterns = new HashSet<String>(Arrays.asList(Precedes.NAME, LeadsTo.NAME, XLeadsTo.NAME, 
				CoExists.NAME, CoAbsent.NAME, Exclusive.NAME, Corequisite.NAME, MutexChoice.NAME, PLeadsTo.NAME));
		mThreeParameterCFPatterns = new HashSet<String>(Arrays.asList(ChainPrecedes.NAME, ChainLeadsTo.NAME, PrecedesChain.NAME, LeadsToChain.NAME));
		mTwoTransitionRP = new HashSet<String>(Arrays.asList(SegregatedFrom.NAME, USegregatedFrom.NAME,
				BoundedWith.NAME));
		mDFPatterns = new HashSet<String>();
		mDFPatterns.addAll(DataflowAntiPattern.getPatternDescription().keySet());
	}
	
	public ArrayList<GuiParameter> getParameters(String patternName) {
		
		ArrayList<GuiParameter> parameters = new ArrayList<GuiParameter>();
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
			
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.TOKEN)),1, "D"));
			
		} else if (mTwoTransitionRP.contains(patternName)) {
			
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY)),1, "T1"));
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY)),1, "T2"));
			
		} else if (patternName.equals(PerformedBy.NAME)) {
			
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY)),1, "T"));
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ROLE)), 1 , "R"));
			
		} else if (patternName.equals(RBoundedWith.NAME)) {
			
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY)),1, "T1"));
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY)),1, "T2"));
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ROLE)),1, "R"));
			
		} else if (patternName.equals(MSegregated.NAME)) {
			
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY)), -1, "(T1, ..., TN)"));
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ROLE)), -1, "(R1, ..., RM)"));
			
		} else if (patternName.equals(Else.NAME)) {
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY, GuiParamType.STATEPREDICATE)), 1, "P"));
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.ACTIVITY, GuiParamType.STATEPREDICATE)), -1, "Q, R"));
		} else if (patternName.equals(Universal.NAME)) {
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.STATEPREDICATE)), 1, "P"));
		} else if (!mNoParameterIFPatterns.contains(patternName)) {
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

	private void addControlFlowParameters(ArrayList<GuiParameter> parameters, int i, ArrayList<String> paramNames) {
		
		for (int j = 0; j < i; j++) {
			parameters.add(new GuiParameter(new HashSet<GuiParamType>(
					Arrays.asList(GuiParamType.STATEPREDICATE, GuiParamType.ACTIVITY)), 1, paramNames.get(j)));	
		}
	}

}
