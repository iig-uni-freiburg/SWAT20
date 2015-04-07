package de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.gui;

import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.parameter.ParameterTypeNames;

public class ParaValuePanelFactory {

	public static ParameterValuePanel createPanel(Object selectedItem, Parameter parameter) {
		
		String parameterTypeStr = (String) selectedItem;
		parameter.setValue(parameterTypeStr);
		ParameterValuePanel panel = null;
		
		if (parameterTypeStr.equals(ParameterTypeNames.STATEPREDICATE)) {
			panel = new StatePredicateParamValuePanel(parameter);
		} else {
			panel = new StandardParamValuePanel(parameter);
		} 
		
		return panel;
	}

}
