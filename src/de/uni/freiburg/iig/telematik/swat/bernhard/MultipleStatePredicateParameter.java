package de.uni.freiburg.iig.telematik.swat.bernhard;

public class MultipleStatePredicateParameter extends PatternMultipleParameterPanel {

	public MultipleStatePredicateParameter(String name, String description,
			String[] pvalues) {
		super(name, description, pvalues);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected PatternParameterPanel addParameter() {
		// TODO Auto-generated method stub
		PatternParameterPanel p=new PatternStatePredicateParameter(name,values);
		panelList.add(p);
		updateContent();
		return p;
	}
}
