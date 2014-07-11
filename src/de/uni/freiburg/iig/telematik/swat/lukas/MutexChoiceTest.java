package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class MutexChoiceTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		MutexChoice p1 = new MutexChoice(new Transition("td"), new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		MutexChoice p1 = new MutexChoice(new Transition("t1"), new Transition("td"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		MutexChoice p1 = new MutexChoice(new Transition("t0"), new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		MutexChoice p1 = new MutexChoice(new Transition("t1"), new Transition("t0"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("pIn_black", Relation.EQUALS, 1);
		AtomicProposition ap2 = new AtomicProposition("pOut_black", Relation.EQUALS, 1);
		MutexChoice p1 = new MutexChoice(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test7() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("p4_yellow", Relation.EQUALS, 1);
		AtomicProposition ap2 = new AtomicProposition("p1_red", Relation.EQUALS, 1);
		MutexChoice p1 = new MutexChoice(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test8() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("p4_yellow", Relation.EQUALS, 1);
		AtomicProposition ap2 = new AtomicProposition("p2_green", Relation.EQUALS, 1);
		MutexChoice p1 = new MutexChoice(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test9() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("p4_yellow", Relation.EQUALS, 3);
		AtomicProposition ap2 = new AtomicProposition("p2_green", Relation.EQUALS, 2);
		MutexChoice p1 = new MutexChoice(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
