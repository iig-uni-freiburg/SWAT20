package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.BoundedWithTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({AbsentTest.class, ChainLeadsToTest.class, ChainPrecedesTest.class, CoAbsentTest.class,
	CoExistsTest.class, CorequisiteTest.class, ExistsTest.class, ExclusiveTest.class, MutexChoiceTest.class, PrecedesTest.class, 
	UniversalTest.class, WeaklyLostDataTest.class, XLeadsToTest.class, PLeadsToTest.class, InconsistentDataTest.class, MissingDataTest.class,
	NeverDestroyedTest.class, NotDeletedOnTimeTest.class, TwiceDestroyedTest.class, MBoundedTest.class, USegregatedFromTest.class, BoundedWithTest.class
})
public class PatternTestSuite {

}
