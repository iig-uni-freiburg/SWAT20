package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;

public class CorequisiteTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		Corequisite p1 = new Corequisite(new Transition("td"), new Transition("t1"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		Corequisite p1 = new Corequisite(new Transition("t1"), new Transition("td"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		Corequisite p1 = new Corequisite(new Transition("t0"), new Transition("t1"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		Corequisite p1 = new Corequisite(new Transition("t1"), new Transition("t0"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("pIn_black", Relation.EQUALS, 1);
		AtomicProposition ap2 = new AtomicProposition("pOut_black", Relation.EQUALS, 1);
		Corequisite p1 = new Corequisite(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test6() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("p0_green", Relation.EQUALS, 1);
		AtomicProposition ap2 = new AtomicProposition("p2_green", Relation.EQUALS, 1);
		Corequisite p1 = new Corequisite(ap1, ap2);
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
		Corequisite p1 = new Corequisite(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
