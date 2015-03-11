package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Else;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class ElseTest {
	
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		Activity tIn = new Activity("tIn");
		Activity td = new Activity("td");
		Activity t0 = new Activity("t0");
		Else p = new Else(tIn, td, t0);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.createIFNetWithGuards();
		TransitionToIDMapper.createMap(ifnet);
		Activity tIn = new Activity("tIn");
		Activity t1 = new Activity("t1");
		Activity t3 = new Activity("t3");
		Else p = new Else(tIn, t1, t3);
		
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
}
