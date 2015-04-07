package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.ptnet;

import java.util.ArrayList;
import java.util.Arrays;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.model_info_provider.PTNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.Absent;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.ParameterTypeNames;


public class PTNetAbsent extends Absent {

	public PTNetAbsent() {
		ArrayList<String> paramTypes = new ArrayList<String>( 
				Arrays.asList(ParameterTypeNames.STATEPREDICATE, ParameterTypeNames.TRANSITION));
		mParameters.add(new Parameter(paramTypes, "P"));
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		
		PTNetInfoProvider ptnetInfo = (PTNetInfoProvider) provider;
		mInfoProvider = ptnetInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions());
		p.setTypeRange(ParameterTypeNames.STATEPREDICATE, ptnetInfo.getPlaces());
		
	}

	@Override
	public CompliancePattern duplicate() {
		PTNetAbsent duplicate = new PTNetAbsent();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	protected void setFormalization() {
		String paramValue = mParameters.get(0).getValue().getValue();
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
