package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCompositePatterns {

	@Test
	public void testCoAbstentTransitionOp() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		CoAbsent p = new CoAbsent(t1, t2);
		assertEquals("P>=1 [!G(!(t1=1)) | G(!(t2=1))]", p.getPrismRep());
		assertEquals("!G(!(t1=1)) | G(!(t2=1))", p.toString());
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testCoExistsTransitionOp() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		CoExists p = new CoExists(t1, t2);
		assertEquals("P>=1 [(!F(t1=1)) | (F(t2=1))]", p.getPrismRep());
		assertEquals("(!F(t1=1)) | (F(t2=1))", p.toString());
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testExclusiveTransitionOp() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		Exclusive p = new Exclusive(t1, t2);
		assertEquals("P>=1 [(!(F(t1=1)) | (F(!(t2=1)))) & "
				+ "(!(F(t2=1)) | (F(!(t1=1))))]", p.getPrismRep());
		assertEquals("(!(F(t1=1)) | (F(!(t2=1)))) & "
				+ "(!(F(t2=1)) | (F(!(t1=1))))", p.toString());
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testCoRequisiteTransitionOp() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		Corequisite p = new Corequisite(t1, t2);
		assertEquals("P>=1 [(F(t1=1) & F(t2=1)) | (F(!(t1=1)) & F(!(t2=1)))]", p.getPrismRep());
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testMutexChoiceTransitionOp() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		MutexChoice p = new MutexChoice(t1, t2);
		assertEquals("P>=1 [(F(t1=1) & G(!(t2=1))) | (F(t2=1) & G(!(t1=1)))]", p.getPrismRep());
		assertEquals(2, p.getOperatorCount());
	}

}