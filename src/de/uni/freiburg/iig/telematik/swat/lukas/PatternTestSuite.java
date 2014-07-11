package de.uni.freiburg.iig.telematik.swat.lukas;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AbsentTest.class, ChainLeadsToTest.class, ChainPrecedesTest.class, CoAbsentTest.class,
	CoExistsTest.class, CorequisiteTest.class, ExistsTest.class, ExclusiveTest.class, InconsistentDataTest.class, MissingDataTest.class,
	MutexChoiceTest.class, NeverDestroyedTest.class, NotDeletedOnTimeTest.class, PrecedesTest.class, TwiceDestroyedTest.class, 
	UniversalTest.class, WeaklyLostDataTest.class, XLeadsToTest.class})

public class PatternTestSuite {

}
