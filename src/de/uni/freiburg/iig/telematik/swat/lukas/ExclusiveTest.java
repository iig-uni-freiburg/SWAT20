package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;

public class ExclusiveTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		Exclusive p1 = new Exclusive(new Transition("tIn"), new Transition("tOut"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied(false));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		Exclusive p1 = new Exclusive(new Transition("tOut"), new Transition("t1"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied(false));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		Exclusive p1 = new Exclusive(new Transition("t1"), new Transition("tOut"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied(false));
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("p4_yellow", Relation.EQUALS, 1);
		AtomicProposition ap2 = new AtomicProposition("p1_red", Relation.EQUALS, 1);
		Exclusive p1 = new Exclusive(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("p1_red", Relation.EQUALS, 1);
		AtomicProposition ap2 = new AtomicProposition("p4_yellow", Relation.EQUALS, 1);
		Exclusive p1 = new Exclusive(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}

}
