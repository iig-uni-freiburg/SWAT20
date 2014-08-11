package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

public class MBoundedTest {
	
	@Test
	public void test0() {
		// the activity occurs in the process log and is executed by pete --> result should be true
		ArrayList<Transition> activities = new ArrayList<Transition>();
		activities.add(new Transition("register request"));
		MBounded p = new MBounded(activities, new User("Pete"));
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
		// the activity doesn't occur in the process log --> result should be true
		ArrayList<Transition> activities = new ArrayList<Transition>();
		activities.add(new Transition("blub"));
		MBounded p = new MBounded(activities, new User("Dieter"));
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
		// one of the activities doesn't exists others are performed by the correct user --> result should be true
		ArrayList<Transition> activities = new ArrayList<Transition>();
		activities.add(new Transition("register request"));
		activities.add(new Transition("blub"));
		MBounded p = new MBounded(activities, new User("Pete"));
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
		// not all activities are performed by the correct user --> result should be false
		ArrayList<Transition> activities = new ArrayList<Transition>();
		activities.add(new Transition("register request"));
		activities.add(new Transition("examine thoroughly"));
		MBounded p = new MBounded(activities, new User("Pete"));
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
		// not all activities are always performed by the correct user --> result should be false
		ArrayList<Transition> activities = new ArrayList<Transition>();
		activities.add(new Transition("register request"));
		activities.add(new Transition("examine thoroughly"));
		MBounded p = new MBounded(activities, new User("Pete"));
		String fileName = "twoOccsOfSameActivity.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}

}
