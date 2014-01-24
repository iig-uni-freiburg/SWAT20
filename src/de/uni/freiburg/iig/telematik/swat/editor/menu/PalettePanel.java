package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.io.File;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxRectangle;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class PalettePanel extends JPanel {

	private static final long serialVersionUID = -1156941541375286369L;
	
	private final int PREFERRED_ICON_SIZE = 40;
	private final Dimension PREFERRED_PALETTE_COMPONENT_SIZE = new Dimension(100,80);
	
	private JSVGCanvas svgCanvasPlace = new JSVGCanvas();
	private JSVGCanvas svgCanvasTransition = new JSVGCanvas();

	protected JPanel selectedEntry = null;

	public PalettePanel() throws ParameterException {
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		add(Box.createVerticalStrut(20));
		addPlaceTemplate();
		add(Box.createVerticalStrut(20));
		addTransitionTemplate();
		add(Box.createVerticalGlue());
	}

	public void addPlaceTemplate() throws ParameterException {
		int size = EditorProperties.getInstance().getDefaultPlaceSize();
		String style = MXConstants.getNodeStyle(PNComponent.PLACE, null, null);
		PNGraphCell cell = new PNGraphCell(null, new mxGeometry(0, 0, size, size), style, PNComponent.PLACE);
		cell.setVertex(true);
		URL svgURL = PNEditor.class.getResource("/images/place.svg");
		addTemplate("Place", svgCanvasPlace, svgURL, cell);
	}

	public void addTransitionTemplate() throws ParameterException {
		int width = EditorProperties.getInstance().getDefaultTransitionWidth();
		int height = EditorProperties.getInstance().getDefaultTransitionHeight();
		String style = MXConstants.getNodeStyle(PNComponent.TRANSITION, null, null);
		PNGraphCell cell = new PNGraphCell(null, new mxGeometry(0, 0, width, height), style, PNComponent.TRANSITION);
		cell.setVertex(true);
		URL svgURL = PNEditor.class.getResource("/images/transition.svg");
		addTemplate("Transition", svgCanvasTransition, svgURL, cell);
	}

	public void addTemplate(final String name, JSVGCanvas svgCanvas, URL svgURL, mxCell cell) {
		mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
		final mxGraphTransferable t = new mxGraphTransferable(new Object[] { cell }, bounds);
		
		JPanel paletteComponent = new JPanel(new BorderLayout());
		paletteComponent.setPreferredSize(PREFERRED_PALETTE_COMPONENT_SIZE);
		paletteComponent.setMaximumSize(PREFERRED_PALETTE_COMPONENT_SIZE);
		paletteComponent.setMinimumSize(PREFERRED_PALETTE_COMPONENT_SIZE);
		
		JPanel paletteIconPanelOuter = new JPanel();
		BoxLayout layout = new BoxLayout(paletteIconPanelOuter, BoxLayout.PAGE_AXIS);
		paletteIconPanelOuter.setLayout(layout);
		paletteIconPanelOuter.add(Box.createVerticalGlue());
		
		JPanel paletteIconPanelInner = new JPanel();
		BoxLayout layout2 = new BoxLayout(paletteIconPanelInner, BoxLayout.LINE_AXIS);
		paletteIconPanelInner.setLayout(layout2);
		paletteIconPanelInner.add(Box.createHorizontalGlue());
		final JPanel iconPanel = new JPanel(new BorderLayout());
		iconPanel.setPreferredSize(new Dimension(PREFERRED_ICON_SIZE,PREFERRED_ICON_SIZE));
		iconPanel.setMaximumSize(new Dimension(PREFERRED_ICON_SIZE,PREFERRED_ICON_SIZE));
		iconPanel.setMinimumSize(new Dimension(PREFERRED_ICON_SIZE,PREFERRED_ICON_SIZE));
		svgCanvas.setBackground(iconPanel.getBackground());
		svgCanvas.setURI(svgURL.toString());
		iconPanel.add(svgCanvas, BorderLayout.CENTER);
		paletteIconPanelInner.add(iconPanel);
		paletteIconPanelInner.add(Box.createHorizontalGlue());
		paletteIconPanelOuter.add(paletteIconPanelInner);
		
		paletteIconPanelOuter.add(Box.createVerticalGlue());
		
		paletteComponent.add(paletteIconPanelOuter, BorderLayout.CENTER);
		paletteComponent.add(new JLabel(name, JLabel.CENTER), BorderLayout.PAGE_END);
		
		
		
//		final JLabel entry = new JLabel(icon);
//		entry.setPreferredSize(new Dimension(50, 50));
//		entry.setBackground(PalettePanel.this.getBackground().brighter());
//		entry.setFont(new Font(entry.getFont().getFamily(), 0, 10));
//
//		entry.setVerticalTextPosition(JLabel.BOTTOM);
//		entry.setHorizontalTextPosition(JLabel.CENTER);
//		entry.setIconTextGap(0);
//
//		entry.setToolTipText(name);
//		entry.setText(name);
//		iconPanel.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mousePressed(MouseEvent e) {
//				setSelectionEntry(iconPanel, t);
//			}
//		});

		DragGestureListener dragGestureListener = new DragGestureListener() {
			public void dragGestureRecognized(DragGestureEvent e) {
				if (!iconPanel.isEnabled()) {
					System.out.println("gerd");
					return;
				}
				System.out.println("gerda");
				e.startDrag(null, MXConstants.EMPTY_IMAGE, new Point(), t, null);
			}
		};

		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(iconPanel, DnDConstants.ACTION_COPY, dragGestureListener);
		
		add(paletteComponent);
	}

	public static void main(String[] args) throws ParameterException {
		JPanel entry = new JPanel(new BorderLayout());
		JSVGCanvas svgCanvas = new JSVGCanvas();

		entry.add(svgCanvas, BorderLayout.CENTER);
		entry.setPreferredSize(new Dimension(100,100));
		svgCanvas.setURI(new File("/Users/stocker/Desktop/place.svg").toURI().toString());
		new DisplayFrame(entry, true);
	}

}
