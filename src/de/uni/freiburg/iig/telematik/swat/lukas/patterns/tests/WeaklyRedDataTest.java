package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.WeaklyRedData;

public class WeaklyRedDataTest {

	@Test
	public void test0() {
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		WeaklyRedData p = new WeaklyRedData(new Token("blue"),
				net.getRegularTransitions(), net.getDrainPlaces());
		
		TestUtils tu = new TestUtils(net, p);
		try {
			// the blue token is never written
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		WeaklyRedData p = new WeaklyRedData(new Token("green"),
				net.getRegularTransitions(), net.getDrainPlaces());
		
		TestUtils tu = new TestUtils(net, p);
		try {
			// the green token is written by tOut and never read until termination
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		WeaklyRedData p = new WeaklyRedData(new Token("red"),
				net.getRegularTransitions(), net.getDrainPlaces());
		
		TestUtils tu = new TestUtils(net, p);
		try {
			// the red token is written by tIn, but it is not read in all successive paths
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		IFNet net = IFNetTestUtils.createParallelExecIFNet();
		WeaklyRedData p = new WeaklyRedData(new Token("red"),
				net.getRegularTransitions(), net.getDrainPlaces());
		
		TestUtils tu = new TestUtils(net, p);
		try {
			// the red token is written by tIn, but it is not read in all successive paths
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}

}
