package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ifnet;

import java.util.ArrayList;
import java.util.Arrays;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.Helpers;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.IFNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.Absent;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;


public class IFNetAbsent extends Absent {

	public IFNetAbsent() {
		ArrayList<String> paramTypes = new ArrayList<String>( 
				Arrays.asList(ParameterTypeNames.STATEPREDICATE, ParameterTypeNames.TRANSITION));
		mParameters.add(new Parameter(paramTypes, "P"));
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		
		IFNetInfoProvider ifnetInfo = (IFNetInfoProvider) provider;
		mInfoProvider = ifnetInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.TRANSITION, ifnetInfo.getTransitions());
		ArrayList<String> range = new ArrayList<String>(); 
		for (String place : ifnetInfo.getPlaces()) {
			ArrayList<String> colors = ifnetInfo.getTokenColors(place);
			for (String color : colors) {
				range.add(place + "-" + color);
			}
		}
		p.setTypeRange(ParameterTypeNames.STATEPREDICATE, range);
		//p.setTypeRange(ParameterTypeNames.USER, ifnetInfo.getSubjects());
	}

	@Override
	public CompliancePattern duplicate() {
		IFNetAbsent duplicate = new IFNetAbsent();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	protected void setFormalization() {
		String paramValue = Helpers.cutOffLabelInfo(mParameters.get(0).getValue().getValue());
		String paramType = mParameters.get(0).getValue().getType();
		if (paramType == ParameterTypeNames.TRANSITION) {
			int transitionId = TransitionToIDMapper.getID(paramValue);
			mFormalization = "P=? [G(!(" + PrismModelAdapter.transitionVarName + "="+ transitionId + "))]\n\n" 
					+ "A[G " + PrismModelAdapter.transitionVarName + "!="+ transitionId + "]";
		} else {
			mFormalization = "P=? [G(!(" + paramValue + "))]\n\n" 
					+ "A[G !(" + paramValue + ")]";
		}
		
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
