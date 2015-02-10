package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParamValue;

public class TransitionParaPNet extends TransitionParameter {
	

	private PetriNetInformation mPnetInfoProvider;

	public TransitionParaPNet(String name, PetriNetInformation pnetInfoProvider) {
		super(name, pnetInfoProvider.getActivities());
		mPnetInfoProvider = pnetInfoProvider;
	}

	@Override
	public List<GuiParamValue> getValue() {
		// TODO Auto-generated method stub
		HashMap<String,String> transitionLabelDic=mPnetInfoProvider.getLabelToTransitionDictionary();
		String val=(String) valueBox.getSelectedItem();
		val=transitionLabelDic.get(val);
		GuiParamValue newVal=new GuiParamValue(val,type);
		ArrayList<GuiParamValue> list=new ArrayList<GuiParamValue>();
		list.add(newVal);
		return list;
	}

	@Override
	public void setValue(List<GuiParamValue> value) {
		HashMap<String,String> transitionLabelDic=mPnetInfoProvider.getTransitionToLabelDictionary();
		String val=transitionLabelDic.get(value.get(0).getOperandName());
		valueBox.setSelectedItem(val);
	}


}
