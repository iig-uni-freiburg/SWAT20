package de.uni.freiburg.iig.telematik.swat.lukas.operands;

import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.modelconvertor.PrismTranslator;

public class Activity extends NetElementExpression {
	
	private String mTransition;
	private String mName;

	public Activity(String name) {
		int id = TransitionToIDMapper.getID(name);
		mTransition = "(" + PrismTranslator.transitionVarName + "=" + id + ")";
		mName = name;
	}
	
	public String toString() {
		return mTransition;	
	}
	
	@Override
	public String getNegation() {
		int id = TransitionToIDMapper.getID(mName);
		String negTransition = "(" + PrismTranslator.transitionVarName + "!=" + id + ")";
		return negTransition;
	}

	@Override
	public String getName() {
		return mName;
	}

}
