package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class AbsentTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.createDeadTransitionNet();
		Absent p1 = new Absent(new Transition("t2"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.createDeadTransitionNet();
		Absent p1 = new Absent(new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.createDeadTransitionNet();
		AtomicProposition ap1 = new AtomicProposition("pIn_black", Relation.GREATER_EQUAL, 1);
		AtomicProposition ap2 = new AtomicProposition("pOut_black", Relation.GREATER_EQUAL, 1);
		Clause c = new Clause(ap1, ap2);
		Absent p1 = new Absent(c);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.createDeadTransitionNet();
		AtomicProposition ap1 = new AtomicProposition("pOut_black", Relation.GREATER_EQUAL, 1);
		Absent p1 = new Absent(ap1);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}

}