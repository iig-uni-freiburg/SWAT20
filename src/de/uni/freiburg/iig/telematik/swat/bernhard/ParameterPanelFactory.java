package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;

public class ParameterPanelFactory {

	private InformationReader objectInformation;

	public ParameterPanel createPanel(Parameter p) {
		ParameterPanel patternPara = null;
		String name = p.getName();
		Set<OperandType> operandSet = p.getTypes();
		String activitiesArray[] = objectInformation.getActivitiesArray();
		if (operandSet.contains(OperandType.TOKEN)) {
			patternPara = new DropDownParameter(name,
					OperandType.TOKEN, ((PetriNetInformationReader) objectInformation)
					.getDataTypesArray());
		} else if (operandSet.contains(OperandType.TRANSITION)
				&& operandSet.contains(OperandType.STATEPREDICATE)) {
			if (p.getMultiplicity() == -1) {
				patternPara = new MultipleTransitionOrStatePredicateParameterPanel(
						name, "Transition or State Predicate",
						(PetriNetInformationReader) objectInformation);
			} else {
				patternPara = new ActivityOrStatePredicateParameter(
						name, (PetriNetInformationReader) objectInformation);
			}
		} else if (operandSet.contains(OperandType.TRANSITION)) {
			if (p.getMultiplicity() == 1) {
				patternPara = new TransitionParameter(name,(PetriNetInformationReader) objectInformation);
			} else if (p.getMultiplicity() != -1) {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", objectInformation);
			} else {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", objectInformation, p.getMultiplicity());
			}
		} else if (operandSet.contains(OperandType.STATEPREDICATE)) {
			patternPara = new StatePredicateParameter(name,
					((PetriNetInformationReader) objectInformation));
		} else if (operandSet.contains(OperandType.ROLE)) {
			patternPara = new DropDownParameter(name,
					OperandType.TRANSITION, objectInformation.getRoleArray());
		}
		return patternPara;
	}

	public ParameterPanelFactory(InformationReader logInformation) {
		this.objectInformation = logInformation;
	}
}
