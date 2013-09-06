package de.unifreiburg.iig.bpworkbench2.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;

import de.unifreiburg.iig.bpworkbench2.model.NumberModel;

//only for testing
public class Increaser implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		// TODO: Increase the NumberModel. Dafür muss allerdings ein Singleton
		// um das NumberModel gebaut werden
		NumberModel.getInstance().setAmount(12345);
		System.out.println("Controller Increaser called");
	}

	public void stateChanged(ChangeEvent e) {
		// TODO: Increase the NumberModel. Dafür muss allerdings ein Singleton
		// um das NumberModel gebaut werden
		NumberModel.getInstance().setAmount(12345);
		System.out.println("Controller Increaser called");

	}

}
