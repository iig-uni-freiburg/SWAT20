package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.dataflowantipatterns;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Token;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.WeaklyLostData;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class WeaklyLostDataTest {
	
	
	@Test
	public void test0() {
		
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		TransitionToIDMapper.createMap(net);
		WeaklyLostData p = new WeaklyLostData(new Token("green"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test1() {
		
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		TransitionToIDMapper.createMap(net);
		WeaklyLostData p = new WeaklyLostData(new Token("blue"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test2() {
		
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		TransitionToIDMapper.createMap(net);
		WeaklyLostData p = new WeaklyLostData(new Token("red"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test3() {
		
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		TransitionToIDMapper.createMap(net);
		WeaklyLostData p = new WeaklyLostData(new Token("red"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test4() {
		
		IFNet net = IFNetTestUtils.createParallelExecIFNet();
		TransitionToIDMapper.createMap(net);
		WeaklyLostData p = new WeaklyLostData(new Token("green"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
