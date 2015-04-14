package de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ifnet;

import java.util.ArrayList;
import java.util.Arrays;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.Helpers;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.IFNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.ModelInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.LeadsTo;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;

/**
 * 
 * @author Lukas Sättler
 * 
 * The "P LeadsTo Q" pattern requires that the occurrence of event P is followed by t
 * he occurrence of event Q.  
 *
 */

public class IFNetLeadsTo extends LeadsTo {
	
	public IFNetLeadsTo() {
		ArrayList<String> paramTypes = new ArrayList<String>( 
				Arrays.asList(ParameterTypeNames.STATEPREDICATE, ParameterTypeNames.TRANSITION));
		
		/* Each parameter of the LeadsTo pattern has to be added to the mParameters list. 
		 All possible parameter types are specified in the class ParameterTypeNames. 
		 The set of valid parameter types depends on the pattern and the model (in this case an ifnet).
		 The parameter types are displayed in a dropdown-box in the pattern dialog later on.*/
		
		mParameters.add(new Parameter(paramTypes, "P"));
		mParameters.add(new Parameter(paramTypes, "Q"));
	}

	@Override
	public void acceptInfoProfider(ModelInfoProvider provider) {
		
		/* Each parameter has a certain value range. For example the range of a parameter of type transition
		 * is (t1,t2,t3) if the ifnet contains three transitions t1, t2 and t3. A ModelInfoProvider which is 
		 * created for the currently selected/ active model, stores all the necessary informations to set 
		 * the range of a parameter for each parameter type.*/
		
		IFNetInfoProvider ifnetInfo = (IFNetInfoProvider) provider;
		
		// the ModelInfoProvider must be stored in order to use it in the duplicate-method.
		mInfoProvider = ifnetInfo;
		for (Parameter p : mParameters) {
			p.setTypeRange(ParameterTypeNames.TRANSITION, ifnetInfo.getTransitions());
			ArrayList<String> range = new ArrayList<String>(); 
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
		IFNetLeadsTo duplicate = new IFNetLeadsTo();
		duplicate.acceptInfoProfider(mInfoProvider);
		return duplicate;
	}

	@Override
	protected void setFormalization() {
		
		// the value of the first parameter which has been set in the pattern dialog.
		String paramValue1 = Helpers.cutOffLabelInfo(mParameters.get(0).getValue().getValue());
		// the type of the first parameter which has been set in the pattern dialog.
		String paramType1 = mParameters.get(0).getValue().getType();
		// the value of the second parameter which has been set in the pattern dialog.
		String paramValue2 = Helpers.cutOffLabelInfo(mParameters.get(1).getValue().getValue());
		// the type of the second parameter which has been set in the pattern dialog.
		String paramType2 = mParameters.get(1).getValue().getType();
		String operand1, operand2;
		
		if (paramType1 == ParameterTypeNames.TRANSITION) {
			int transitionId1 = TransitionToIDMapper.getID(paramValue1);
			operand1 = PrismModelAdapter.transitionVarName + "="+ transitionId1;
		} else {
			operand1 = paramValue1;
		}
		
		if (paramType2 == ParameterTypeNames.TRANSITION) {
			int transitionId2 = TransitionToIDMapper.getID(paramValue2);
			operand2 = PrismModelAdapter.transitionVarName + "="+ transitionId2;
		} else {
			operand2 = paramValue2;
		}
		
		mFormalization = "P=? [G((" + operand1 + ") => (" + "F(" + operand2 + ")))]";
		
	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
