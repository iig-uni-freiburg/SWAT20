package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
/**
 * This Class represents the Parameter for a state predicate which consists
 * of several StatePredicateStatements
 * @author bernhard
 *
 */
public class StatePredicateParameter extends MultipleParameterPanel {
	/**
	 * Create a StatePredicateParameter with a given name and a given
	 * PNReader
	 * @param name the name of the Parameter
	 * @param informationReader an object implementing the interface PNReader
	 */
	public StatePredicateParameter(String name,
			PNReader informationReader) {
		super(name, "Condition", informationReader);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ParameterPanel getNewPanel() {
		PNReader pnInformation = (PNReader) informationReader;
		return new StatePredicateStatement(name,pnInformation);
	}
	
	/**
	 * return a StatePredicate as p1 >= 0 & p3 < 3
	 */
	@Override
 	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> value=new ArrayList<ParamValue>();
		String val="";
		for(int i=0; i < panelList.size(); i++) {
			val+=panelList.get(i).getValue().get(0).getOperandName();
			if(i < panelList.size() -1 ) {
				val+=" & ";
			}
		}
		value.add(new ParamValue(val,OperandType.STATEPREDICATE));
		return value;
	}
	@Override
	public void setValue(List<ParamValue> value) {
		// TODO Auto-generated method stub
		panelList.clear();
		content.removeAll();
		String conjunctions[]=value.get(0).getOperandName().split(" & ");
		for(String conjunction: conjunctions) {
			ParameterPanel p=addParameter();
			ParamValue newValue=new ParamValue(conjunction,OperandType.STATEPREDICATE);
			ArrayList<ParamValue> list=new ArrayList<ParamValue>();
			list.add(newValue);
			p.setValue(list);
		}
		updateContent();
	}
}
