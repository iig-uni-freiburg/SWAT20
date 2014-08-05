package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;

public class PatternParameterPanelFactory {

	private PetriNetInformationReader netInformation;
	private LogInformationReader logInformation;
	private boolean isNet;
	public PatternParameterPanel createPanel(Parameter p) {
		PatternParameterPanel patternPara=null;
		String name=p.getName();
		Set<OperandType> operandSet=p.getTypes();
		String transitionsArray[]=netInformation.getActivitiesArray();
		String dataTypeArray[]=netInformation.getDataTypesArray();
		if(operandSet.contains(OperandType.TOKEN)) {
			patternPara = new PatternDropDownParameter(name, OperandType.TOKEN, dataTypeArray);
		} else if (operandSet.contains(OperandType.TRANSITION) && operandSet.contains(OperandType.STATEPREDICATE)) {
			if(p.getMultiplicity() == -1) {
				patternPara=new MultipleTransitionOrStatePredicateParameterPanel(name, "Transition or State Predicate", netInformation);
			} else {
				patternPara=new PatternActivityOrStatePredicateParameter(name, netInformation);
			}
		} else if (operandSet.contains(OperandType.TRANSITION)) {
			if(p.getMultiplicity() == 1) {
				patternPara=new PatternDropDownParameter(name, OperandType.TRANSITION, transitionsArray);
			} else if(p.getMultiplicity() != -1) {
				patternPara=new MultipleTransitionParameterPanel(name, "Transition", netInformation);
			} else {
				patternPara=new MultipleTransitionParameterPanel(name, "Transition", netInformation, p.getMultiplicity());
			}
		} else if (operandSet.contains(OperandType.ROLE)) {
			
		}
		return patternPara;
	}
	public PatternParameterPanelFactory(PetriNetInformationReader netInformation) {
		this.netInformation = netInformation;
		isNet=true;
	}
	public PatternParameterPanelFactory(LogInformationReader logInformation) {
		this.logInformation = logInformation;
		isNet=false;
	}
}
