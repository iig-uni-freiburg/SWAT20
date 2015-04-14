package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ptnet;

import java.util.ArrayList;
import java.util.Arrays;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.PTNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.Exists;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class PTNetExists extends Exists {
	
	public PTNetExists() {
		ArrayList<String> paramTypes = new ArrayList<String>( 
				Arrays.asList(ParameterTypeNames.STATEPREDICATE, ParameterTypeNames.TRANSITION));
		mParameters.add(new Parameter(paramTypes, "P"));
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		
		PTNetInfoProvider ptInfo = (PTNetInfoProvider) provider;
		mInfoProvider = ptInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.TRANSITION, ptInfo.getTransitions());
		ArrayList<String> range = new ArrayList<String>(); 
		for (String place : ptInfo.getPlaces()) {
			range.add(place);
		}
		p.setTypeRange(ParameterTypeNames.STATEPREDICATE, range);

	}

	@Override
	public CompliancePattern duplicate() {
		PTNetExists duplicate = new PTNetExists();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	protected void setFormalization() {
		String paramValue = mParameters.get(0).getValue().getValue();
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
