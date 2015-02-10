package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.Arrays;
import java.util.Set;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParamType;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParameter;
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
	public ParameterPanel createPanel(GuiParameter p) {
		ParameterPanel patternPara = null;
		String name = p.getName();
		Set<GuiParamType> operandSet = p.getTypes();
		
		if (operandSet.contains(GuiParamType.TOKEN)) {
			patternPara = new DropDownParameter(name,
					GuiParamType.TOKEN, ((PetriNetInformation) componentInfo)
					.getDataTypesArray());
		} else if (operandSet.contains(GuiParamType.ACTIVITY)
				&& operandSet.contains(GuiParamType.STATEPREDICATE)) {
			if (p.getMultiplicity() == -1) {
				patternPara = new MultipleTransitionOrStatePredicateParameterPanel(
						name, "Transition or State Predicate",
						(PetriNetInformation) componentInfo);
			} else {
				patternPara = new ActivityOrStatePredicateParameter(
						name, (PetriNetInformation) componentInfo);
			}
		} else if (operandSet.contains(GuiParamType.ACTIVITY)) {
			if (p.getMultiplicity() == 1) {
				
				if (componentInfo instanceof PetriNetInformation) {
					patternPara = new TransitionParaPNet(name, (PetriNetInformation) componentInfo);
				} else { //LogModelInformation
					patternPara = new TransitionParaLogFile(name, (LogFileInformation) componentInfo);
				}
		
				
			} else if (p.getMultiplicity() != -1) {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", componentInfo);
			} else {
				patternPara = new MultipleTransitionParameterPanel(name,
						"Transition", componentInfo, p.getMultiplicity());
			}
		} else if (operandSet.contains(GuiParamType.STATEPREDICATE)) {
			patternPara = new StatePredicateParameter(name,
					((PetriNetInformation) componentInfo));
		} else if (operandSet.contains(GuiParamType.ROLE)) {
			//Get Roles AND Subjects
			patternPara = new DropDownParameter(name,GuiParamType.ROLE, getRolesAndSubject(componentInfo));
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

	private String[] getRolesAndSubject(AnalysisComponentInfoProvider componentInfo) {
		String[] result = Arrays.copyOf(componentInfo.getRoles(), componentInfo.getRoles().length + componentInfo.getSubjects().length);
		System.arraycopy(componentInfo.getSubjects(), 0, result, componentInfo.getRoles().length, componentInfo.getSubjects().length);
		return result;
	}
}
