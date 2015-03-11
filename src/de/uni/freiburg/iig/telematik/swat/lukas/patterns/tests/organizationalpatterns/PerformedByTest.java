package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.Role;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PerformedBy;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class PerformedByTest {
	
	@Test
	public void test0() {
		// activity is performed by correct role --> result should be true
		PerformedBy p = new PerformedBy(new Activity("reject request"), new Role("R1"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test1() {
		// activity is performed by super-role --> result should be true
		PerformedBy p = new PerformedBy(new Activity("a2"), new Role("R1"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
		
}
