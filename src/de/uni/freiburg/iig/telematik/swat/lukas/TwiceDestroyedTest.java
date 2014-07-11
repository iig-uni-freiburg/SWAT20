package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class TwiceDestroyedTest {
	
	@Test
	public void test0() {
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		TwiceDestroyed p = new TwiceDestroyed(new Token("green"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test1() {
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		TwiceDestroyed p = new TwiceDestroyed(new Token("blue"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test3() {
		IFNet net = IFNetTestUtils.createParallelExecIFNet();
		TwiceDestroyed p = new TwiceDestroyed(new Token("green"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
