package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.Parameter;
/**
 * This class is used to create the parameterpanel for a given
 * Parameter.
 * @author bernhard
 *
 */
public class ParameterPanelFactory {

	private AnalysisComponentInfoProvider componentInfo;
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
					OperandType.TOKEN, ((PetriNetInformation) componentInfo)
					.getDataTypesArray());
		} else if (operandSet.contains(OperandType.TRANSITION)
				&& operandSet.contains(OperandType.STATEPREDICATE)) {
			if (p.getMultiplicity() == -1) {
				patternPara = new MultipleTransitionOrStatePredicateParameterPanel(
						name, "Transition or State Predicate",
						(PetriNetInformation) componentInfo);
			} else {
				patternPara = new ActivityOrStatePredicateParameter(
						name, (PetriNetInformation) componentInfo);
			}
		} else if (operandSet.contains(OperandType.TRANSITION)) {
			if (p.getMultiplicity() == 1) {
				
				if (componentInfo instanceof PetriNetInformation) {
					patternPara = new TransitionParaPNet(name, (PetriNetInformation) componentInfo);
				} else {
					patternPara = new TransitionParaLogFile(name, (LogFileInformation) componentInfo);
				}
		
				
			} else if (p.getMultiplicity() != -1) {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", componentInfo);
			} else {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", componentInfo, p.getMultiplicity());
			}
		} else if (operandSet.contains(OperandType.STATEPREDICATE)) {
			patternPara = new StatePredicateParameter(name,
					((PetriNetInformation) componentInfo));
		} else if (operandSet.contains(OperandType.ROLE)) {
			patternPara = new DropDownParameter(name,
					OperandType.ROLE, componentInfo.getRoles());
		}
		return patternPara;
	}
	/**
	 * Create a ParameterPanelFactory for an Object implementing
	 * the interface LogFileReader
	 * @param logInformation
	 */
	public ParameterPanelFactory(AnalysisComponentInfoProvider logInformation) {
		this.componentInfo = logInformation;
	}
}
