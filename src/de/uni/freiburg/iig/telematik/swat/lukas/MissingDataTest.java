package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class MissingDataTest {

	@Test
	public void test0() {
		IFNet net = IFNetTestUtils.create6PlaceIFnetWithAccessModes();
		MissingData p = new MissingData(new Token("red"), net.getRegularTransitions());
		TestUtils tu = new TestUtils(net, p);
		
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}