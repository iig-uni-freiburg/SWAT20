package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestOperand {

	@Test
	public void testAtomicProposition() {
		Statepredicate sp1 = new AtomicProposition("p1", Relation.EQUALS, 1);
		Statepredicate sp2 = new AtomicProposition("p2", Relation.GREATER, 3);
		Statepredicate sp3 = new AtomicProposition("p3", Relation.SMALLER, 3);
		Statepredicate sp4 = new AtomicProposition("p3", Relation.SMALLER_EQUAL, 3);
		Statepredicate sp5 = new AtomicProposition("p4", Relation.GREATER_EQUAL, 3);
		assertEquals("(p1=1)", sp1.toString());
		assertEquals("(p2>3)", sp2.toString());
		assertEquals("(p3<3)", sp3.toString());
		assertEquals("(p3<=3)", sp4.toString());
		assertEquals("(p4>=3)", sp5.toString());
	}
	
	@Test
	public void testClause() {
		Statepredicate sp1 = new AtomicProposition("p1", Relation.EQUALS, 1);
		Statepredicate sp2 = new AtomicProposition("p2", Relation.GREATER, 3);
		Statepredicate sp3 = new AtomicProposition("p3", Relation.SMALLER, 3);
		Statepredicate clause1 = new Clause(sp1, sp2);
		Statepredicate clause2 = new Clause(clause1, sp3);
		
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
