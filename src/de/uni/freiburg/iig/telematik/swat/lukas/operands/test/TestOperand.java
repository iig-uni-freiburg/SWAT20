package de.uni.freiburg.iig.telematik.swat.lukas.operands.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.Clause;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.PlacePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;

public class TestOperand {

	@Test
	public void testAtomicProposition() {
		StateExpression sp1 = new PlacePredicate("p1", Relation.EQUALS, 1);
		StateExpression sp2 = new PlacePredicate("p2", Relation.GREATER, 3);
		StateExpression sp3 = new PlacePredicate("p3", Relation.SMALLER, 3);
		StateExpression sp4 = new PlacePredicate("p3", Relation.SMALLER_EQUAL, 3);
		StateExpression sp5 = new PlacePredicate("p4", Relation.GREATER_EQUAL, 3);
		assertEquals("(p1=1)", sp1.toString());
		assertEquals("(p2>3)", sp2.toString());
		assertEquals("(p3<3)", sp3.toString());
		assertEquals("(p3<=3)", sp4.toString());
		assertEquals("(p4>=3)", sp5.toString());
	}
	
	@Test
	public void testClause() {
		StateExpression sp1 = new PlacePredicate("p1", Relation.EQUALS, 1);
		StateExpression sp2 = new PlacePredicate("p2", Relation.GREATER, 3);
		StateExpression sp3 = new PlacePredicate("p3", Relation.SMALLER, 3);
		StateExpression clause1 = new Clause(sp1, sp2);
		StateExpression clause2 = new Clause(clause1, sp3);
		
		assertEquals("(p1=1)", sp1.toString());
		assertEquals("(p2>3)", sp2.toString());
		assertEquals("(p3<3)", sp3.toString());
		assertEquals("((p1=1) & (p2>3))", clause1.toString());
		assertEquals("(((p1=1) & (p2>3)) & (p3<3))", clause2.toString());
	}
	
	
	
	@Test
	public void testTransition() {
		Transition sp1 = new Transition("t1");
		assertEquals("(t1_last=1)", sp1.toString());
	}

}
