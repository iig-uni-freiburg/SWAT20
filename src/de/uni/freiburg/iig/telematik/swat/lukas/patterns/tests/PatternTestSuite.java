package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.AbsentTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.ChainLeadsToTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.ChainPrecedesTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.CoAbsentTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.CoExistsTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.CorequisiteTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.ExclusiveTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.ExistsTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.MutexChoiceTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.PLeadsToTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.PrecedesTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.UniversalTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns.XLeadsToTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.dataflowantipatterns.InconsistentDataTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.dataflowantipatterns.MissingDataTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.dataflowantipatterns.NeverDestroyedTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.dataflowantipatterns.NotDeletedOnTimeTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.dataflowantipatterns.TwiceDestroyedTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.dataflowantipatterns.WeaklyLostDataTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.informationflowpatterns.ReadUpTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.informationflowpatterns.WriteDownTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns.BoundedWithTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns.MBoundedTest;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns.USegregatedFromTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({AbsentTest.class, ChainLeadsToTest.class, ChainPrecedesTest.class, CoAbsentTest.class,
	CoExistsTest.class, CorequisiteTest.class, ExistsTest.class, ExclusiveTest.class, MutexChoiceTest.class, PrecedesTest.class, 
	UniversalTest.class, WeaklyLostDataTest.class, XLeadsToTest.class, PLeadsToTest.class, InconsistentDataTest.class, MissingDataTest.class,
	NeverDestroyedTest.class, NotDeletedOnTimeTest.class, TwiceDestroyedTest.class, MBoundedTest.class, USegregatedFromTest.class, 
	BoundedWithTest.class, ReadUpTest.class, WriteDownTest.class
})

public class PatternTestSuite {

}
