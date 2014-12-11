package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ReadUp;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.IFFlowPatternCreator;

public class ReadUpTest {
	
	@Test
	public void test0() {
		
		IFNet ifnet = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		IFFlowPatternCreator ifpc = new IFFlowPatternCreator(ifnet); 
		ArrayList<CompliancePattern> readUpPatterns = ifpc.createPatterns(ReadUp.NAME);
		boolean sat = false;
		for (CompliancePattern pattern : readUpPatterns) {
			TestUtils tu = new TestUtils(ifnet, pattern);
			try {
				sat = sat || tu.isPropertySatisfied();
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}
		assertTrue(sat);
		
	}
	
	@Test
	public void test1() {
		
		IFNet ifnet = IFNetTestUtils.createParallelExecIFNet();
		IFFlowPatternCreator ifpc = new IFFlowPatternCreator(ifnet); 
		ArrayList<CompliancePattern> readUpPatterns = ifpc.createPatterns(ReadUp.NAME);
		boolean sat = false;
		for (CompliancePattern pattern : readUpPatterns) {
			TestUtils tu = new TestUtils(ifnet, pattern);
			try {
				sat = sat || tu.isPropertySatisfied();
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}
		assertFalse(sat);
		
	}
	
}
