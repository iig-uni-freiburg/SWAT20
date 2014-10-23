package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.ParamValue;

public class TransitionParaPNet extends TransitionParameter {
	

	private PetriNetInformation mPnetInfoProvider;

	public TransitionParaPNet(String name, PetriNetInformation pnetInfoProvider) {
		super(name, pnetInfoProvider.getActivities());
		mPnetInfoProvider = pnetInfoProvider;
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		HashMap<String,String> transitionLabelDic=mPnetInfoProvider.getLabelToTransitionDictionary();
		String val=(String) valueBox.getSelectedItem();
		val=transitionLabelDic.get(val);
		ParamValue newVal=new ParamValue(val,type);
		ArrayList<ParamValue> list=new ArrayList<ParamValue>();
		list.add(newVal);
		return list;
	}

	@Override
	public void setValue(List<ParamValue> value) {
		HashMap<String,String> transitionLabelDic=mPnetInfoProvider.getTransitionToLabelDictionary();
		String val=transitionLabelDic.get(value.get(0).getOperandName());
		valueBox.setSelectedItem(val);
	}


}
