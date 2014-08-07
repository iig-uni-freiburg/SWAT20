package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;

public class ExistsTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("pIn_black", Relation.GREATER_EQUAL, 1);
		AtomicProposition ap2 = new AtomicProposition("pOut_black", Relation.GREATER_EQUAL, 1);
		Clause c = new Clause(ap1, ap2);
		Exists p1 = new Exists(c);
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
		AtomicProposition ap1 = new AtomicProposition("pOut_black", Relation.GREATER_EQUAL, 1);
		Exists p1 = new Exists(ap1);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.createDeadTransitionNet();
		Collection<IFNetPlace> outplaces = ifnet.getDrainPlaces();
		Exists p1 = new Exists(new Transition("t1"), outplaces.iterator().next());
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}

}
