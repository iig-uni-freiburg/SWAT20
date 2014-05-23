package de.uni.freiburg.iig.telematik.swat.editor.menu;

/**
 * methods/CirclePanel.java - Component to draw circles.
 * This class functions as a GUI component, and can be added to a layout.
 * @version 22 June 1998, revised July 1999,  2002-02-07 JPanel
 * @author Fred Swartz
 */

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.FillColorSelectionAction;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class CirclePanel extends JPanel {

   private Color color;
   int size = 10;

//=========================================== constructor
   public CirclePanel(Color tokenColor) {
	   color = tokenColor;
	  
	try {
		size = SwatProperties.getInstance().getIconSize().getSize()/3;
	} catch (PropertyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
	setPreferredSize(new Dimension(size, size));
//       setBackground(Color.white);
   }//end constructor

   //=========================================== paintComponent
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2 = (Graphics2D) g;
       g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
       g2.setColor(color);
       g2.fillOval(size/2+size/4,size, size, size);

   }//end paintComponent

   //========================================== drawCircle
   // Convenience method to draw from center with radius
   public void drawCircle(Graphics cg, int xCenter, int yCenter, int r) {
       cg.drawOval(xCenter-r, yCenter-r, 2*r, 2*r);
   }//end drawCircle
} // end class CirclePanel