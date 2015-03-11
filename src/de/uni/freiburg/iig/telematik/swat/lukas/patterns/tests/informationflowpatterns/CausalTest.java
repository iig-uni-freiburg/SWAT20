package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.informationflowpatterns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.Causal;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.IFFlowPatternCreator;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class CausalTest {
	
	@Test
	public void test0() {
		
		IFNet ifnet = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		TransitionToIDMapper.createMap(ifnet);
		IFFlowPatternCreator ifpc = new IFFlowPatternCreator(ifnet); 
		ArrayList<CompliancePattern> patterns = ifpc.createPatterns(Causal.NAME);
		boolean sat = false;
		for (CompliancePattern pattern : patterns) {
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
		TransitionToIDMapper.createMap(ifnet);
		IFFlowPatternCreator ifpc = new IFFlowPatternCreator(ifnet); 
		ArrayList<CompliancePattern> patterns = ifpc.createPatterns(Causal.NAME);
		boolean sat = false;
		for (CompliancePattern pattern : patterns) {
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
