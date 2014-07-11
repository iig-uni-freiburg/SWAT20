package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class InconsistentDataTest {
	
	@Test
	public void test0() {
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		WeaklyLostData p = new WeaklyLostData(new Token("red"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
