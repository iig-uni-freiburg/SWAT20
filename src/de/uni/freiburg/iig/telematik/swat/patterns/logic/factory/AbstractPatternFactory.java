package de.uni.freiburg.iig.telematik.swat.patterns.logic.factory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import de.invation.code.toval.reflect.ReflectionUtils;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;

public abstract class AbstractPatternFactory {
	
	ArrayList<CompliancePattern> mSupportedPatterns;
	
	protected String mPackage;

	protected ArrayList<CompliancePattern> loadPatterns() {
		
		
			List<Class<?>> classes = ReflectionUtils.getClasses(mPackage);
			mSupportedPatterns = new ArrayList<CompliancePattern>();
			
			for (Class<?> cl : classes) {
				
				if (Modifier.isAbstract(cl.getModifiers())) continue;
				CompliancePattern cp;
				
				try {
					cp = (CompliancePattern) cl.newInstance();
					mSupportedPatterns.add(cp);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			return mSupportedPatterns;
			
	}
	
	
	public ArrayList<CompliancePattern> createPatterns() {
		return mSupportedPatterns;
	}
	
	

}
