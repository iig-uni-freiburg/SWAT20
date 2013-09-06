package de.unifreiburg.iig.bpworkbench2.controller;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import de.unifreiburg.iig.bpworkbench2.helper.JVMUsage;
import de.unifreiburg.iig.bpworkbench2.model.GuiModel;

//onlx for testing
public class History implements Observer {
	History myHistory = new History();
	private CircularFifoBuffer buffer;
	private CircularFifoBuffer future;
	private long memoryUsageAtBeginning;

	@Override
	public void update(Observable arg0, Object arg1) {
		// If the model changed, add the current state of the model to the
		// buffer
		buffer.add(arg0);
	}

	/**
	 * Creates a history with 10 Elements.
	 */
	private History() {
		this(10);
	}

	/**
	 * Creates a history with <b>size</b> number of elements
	 * 
	 * @param size
	 *            sets the history size
	 */
	private History(int size) {
		// Get memory usage before to roughly approximate memory consumption of
		// history
		memoryUsageAtBeginning = JVMUsage.getByteUsed();
		// create ring buffer for undo and redo
		buffer = new CircularFifoBuffer(size);
		future = new CircularFifoBuffer(size);
	}

	public History getInstance() {
		// Singleton Pattern
		return myHistory;
	}

	/**
	 * TODO: Seems broken!
	 * 
	 * @return
	 */
	public GuiModel getLastState() {
		// first element contains current state. However, we want the last state
		// the current state should be the first redo state
		future.add(buffer.remove());
		// get last state
		GuiModel result = (GuiModel) buffer.get();
		// remove last state from stack. No longer nedded
		buffer.remove();
		return result;
	}

}
