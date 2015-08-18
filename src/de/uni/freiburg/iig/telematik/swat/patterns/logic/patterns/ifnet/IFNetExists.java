package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ifnet;

import java.util.ArrayList;
import java.util.Arrays;

import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.Helpers;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.IFNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.Exists;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class IFNetExists extends Exists {
	
	public IFNetExists() {
		ArrayList<String> paramTypes = new ArrayList<>( 
				Arrays.asList(ParameterTypeNames.STATEPREDICATE, ParameterTypeNames.TRANSITION));
		mParameters.add(new Parameter(paramTypes, "P"));
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		
		IFNetInfoProvider ifnetInfo = (IFNetInfoProvider) provider;
		mInfoProvider = ifnetInfo;
		Parameter p = mParameters.get(0);
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

	@Override
	public CompliancePattern duplicate() {
		IFNetExists duplicate = new IFNetExists();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public void setFormalization() {
		String paramValue = Helpers.cutOffLabelInfo(mParameters.get(0).getValue().getValue());
		String paramType = mParameters.get(0).getValue().getType();
		if (paramType == ParameterTypeNames.TRANSITION) {
			int transitionId = TransitionToIDMapper.getID(paramValue);
			mFormalization = "P=? [F(" + PrismModelAdapter.transitionVarName + "="+ transitionId + ")]\n\n" 
					+ "E[F " + PrismModelAdapter.transitionVarName + "="+ transitionId + "]";
		} else {
			mFormalization = "P=? [F(" + paramValue + ")]\n\n" 
					+ "E[F (" + paramValue + ")]";
		}
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
