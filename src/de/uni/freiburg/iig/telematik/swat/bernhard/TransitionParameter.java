package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
/**
 * This Class represents a parameter panel where a Transition of an
 * PN can be chosen.
 * @author bernhard
 *
 */
public class TransitionParameter extends DropDownParameter {

	private PNReader pnReader;
	/**
	 * Create an TransitionParameter with a given name for a given PNReader.
	 * @param name the name of the parameter
	 * @param pnReader an object implementing the interface PNReader which is
	 * used to retrieve the list of transitions
	 */
	public TransitionParameter(String name,PNReader pnReader) {
		super(name, OperandType.TRANSITION, pnReader.getActivities());
		this.pnReader=pnReader;
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		HashMap<String,String> transitionLabelDic=pnReader.getLabelToTransitionDictionary();
		String val=(String) valueBox.getSelectedItem();
		val=transitionLabelDic.get(val);
		ParamValue newVal=new ParamValue(val,type);
		ArrayList<ParamValue> list=new ArrayList<ParamValue>();
		list.add(newVal);
		return list;
	}

	@Override
	public void setValue(List<ParamValue> value) {
		HashMap<String,String> transitionLabelDic=pnReader.getTransitionToLabelDictionary();
		String val=transitionLabelDic.get(value.get(0).getOperandName());
		valueBox.setSelectedItem(val);
	}

}
