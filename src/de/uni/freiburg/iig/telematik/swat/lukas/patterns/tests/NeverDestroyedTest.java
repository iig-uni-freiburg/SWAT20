package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.NotDeletedOnTime;

public class NeverDestroyedTest {
	
	@Test
	public void test0() {
		
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		NotDeletedOnTime p = new NotDeletedOnTime(new Token("green"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test1() {
		
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		NotDeletedOnTime p = new NotDeletedOnTime(new Token("blue"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test2() {
		
		IFNet net = IFNetTestUtils.createIFNetWithGuards();
		NotDeletedOnTime p = new NotDeletedOnTime(new Token("red"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}

}
