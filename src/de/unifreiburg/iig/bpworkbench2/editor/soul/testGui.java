package de.unifreiburg.iig.bpworkbench2.editor.soul;

import javax.swing.*;

import de.unifreiburg.iig.bpworkbench2.editor.gui.PNMLEditor;

import java.awt.Color;
import java.io.File;


public class testGui {

	public static void main(String[] args) {
		// Create new JFrame
		JFrame frame = new JFrame();
		PNMLEditor panel;
		frame.setTitle("PNML Editor");
		frame.setSize(450, 300);

		/*
		 * LOAD PNML-EDITOR parsers not finished, that's why we create an empty
		 * editor with "null", as soon as the finished parsers are implemented,
		 * "null" is replaced by the File-Object of the pnml-file
		 */
//		panel = new PNMLEditor(null);
		panel = new PNMLEditor(new File("/Users/julius/Desktop/Samples/sampleCPnet.pnml"));

		// set background black
		panel.setBackground(Color.black);

		// add Editor to JFrame
		frame.add(panel);

		// show all
		frame.setVisible(true);

	}
}