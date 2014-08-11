package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class USegregatedFromTest {
	
	@Test
	public void test0() {
		// second activity doesn't exist in process log --> result should be true
		USegregatedFrom p = new USegregatedFrom(new Transition("register request"), new Transition("abort"));
		p.printOut();
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		// first activity doesn't exist in process log --> result should be true
		USegregatedFrom p = new USegregatedFrom(new Transition("abort"), new Transition("register request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test2() {
		// both activities exist and are executed by the same user --> result should be false
		USegregatedFrom p = new USegregatedFrom(new Transition("register request"), new Transition("reject request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		// first and second activity are the same --> result should be false
		USegregatedFrom p = new USegregatedFrom(new Transition("decide"), new Transition("decide"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		// both activities exist and are executed by different users --> result should be true
		USegregatedFrom p = new USegregatedFrom(new Transition("examine thoroughly"),
				new Transition("register request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test5() {
		// the two activities are not always executed by different users --> result should be false
		USegregatedFrom p = new USegregatedFrom(new Transition("register request"), 
				new Transition("examine thoroughly"));
		String fileName = "twoOccsOfSameActivity.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}

}
