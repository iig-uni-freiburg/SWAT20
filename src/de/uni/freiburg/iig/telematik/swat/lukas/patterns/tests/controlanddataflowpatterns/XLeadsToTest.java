package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.ConjunctiveClause;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StatePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.XLeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class XLeadsToTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		XLeadsTo p1 = new XLeadsTo(new Activity("tIn"), new Activity("tOut"));
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
		TransitionToIDMapper.createMap(ifnet);
		XLeadsTo p1 = new XLeadsTo(new Activity("t1"), new Activity("td"));
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
		TransitionToIDMapper.createMap(ifnet);
		XLeadsTo p1 = new XLeadsTo(new Activity("td"), new Activity("t1"));
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
		TransitionToIDMapper.createMap(ifnet);
		StatePredicate ap1 = new StatePredicate("pIn_black", Relation.EQUALS, 1);
		StatePredicate ap2 = new StatePredicate("pOut_black", Relation.EQUALS, 1);
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
		TransitionToIDMapper.createMap(ifnet);
		StatePredicate ap1 = new StatePredicate("p0_green", Relation.EQUALS, 1);
		StatePredicate ap2 = new StatePredicate("p2_green", Relation.EQUALS, 1);
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
		TransitionToIDMapper.createMap(ifnet);
		StatePredicate ap1 = new StatePredicate("p2_black", Relation.EQUALS, 1);
		StatePredicate ap2 = new StatePredicate("p2_green", Relation.EQUALS, 1);
		StatePredicate ap3 = new StatePredicate("p3_black", Relation.EQUALS, 1);
		StatePredicate ap4 = new StatePredicate("p3_blue", Relation.EQUALS, 1);
		StatePredicate ap5 = new StatePredicate("pOut_black", Relation.EQUALS, 1);
		StateExpression sp1 = new ConjunctiveClause(ap1, ap2);
		StateExpression sp2 = new ConjunctiveClause(ap3, ap4);
		StateExpression sp3 = new ConjunctiveClause(sp1, sp2);
		XLeadsTo p1 = new XLeadsTo(sp3, ap5);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
