package de.uni.freiburg.iig.telematik.swat.jascha;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**This class is intended only for the special case where we want to create Activity/CompoundResource pairs from logs.
 * To easily compare if an activity was already paired with the CompoundResource (consisting of a user and a material resource) in question,
 * this can be used with HashSet<Compound>;
 * @author Jascha
 *
 */
public final class Compound {
	
	final private String activity;
	final private String human;
	final private String material;
	
	public Compound (String activity, String human, String material){
		this.activity = activity;
		this.human = human;
		this.material = material;
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(71, 37).append(activity).append(human).append(material).toHashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof Compound)){
			return false;
		}
		if (obj == this){
			return true;
		}		
		Compound comp = (Compound)obj;
		if (comp.getActivity().equals(activity)&&
				comp.getHuman().equals(human)&&
				comp.getMaterial().equals(material)){
			return true;
		}
		return false;
	}
	
	public String getActivity() {
		return activity;
	}

	public String getHuman() {
		return human;
	}

	public String getMaterial() {
		return material;
	}
	
	public String compoundToString(){
		return activity+human+material;
	}

}
