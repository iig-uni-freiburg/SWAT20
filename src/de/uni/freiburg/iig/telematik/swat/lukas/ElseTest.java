package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class ElseTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Transition tIn = new Transition("tIn");
		Transition tOut = new Transition("tOut");
		ArrayList<NetElementExpression> lis = new ArrayList<NetElementExpression>(Arrays.asList(tIn, tOut));
		Else p = new Else(lis);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.createUsageConflictIFNet();
		Transition tIn = new Transition("t1");
		Transition tOut = new Transition("t3");
		ArrayList<NetElementExpression> lis = new ArrayList<NetElementExpression>(Arrays.asList(tIn, tOut));
		Else p = new Else(lis);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Transition tIn = new Transition("tIn");
		Transition td = new Transition("td");
		Transition t0 = new Transition("t0");
		ArrayList<NetElementExpression> lis = new ArrayList<NetElementExpression>(Arrays.asList(tIn, td, t0));
		Else p = new Else(lis);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.createIFNetWithGuards();
		Transition tIn = new Transition("tIn");
		Transition t1 = new Transition("t1");
		Transition t3 = new Transition("t3");
		Transition t2 = new Transition("t2");
		ArrayList<NetElementExpression> lis = new ArrayList<NetElementExpression>(Arrays.asList(tIn, t1, t3, t2));
		Else p = new Else(lis);
		
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		IFNet ifnet = IFNetTestUtils.createIFNetWithGuards();
		Transition tIn = new Transition("tIn");
		Transition t1 = new Transition("t1");
		Transition t3 = new Transition("t3");
		ArrayList<NetElementExpression> lis = new ArrayList<NetElementExpression>(Arrays.asList(tIn, t1, t3));
		Else p = new Else(lis);
		
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
