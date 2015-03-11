package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.organizationalpatterns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.User;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.MBounded;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class MBoundedTest {
	
	@Test
	public void test0() {
		// the activity occurs in the process log and is executed by pete --> result should be true
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(new Activity("register request"));
		MBounded p = new MBounded(activities, new User("Pete"));
		p.printOut();
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
		// the activity doesn't occur in the process log --> result should be true
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(new Activity("blub"));
		MBounded p = new MBounded(activities, new User("Dieter"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void test2() {
		// one of the activities doesn't exists others are performed by the correct user --> result should be true
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(new Activity("register request"));
		activities.add(new Activity("blub"));
		MBounded p = new MBounded(activities, new User("Pete"));
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
		// not all activities are performed by the correct user --> result should be false
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(new Activity("register request"));
		activities.add(new Activity("examine thoroughly"));
		MBounded p = new MBounded(activities, new User("Pete"));
		String fileName = "simpleProcessInstance.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test4() {
		// not all activities are always performed by the correct user --> result should be false
		ArrayList<Activity> activities = new ArrayList<Activity>();
		activities.add(new Activity("register request"));
		activities.add(new Activity("examine thoroughly"));
		MBounded p = new MBounded(activities, new User("Pete"));
		//p.printOut();
		String fileName = "twoOccsOfSameActivity.mxml";
		TestUtils tu = new TestUtils(fileName, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
