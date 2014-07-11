package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class UniversalTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		AtomicProposition ap1 = new AtomicProposition("pIn_black", Relation.SMALLER_EQUAL, 1);
		AtomicProposition ap2 = new AtomicProposition("pOut_black", Relation.SMALLER_EQUAL, 1);
		Clause c = new Clause(ap1, ap2);
		Universal p1 = new Universal(c);
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
		AtomicProposition ap1 = new AtomicProposition("pOut_black", Relation.GREATER_EQUAL, 1);
		Universal p1 = new Universal(ap1);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}


}
