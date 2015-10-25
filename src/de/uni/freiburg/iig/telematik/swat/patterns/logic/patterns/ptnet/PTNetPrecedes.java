package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ptnet;

import java.util.ArrayList;
import java.util.Arrays;

import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.PTNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

public class PTNetPrecedes extends CompliancePattern {
	
	public PTNetPrecedes() {
		ArrayList<String> paramTypes = new ArrayList<>( 
				Arrays.asList(ParameterTypeNames.TRANSITION));
		mParameters.add(new Parameter(paramTypes, "T1"));
		mParameters.add(new Parameter(paramTypes, "T2")); 
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		PTNetInfoProvider ptnetInfo = (PTNetInfoProvider) provider;
		mInfoProvider = ptnetInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions());
		mParameters.get(1).setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions());
	}

	@Override
	public String getName() {
		return "P Precedes Q";
	}

	@Override
	public String getDescription() {
		return "Q must always be preceded by P";
	}

	@Override
	public CompliancePattern duplicate() {
		PTNetExclusive duplicate = new PTNetExclusive();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public void setFormalization() {
		String paramValue1 = mParameters.get(0).getValue().getValue();
		String paramType1 = mParameters.get(0).getValue().getType();
		String paramValue2 = mParameters.get(1).getValue().getValue();
		String paramType2 = mParameters.get(1).getValue().getType();
		String operand1, operand2;
		
		if (paramType1 == ParameterTypeNames.TRANSITION) {
			int transitionId1 = TransitionToIDMapper.getID(paramValue1);
			operand1 = PrismModelAdapter.transitionVarName + "=" + transitionId1;
		} else {
			operand1 = paramValue1;
		}
		
		if (paramType2 == ParameterTypeNames.TRANSITION) {
			int transitionId2 = TransitionToIDMapper.getID(paramValue2);
			operand2 = PrismModelAdapter.transitionVarName + "=" + transitionId2;
		} else {
			operand2 = paramValue2;
		}
		
		mFormalization = "P=? [ ((G(!(" + operand2 + "))) | ((!(" + operand2 + ")) U (" + operand1 +  ")))]";
		

	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}