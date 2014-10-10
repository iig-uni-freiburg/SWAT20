package de.uni.freiburg.iig.telematik.swat.misc;

import java.io.File;
import java.util.Comparator;
import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;

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

	public int compare(SwatComponent comp1, SwatComponent comp2) {
		return comp1.getName().compareTo(comp2.getName());
	}

	public int compare(LogModel comp1, LogModel comp2) {
		return comp1.getName().compareTo(comp2.getName());
	}

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 instanceof SwatComponent)
			return compare((SwatComponent) o1, (SwatComponent) o2);
		if (o1 instanceof LogModel)
			return compare((LogModel) o1, (LogModel) o2);
		else
			return compare((AbstractGraphicalPN) o1, (AbstractGraphicalPN) o2);
	}
}

