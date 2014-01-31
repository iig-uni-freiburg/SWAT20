package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillGradientDirectionAction;

public class ColorPanel {
	
	public static void main(String[] args) throws ParameterException {
		JPanel entry = new JPanel(new BorderLayout());
	JButton button = new JButton();
	FillGradientDirectionAction gradientDirectionAction = new FillGradientDirectionAction();
	JButton gradientDirectionButton = (JButton) add(gradientDirectionAction, false);
		entry.add(button, BorderLayout.CENTER);
		entry.setPreferredSize(new Dimension(100,100));
		new DisplayFrame(entry, true);
	}

}
