package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Else;

public class ElseTest {
	
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Transition tIn = new Transition("tIn");
		Transition td = new Transition("td");
		Transition t0 = new Transition("t0");
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
		Transition tIn = new Transition("tIn");
		Transition t1 = new Transition("t1");
		Transition t3 = new Transition("t3");
		Else p = new Else(tIn, t1, t3);
		
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
}
