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

public class NeueRegel extends CompliancePattern {
	
	public NeueRegel() {
		ArrayList<String> paramTypes = new ArrayList<>( 
				Arrays.asList(ParameterTypeNames.TRANSITION)); //Wir wollen, dass der Benituzer  nur Transitionen auswählen kann
		mParameters.add(new Parameter(paramTypes, "T1")); //Erster Satz an Drop-Down Felder
		mParameters.add(new Parameter(paramTypes, "T2")); //Zweiter Satz an Drop-Down Felder
	}

	@Override
	//ModelInforProvider hält Informationen über das Petri-Netz (Liste aller Transitionen)
	public void acceptInfoProfider(ModelInfoProvider provider) {
		PTNetInfoProvider ptnetInfo = (PTNetInfoProvider) provider;
		mInfoProvider = ptnetInfo;
		Parameter p = mParameters.get(0);
		p.setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions());
		mParameters.get(1).setTypeRange(ParameterTypeNames.TRANSITION, ptnetInfo.getTransitions()); //was soll in das 2. DropDown Feld?
		//p.setTypeRange(ParameterTypeNames.STATEPREDICATE, ptnetInfo.getPlaces());

	}

	@Override
	public String getName() {
		return"neues pattern";
	}

	@Override
	public String getDescription() {
		return"neues pattern";
	}

	@Override
	public CompliancePattern duplicate() {
		NeueRegel result = new NeueRegel();
		result.acceptInfoProfider(mInfoProvider);
		return result;
	}

	@Override
	public void setFormalization() {
		String paramValueT1 = mParameters.get(0).getValue().getValue();//T1(Benutzereingabe)
		String paramType = mParameters.get(0).getValue().getType();//T1 Typ(nur Transition möglich)
		String paramValueT2=mParameters.get(1).getValue().getValue();//T2 Benutzereingabe
		String paramTypeT2=mParameters.get(1).getValue().getType();//T2 Typ
		
		
		if (paramType == ParameterTypeNames.TRANSITION) {
			int transitionId = TransitionToIDMapper.getID(paramValueT1); //welche ID im Prism-Modell entspricht der Transition
			mFormalization = "P=? [G(!(" + PrismModelAdapter.transitionVarName + "="+ transitionId + "))]\n\n" //Erzeuge das LTL-Pattern als String
					+ "A[G " + PrismModelAdapter.transitionVarName + "!="+ transitionId + "]"; //zusätzlich das Pattern als CTL-Formel
		} else {
			mFormalization = "P=? [G(!(" + paramValueT1 + "))]\n\n" 
					+ "A[G !(" + paramValueT1 + ")]";
		}
		

	}

	@Override
	public boolean isAntiPattern() {
		return false;
	}

}
