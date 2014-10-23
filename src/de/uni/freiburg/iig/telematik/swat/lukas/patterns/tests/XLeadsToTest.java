package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Clause;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.PlacePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.XLeadsTo;

public class XLeadsToTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		XLeadsTo p1 = new XLeadsTo(new Transition("tIn"), new Transition("tOut"));
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
		XLeadsTo p1 = new XLeadsTo(new Transition("t1"), new Transition("td"));
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
		XLeadsTo p1 = new XLeadsTo(new Transition("td"), new Transition("t1"));
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
		PlacePredicate ap1 = new PlacePredicate("pIn_black", Relation.EQUALS, 1);
		PlacePredicate ap2 = new PlacePredicate("pOut_black", Relation.EQUALS, 1);
		XLeadsTo p1 = new XLeadsTo(ap1, ap2);
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
		PlacePredicate ap1 = new PlacePredicate("p0_green", Relation.EQUALS, 1);
		PlacePredicate ap2 = new PlacePredicate("p2_green", Relation.EQUALS, 1);
		XLeadsTo p1 = new XLeadsTo(ap1, ap2);
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
		PlacePredicate ap1 = new PlacePredicate("p2_black", Relation.EQUALS, 1);
		PlacePredicate ap2 = new PlacePredicate("p2_green", Relation.EQUALS, 1);
		PlacePredicate ap3 = new PlacePredicate("p3_black", Relation.EQUALS, 1);
		PlacePredicate ap4 = new PlacePredicate("p3_blue", Relation.EQUALS, 1);
		PlacePredicate ap5 = new PlacePredicate("pOut_black", Relation.EQUALS, 1);
		StateExpression sp1 = new Clause(ap1, ap2);
		StateExpression sp2 = new Clause(ap3, ap4);
		StateExpression sp3 = new Clause(sp1, sp2);
		XLeadsTo p1 = new XLeadsTo(sp3, ap5);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
