package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.BoundedWith;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class BoundedWithTest {
	
	@Test
	public void test0() {
		// second activity doesn't exist in process log --> result should be true
		BoundedWith p = new BoundedWith(new Activity("register request"), new Activity("abort"));
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
		BoundedWith p = new BoundedWith(new Activity("abort"), new Activity("register request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test3() {
		// both activities exist and are executed by the same user --> result should be true
		BoundedWith p = new BoundedWith(new Activity("register request"), new Activity("reject request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		BoundedWith p = new BoundedWith(new Activity("decide"), new Activity("decide"));
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
		BoundedWith p = new BoundedWith(new Activity("examine thoroughly"),
				new Activity("register request"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test6() {
		BoundedWith p = new BoundedWith(new Activity("register request"), 
				new Activity("examine thoroughly"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test7() {
		BoundedWith p = new BoundedWith(new Activity("register request"), 
				new Activity("examine thoroughly"));
		String fileName = "twoOccsOfSameActivity.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
}
