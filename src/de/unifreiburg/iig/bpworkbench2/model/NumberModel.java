package de.unifreiburg.iig.bpworkbench2.model;

import java.util.Observable;

//Only for testing
public class NumberModel extends Observable {
	private static NumberModel myNumberModel = null;
	private int money = 100;

	public int getAmount() {
		return money;
	}

	public void setAmount(int newAmount) {
		money = newAmount;
		setChanged();
		notifyObservers("New Transaction");
		System.out.println("Set Amount to " + newAmount);
	}

	public static NumberModel getInstance() {
		if (myNumberModel == null) {
			myNumberModel = new NumberModel();
		}
		return myNumberModel;
	}
}
