package de.uni.freiburg.iig.telematik.swat.analysis;

import de.invation.code.toval.misc.NamedComponent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;

public class Analysis implements Comparable<Analysis>, NamedComponent {

	private String name = null;
	private List<CompliancePattern> patternSetting = new LinkedList<CompliancePattern>();
	private int hashCode = 0;
	
	public Analysis(String name, List<CompliancePattern> patternSetting) {
		super();
		Validate.notNull(name);
		Validate.notNull(patternSetting);
		this.name = name;
		this.patternSetting = patternSetting;
		//add only instantiated patterns
		//		for (CompliancePattern p : patternSetting) {
		//			if (p.isInstantiated())
		//				this.patternSetting.add(p);
		//		}
	}

	public String getName() {
		return name;
	}
        
        public void setName(String name){
            Validate.notNull(name);
            this.name = name;
        }

	public ArrayList<CompliancePattern> getPatternSetting() {
		System.out.println("Returning pattern with: " + patternSetting.get(0).getParameters().get(0).getValue().getValue());
		return new ArrayList<CompliancePattern>(patternSetting);
	}
	
	public String toString() {
		return name;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		try{
		this.hashCode = hashCode;
		} catch (RuntimeException e) {
			hashCode = 0;
		}
	}

	@Override
	public int compareTo(Analysis o) {
		if (o instanceof Analysis)
			return ((Analysis) o).getName().compareTo(getName());
		return 0;
	}
	
	public void setFormalizationOnPatterns() {
		for (CompliancePattern p : patternSetting)
			p.setFormalization();
	}

	public void setLoadedFromDisk() {
		for (CompliancePattern p : patternSetting)
			p.setLoadedFromDisk(true);
	}

}
