package de.uni.freiburg.iig.telematik.swat.patterns.logic;

public class Helpers {

	public static String cutOffLabelInfo(String str) {
		
		int index1 = str.indexOf("(");
		int index2 = str.indexOf(")");
		
		String pref = str.substring(0, index1);
		String suf = str.substring(index2 + 1, str.length());
		
		str =  pref + suf;
		return str;
		
	}
}
