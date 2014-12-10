package de.uni.freiburg.iig.telematik.swat.editor.actions.nodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mxgraph.util.mxRectangle;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorProperties;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBarDialog;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.FontToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.PopupToolBar;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class NodeToolBarAction extends PopUpToolBarAction {

	private static final long serialVersionUID = -381740152242776391L;
	private static String nodeColor = "#333333";

	public NodeToolBarAction(PNEditor editor,String name, JToolBar toolbar ) throws ParameterException, PropertyException, IOException {
		super(editor, name, "plus", toolbar);
		getIcon().setImage(createIconImage());
	}

	public static Image createIconImage() throws PropertyException, IOException {
		Color defaultFillColor = Utils.parseColor(nodeColor );
		IconSize iconsize = null;
			iconsize = SwatProperties.getInstance().getIconSize();
		int size = iconsize.getSize();

		Image image = new BufferedImage (size, size, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      
		mxRectangle bounds = new mxRectangle(0, 0, size, size);
		float x1 = (float) bounds .getX();
		float y1 = (float) bounds.getY();
		float x2 = (float) bounds.getX();
		float y2 = (float) bounds.getY();


		GradientPaint fillPaint = new GradientPaint(x1, y1, defaultFillColor, x2, y2, defaultFillColor, false);
 		g2.setPaint(fillPaint);
 		int transitionSize = (size-1)/2;
        g2.fillRect(transitionSize-(transitionSize/2), transitionSize-(transitionSize/2), 	transitionSize+(transitionSize/2), transitionSize+(transitionSize/2));
        g2.setColor (new Color(0,0,0));
		g2.setStroke(new BasicStroke(1));
        g2.drawRect(transitionSize-(transitionSize/2), transitionSize-(transitionSize/2), 	transitionSize+(transitionSize/2), transitionSize+(transitionSize/2));
        
   
        int placeSize = (size-1)/2;
        Color bgcolor = UIManager.getColor ( "Panel.background" );
        g2.setColor(bgcolor);
        g2.fillOval(0, 0, 	placeSize+placeSize/2+2, placeSize+placeSize/2+2);
        g2.setColor(defaultFillColor);
        g2.fillOval(0, 0, 	placeSize+placeSize/2, placeSize+placeSize/2);
        g2.setColor (new Color(0,0,0));
		g2.setStroke(new BasicStroke(1));
        g2.drawOval(0, 0, 	placeSize+placeSize/2, placeSize + placeSize/2);

        g2.dispose ();
        return image;

	}
	

}
