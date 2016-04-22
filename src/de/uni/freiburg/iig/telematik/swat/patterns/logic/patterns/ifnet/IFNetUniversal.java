package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ifnet;

import java.util.ArrayList;
import java.util.Arrays;

import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.Helpers;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.IFNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.Universal;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class IFNetUniversal extends Universal {
	
	public IFNetUniversal() {
		ArrayList<String> paramTypes = new ArrayList<>( 
				Arrays.asList(ParameterTypeNames.STATEPREDICATE, ParameterTypeNames.TRANSITION));		
		mParameters.add(new Parameter(paramTypes, "P"));
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		IFNetInfoProvider ifnetInfo = (IFNetInfoProvider) provider;
		mInfoProvider = ifnetInfo;
		for (Parameter p : mParameters) {
			p.setTypeRange(ParameterTypeNames.TRANSITION, ifnetInfo.getTransitions());
			ArrayList<String> range = new ArrayList<>(); 
			for (String place : ifnetInfo.getPlaces()) {
				ArrayList<String> colors = ifnetInfo.getTokenColors(place);
				for (String color : colors) {
					range.add(place + "-" + color);
				}
			}
			p.setTypeRange(ParameterTypeNames.STATEPREDICATE, range);
		}	
	}

	@Override
	public CompliancePattern duplicate() {
		IFNetUniversal duplicate = new IFNetUniversal();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public void setFormalization() {
		String paramValue1 = Helpers.cutOffLabelInfo(mParameters.get(0).getValue().getValue());
		String paramType1 = mParameters.get(0).getValue().getType();
		String operand1;
		
		if (paramType1 == ParameterTypeNames.TRANSITION) {
			int transitionId1 = TransitionToIDMapper.getID(paramValue1);
			operand1 = PrismModelAdapter.transitionVarName + "=" + transitionId1;
		} else {
			operand1 = paramValue1;
		}

		mFormalization = "P=? [ (G(" + operand1 + "))]";
	}


	@Override
	public boolean isAntiPattern() {
		return false;
	}
}