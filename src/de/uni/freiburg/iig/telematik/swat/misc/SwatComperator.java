package de.uni.freiburg.iig.telematik.swat.misc;

import java.io.File;
import java.util.Comparator;
import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.logs.XESLogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchComponent;

/** For use with TreeMap **/
public class SwatComperator implements Comparator<Object> {
	// Note: this comparator imposes orderings that are inconsistent with equals. Only compares File Names   
	Map<String, File> base;

	/** because AbstractGraphicalPN does not carry its name:need mapping **/
	public SwatComperator(Map<String, File> base) {
		this.base = base;
	}

	public SwatComperator() {
		this.base = null;
	}

	public int compare(AbstractGraphicalPN a, AbstractGraphicalPN b) {
		return base.get(a).getName().compareTo(base.get(b).getName());
	} // returning 0 would merge keys

	public int compare(WorkbenchComponent comp1, WorkbenchComponent comp2) {
		return comp1.getName().compareTo(comp2.getName());
	}

	public int compare(XESLogModel comp1, XESLogModel comp2) {
		return comp1.getName().compareTo(comp2.getName());
	}

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 instanceof WorkbenchComponent)
			return compare((WorkbenchComponent) o1, (WorkbenchComponent) o2);
		if (o1 instanceof XESLogModel)
			return compare((XESLogModel) o1, (XESLogModel) o2);
		else
			return compare((AbstractGraphicalPN) o1, (AbstractGraphicalPN) o2);
	}
}

