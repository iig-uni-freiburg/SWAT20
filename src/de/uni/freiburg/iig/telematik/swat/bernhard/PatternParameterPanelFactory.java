package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;

public class PatternParameterPanelFactory {

	private InformationReader objectInformation;

	public PatternParameterPanel createPanel(Parameter p) {
		PatternParameterPanel patternPara = null;
		String name = p.getName();
		Set<OperandType> operandSet = p.getTypes();
		String activitiesArray[] = objectInformation.getActivitiesArray();
		if (operandSet.contains(OperandType.TOKEN)) {
			patternPara = new PatternDropDownParameter(name,
					OperandType.TOKEN, ((PetriNetInformationReader) objectInformation)
					.getDataTypesArray());
		} else if (operandSet.contains(OperandType.TRANSITION)
				&& operandSet.contains(OperandType.STATEPREDICATE)) {
			if (p.getMultiplicity() == -1) {
				patternPara = new MultipleTransitionOrStatePredicateParameterPanel(
						name, "Transition or State Predicate",
						(PetriNetInformationReader) objectInformation);
			} else {
				patternPara = new PatternActivityOrStatePredicateParameter(
						name, (PetriNetInformationReader) objectInformation);
			}
		} else if (operandSet.contains(OperandType.TRANSITION)) {
			if (p.getMultiplicity() == 1) {
				patternPara = new PatternDropDownParameter(name,
						OperandType.TRANSITION, activitiesArray);
			} else if (p.getMultiplicity() != -1) {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", objectInformation);
			} else {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", objectInformation, p.getMultiplicity());
			}
		} else if (operandSet.contains(OperandType.STATEPREDICATE)) {
			patternPara = new PatternStatePredicateParameter(name,
					((PetriNetInformationReader) objectInformation));
		} else if (operandSet.contains(OperandType.ROLE)) {
			patternPara = new PatternDropDownParameter(name,
					OperandType.TRANSITION, objectInformation.getRoleArray());
		}
		return patternPara;
	}

	public PatternParameterPanelFactory(InformationReader logInformation) {
		this.objectInformation = logInformation;
	}
}
