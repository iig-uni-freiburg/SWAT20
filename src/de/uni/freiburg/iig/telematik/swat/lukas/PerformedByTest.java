package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

public class PerformedByTest extends RolePatternsTest {
	
	@Test
	public void test0() {
		// activity is performed by correct role --> result should be true
		PerformedBy p = new PerformedBy(new Transition("a1"), new Role("R1"));
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
		PerformedBy p = new PerformedBy(new Transition("a1"), new Role("R2"));
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test2() {
		// activity is performed by sibling-role --> result should be false
		PerformedBy p = new PerformedBy(new Transition("a3"), new Role("R2"));
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test3() {
		// activity once performed by a valid role and once by an illegal role --> result should be false
		PerformedBy p = new PerformedBy(new Transition("a4"), new Role("R4"));
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
}
