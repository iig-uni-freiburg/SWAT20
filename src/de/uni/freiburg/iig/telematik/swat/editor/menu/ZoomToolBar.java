package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontRotationAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontUnderlineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.zoom.ZoomInAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.zoom.ZoomOutAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;

public class ZoomToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private boolean ignoreZoomChange = false;

	// Actions

	private ZoomInAction zoomInAction;
	
	// Buttons
	private JButton zoomInButton;

	// Tooltips
	private String zoomInTooltip = "zoom in";

	private String zoomOutTooltip = "zoom out";
	


	// further variables

	private ZoomOutAction zoomOutAction;

	private JButton zoomOutButton;


	public ZoomToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
//		setLayout(new WrapLayout(FlowLayout.LEFT));

		try {

			zoomInAction = new ZoomInAction(pnEditor);	
			zoomOutAction = new ZoomOutAction(pnEditor);
			
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setFloatable(false);

		zoomInButton = add(zoomInAction);
		setButtonSettings(zoomInButton);	
		zoomOutButton = add(zoomOutAction);
		setButtonSettings(zoomOutButton);
	
		zoomInButton.setToolTipText(zoomInTooltip);
		zoomOutButton.setToolTipText(zoomOutTooltip);
		
		
	}


	


	private void setButtonSettings(final JButton button) {
		button.setBorderPainted(false);
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				button.setBorderPainted(false);
				super.mouseReleased(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				button.setBorderPainted(true);
				super.mousePressed(e);
			}

		});
	}

}
