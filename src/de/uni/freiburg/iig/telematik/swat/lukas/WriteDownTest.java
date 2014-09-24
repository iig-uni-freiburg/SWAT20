package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class WriteDownTest {
	
	@Test
	public void test0() {
		
		IFNet ifnet = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		IFFlowPatternCreator ifpc = new IFFlowPatternCreator(ifnet); 
		ArrayList<CompliancePattern> writeDownPatterns = ifpc.createPatterns(WriteDown.NAME);
		boolean sat = false;
		for (CompliancePattern pattern : writeDownPatterns) {
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
		ArrayList<CompliancePattern> writeDownPatterns = ifpc.createPatterns(WriteDown.NAME);
		boolean sat = false;
		for (CompliancePattern pattern : writeDownPatterns) {
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
