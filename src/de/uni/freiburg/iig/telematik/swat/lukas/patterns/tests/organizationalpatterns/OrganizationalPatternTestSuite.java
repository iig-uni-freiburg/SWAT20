package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns.SegregatedFromTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns.BoundedWithTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns.MBoundedTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns.USegregatedFromTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({MBoundedTest.class, USegregatedFromTest.class, 
	BoundedWithTest.class, BoundedWithTest.class, SegregatedFromTest.class
})

public class OrganizationalPatternTestSuite {

}