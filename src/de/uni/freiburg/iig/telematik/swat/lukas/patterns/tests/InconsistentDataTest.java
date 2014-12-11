package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.InconsistentData;

public class InconsistentDataTest {
	
	@Test
	public void test0() {
		
		IFNet net = IFNetTestUtils.createParallelExecIFNet();
		InconsistentData p = new InconsistentData(new Token("red"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			
			assertTrue(tu.isPropertySatisfied());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test2() {
		
		IFNet net = IFNetTestUtils.create6PlaceIFNet();
		InconsistentData p = new InconsistentData(new Token("blue"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			
			assertFalse(tu.isPropertySatisfied());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test3() {
		
		IFNet net = IFNetTestUtils.createIFNetWithGuards();
		InconsistentData p = new InconsistentData(new Token("red"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			
			assertFalse(tu.isPropertySatisfied());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
}
