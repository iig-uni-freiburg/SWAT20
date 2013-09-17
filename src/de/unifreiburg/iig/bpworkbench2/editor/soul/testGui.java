package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.awt.Color;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

import de.unifreiburg.iig.bpworkbench2.editor.gui.PNMLEditor;

public class testGui {

	public static void main(String[] args) {
		// Create new JFrame
		JFrame frame = new JFrame();
		PNMLEditor panel;
		frame.setTitle("PNML Editor");
		frame.setSize(450, 300);

		/*
		 * LOAD PNML-EDITOR parser partially finished, you may create an empty
		 * editor with "null", or an Editor to Load a PT-Net oder CPN by
		 * inserting its File-Object
		 */
		// panel = new PNMLEditor(null);
		String filePath = PNMLEditor.class.getResource(
				"/samples/sampleCPnet.pnml").getPath();
		System.out.println(filePath);
		
		panel = new PNMLEditor(new File(filePath));

		// set background black
		panel.setBackground(Color.black);

		// add Editor to JFrame
		frame.add(panel);

		// show all
		frame.setVisible(true);

	}
}