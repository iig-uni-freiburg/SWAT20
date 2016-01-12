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

public class PTNetPrecedesChain extends CompliancePattern {
	
	public PTNetPrecedesChain() {
		ArrayList<String> paramTypes = new ArrayList<>( 
				Arrays.asList(ParameterTypeNames.TRANSITION));
		mParameters.add(new Parameter(paramTypes, "P"));
		mParameters.add(new Parameter(paramTypes, "Q"));
		mParameters.add(new Parameter(paramTypes, "R")); 
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		PTNetInfoProvider ptnetInfo = (PTNetInfoProvider) provider;
		mInfoProvider = ptnetInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions());
		mParameters.get(1).setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions());
		mParameters.get(2).setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions());
	}

	@Override
	public String getName() {
		return "P Precedes-Chain (Q, R)";
	}

	@Override
	public String getDescription() {
		return "P precedes a sequence of Q, R";
	}

	@Override
	public CompliancePattern duplicate() {
		PTNetPrecedesChain duplicate = new PTNetPrecedesChain();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	public void setFormalization() {
		String paramValue1 = mParameters.get(0).getValue().getValue();
		String paramType1 = mParameters.get(0).getValue().getType();
		String paramValue2 = mParameters.get(1).getValue().getValue();
		String paramType2 = mParameters.get(1).getValue().getType();
		String paramValue3 = mParameters.get(2).getValue().getValue();
		String paramType3 = mParameters.get(2).getValue().getType();
		String operand1, operand2, operand3;
		
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
		
		if (paramType3 == ParameterTypeNames.TRANSITION) {
			int transitionId3 = TransitionToIDMapper.getID(paramValue3);
			operand3 = PrismModelAdapter.transitionVarName + "=" + transitionId3;
		} else {
			operand3 = paramValue3;
		}
		
		mFormalization = "P=? [(F(("+operand2+") & (X(F("+operand3+"))))) => ((!("+operand2+")) U ("+operand1+"))]";
		

	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}