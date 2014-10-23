package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Clause;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.PlacePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StateExpression;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Absent;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainLeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainPrecedes;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Exists;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsToChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Precedes;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PrecedesChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Universal;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.XLeadsTo;

public class TestAtomicPatterns {

	@Test
	public void testPrecedesTransitionOps() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		Precedes p = new Precedes(t1, t2);
		assertEquals("P>=1 [((G (t1_last=1)) | (!(t2_last=1) U (t1_last=1)))]", p.getPrismProperty(false));
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testPrecedesStatePredOps() {
		StateExpression sp1 = new PlacePredicate("p1", Relation.EQUALS, 1);
		StateExpression sp2 = new PlacePredicate("p2", Relation.GREATER, 3);
		Precedes p = new Precedes(sp1, sp2);
		assertEquals("P>=1 [((G (p1=1)) | (!(p2>3) U (p1=1)))]", p.getPrismProperty(false));
		Clause c = new Clause(sp1, sp2);
		StateExpression sp3 = new PlacePredicate("p3", Relation.SMALLER, 4);
		Precedes p1 = new Precedes(sp3, c);
		assertEquals("P>=1 [((G (p3<4)) | (!((p1=1) & (p2>3)) U (p3<4)))]", p1.getPrismProperty(false));
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testLeadsToTransitionOps() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		LeadsTo p = new LeadsTo(t1, t2);
		assertEquals("P>=1 [G((t1_last=1) => (F(t2_last=1)))]", p.getPrismProperty(false));
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testXLeadsToTransitionOps() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		XLeadsTo p = new XLeadsTo(t1, t2);
		assertEquals("P>=1 [G((t1_last=1) => (X(t2_last=1)))]", p.getPrismProperty(false));
		assertEquals(2, p.getOperatorCount());
	}
	
	@Test
	public void testChainPrecedesTransitionOps() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		Transition t3 = new Transition("t3");
		ChainPrecedes p = new ChainPrecedes(t1, t2, t3);
		assertEquals("P>=1 [F(t3_last=1) => ((!(t3_last=1)) U ((t1_last=1) & "
				+ "(!(t3_last=1)) & (X((!(t3_last=1)) U (t2_last=1)))))]", 
				p.getPrismProperty(false));
		assertEquals(3, p.getOperatorCount());
		PrecedesChain p2 = new PrecedesChain(t1, t2, t3);
		assertEquals("P>=1 [(F((t2_last=1) & (X(F(t3_last=1))))) => ((!(t2_last=1)) U (t1_last=1))]", 
				p2.getPrismProperty(false));
	}
	
	@Test
	public void testChainLeadsToTransitionOps() {
		Transition t1 = new Transition("t1");
		Transition t2 = new Transition("t2");
		Transition t3 = new Transition("t3");
		ChainLeadsTo p = new ChainLeadsTo(t1, t2, t3);
		assertEquals("P>=1 [G(((t1_last=1) & (X(F(t2_last=1)))) => (X(F((t2_last=1) & "
				+ "(F(t3_last=1))))))]", 
				p.getPrismProperty(false));
		assertEquals(3, p.getOperatorCount());
		LeadsToChain p2 = new LeadsToChain(t1, t2, t3);
		assertEquals("P>=1 [G((t1_last=1) => (F((t2_last=1) & (X(F(t3_last=1)))))))]", 
				p2.getPrismProperty(false));
	}
	
	@Test
	public void testUniversalTransitionOps() {
		Transition t1 = new Transition("t1");
		Universal p = new Universal(t1, new IFNetPlace("pOut"));
		assertEquals("P>=1 [G(t1_last=1)]", 
				p.getPrismProperty(false));
		assertEquals(1, p.getOperatorCount());
	}
	
	@Test
	public void testAbsentTransitionOps() {
		Transition t1 = new Transition("t1");
		Absent p = new Absent(t1);
		assertEquals("P>=1 [G(!(t1_last=1))]", 
				p.getPrismProperty(false));
		assertEquals(1, p.getOperatorCount());
	}
	
	@Test
	public void testExistsTransitionOps() {
		Transition t1 = new Transition("t1");
		Exists p = new Exists(t1, new IFNetPlace("pIn"));
		assertEquals("P>=1 [F(t1_last=1)]", 
				p.getPrismProperty(false));
		assertEquals(1, p.getOperatorCount());
	}

}
