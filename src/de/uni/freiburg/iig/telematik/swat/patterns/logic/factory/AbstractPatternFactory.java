package de.uni.freiburg.iig.telematik.swat.patterns.logic.factory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import de.invation.code.toval.reflect.ReflectionException;
import de.invation.code.toval.reflect.ReflectionUtils;
import de.uni.freiburg.iig.telematik.swat.patterns.PatternException;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import java.util.LinkedHashSet;

public abstract class AbstractPatternFactory {

    public ArrayList<CompliancePattern> loadPatterns() throws PatternException {
        ArrayList<CompliancePattern> mSupportedPatterns = new ArrayList<>();

        LinkedHashSet<Class<?>> classes;
        try {
            classes = ReflectionUtils.getClassesInPackage(getPatternPackage(), true);
        } catch (ReflectionException e1) {
            throw new PatternException("Cannot load patterns", e1);
        }

        for (Class<?> cl : classes) {
            if (Modifier.isAbstract(cl.getModifiers())) {
                continue;
            }
            CompliancePattern cp;
            try {
                cp = (CompliancePattern) cl.newInstance();
                mSupportedPatterns.add(cp);
                Workbench.consoleMessage("Added pattern: " + cp.getName());
            } catch (InstantiationException e) {
                throw new PatternException("Cannot instantiate compliance pattern", e);
            } catch (IllegalAccessException e) {
                throw new PatternException("Cannot access class definition of compliance pattern", e);
            }
        }

        if (mSupportedPatterns.isEmpty()) {
            throw new PatternException("Cannot load any analysis pattern");
        }

        Collections.sort(mSupportedPatterns);

        return mSupportedPatterns;
    }

    protected abstract String getPatternPackage();

}
