package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
/**
 * This class is used to create the parameterpanel for a given
 * Parameter.
 * @author bernhard
 *
 */
public class ParameterPanelFactory {

	private LogFileReader objectInformation;
	/**
	 * Create a ParameterPanel for a given Parameter.
	 * @param p the parameter for which the parameter panel should be 
	 * created
	 * @return a ParameterPanel which allows the user to select a value
	 * for the given Parameter
	 */
	public ParameterPanel createPanel(Parameter p) {
		ParameterPanel patternPara = null;
		String name = p.getName();
		Set<OperandType> operandSet = p.getTypes();
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
					OperandType.TRANSITION, objectInformation.getRoles());
		}
		return patternPara;
	}

	public ParameterPanelFactory(LogFileReader logInformation) {
		this.objectInformation = logInformation;
	}
}
