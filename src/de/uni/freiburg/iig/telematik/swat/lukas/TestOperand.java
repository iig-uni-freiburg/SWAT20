package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestOperand {

	@Test
	public void testAtomicProposition() {
		Statepredicate sp1 = new AtomicProposition("p1", 1, Relation.EQUALS);
		Statepredicate sp2 = new AtomicProposition("p2", 3, Relation.GREATER);
		Statepredicate sp3 = new AtomicProposition("p3", 3, Relation.SMALLER);
		Statepredicate sp4 = new AtomicProposition("p3", 3, Relation.SMALLER_EQUAL);
		Statepredicate sp5 = new AtomicProposition("p4", 3, Relation.GREATER_EQUAL);
		assertEquals("(p1=1)", sp1.toString());
		assertEquals("(p2>3)", sp2.toString());
		assertEquals("(p3<3)", sp3.toString());
		assertEquals("(p3<=3)", sp4.toString());
		assertEquals("(p4>=3)", sp5.toString());
	}
	
	@Test
	public void testTransition() {
		Transition sp1 = new Transition("t1");
		assertEquals("(t1=1)", sp1.toString());
	}

}
