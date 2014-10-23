package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.SegregatedFrom;

public class SegregatedFromTest {
	
	@Test
	public void test0() {
		// the activities are performed by the same user and thus the same role --> result should be false
		SegregatedFrom p = new SegregatedFrom(new Transition("register request"), new Transition("reject request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test1() {
		// result should be false
		SegregatedFrom p = new SegregatedFrom(new Transition("register request"), new Transition("register request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test2() {
		// the second activity doesn't occur in the log --> result should be true
		SegregatedFrom p = new SegregatedFrom(new Transition("register request"), new Transition("abort"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test3() {
		// both activities occur in the log and different user with different role perform them --> result should be true
		SegregatedFrom p = new SegregatedFrom(new Transition("register request"), new Transition("examine thoroughly"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test4() {
		// both activies occur twice in the process log, once performed by user A and once performed by user B --> result should be true
		SegregatedFrom p = new SegregatedFrom(new Transition("register request"), new Transition("examine thoroughly"));
		String fileName = "twoOccsOfSameActivity.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}