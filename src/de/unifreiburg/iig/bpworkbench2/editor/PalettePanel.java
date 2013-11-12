package de.unifreiburg.iig.bpworkbench2.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;

import de.uni.freiburg.iig.telematik.swat.editor.graph.MXConstants;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class PalettePanel extends JPanel {

	private static final long serialVersionUID = -1156941541375286369L;
	
	protected JLabel selectedEntry = null;

    public PalettePanel(String placeShape, String transitionShape) {
        setLayout(new GridLayout(getComponentCount(), 1));
        addTransitionTemplate("Transition", new ImageIcon(PNEditor.class.getResource("/images/rectangle.png")), transitionShape, EditorProperties.getInstance().getDefaultTransitionWidth(), EditorProperties.getInstance().getDefaultTransitionHeight(), null);
		addPlaceTemplate("Place", new ImageIcon(PNEditor.class.getResource("/images/ellipse.png")), placeShape, EditorProperties.getInstance().getDefaultPlaceSize(), EditorProperties.getInstance().getDefaultPlaceSize(), null);
    }

    public void setSelectionEntry(JLabel entry, mxGraphTransferable t) {
        if (!entry.isEnabled()) {
            return;
        }
        JLabel previous = selectedEntry;
        selectedEntry = entry;

        if (previous != null) {
            previous.setBorder(null);
            previous.setOpaque(false);
        }

        if (selectedEntry != null) {
            selectedEntry.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(218, 186, 133).darker(), 1, true),
                    new LineBorder(new Color(218, 186, 133), 1, true)));
            selectedEntry.setBackground(new Color(255, 223, 113));
            selectedEntry.setOpaque(true);
        }
    }

    public void addEdgeTemplate(final String name, ImageIcon icon, String style, int width, int height, Object value) {
        mxGeometry geometry = new mxGeometry(0, 0, width, height);
        geometry.setTerminalPoint(new mxPoint(0, height), true);
        geometry.setTerminalPoint(new mxPoint(width, 0), false);
        geometry.setRelative(true);

        mxCell cell = new mxCell(value, geometry, style);
        cell.setEdge(true);

        addTemplate(name, icon, cell);
    }

    public void addPlaceTemplate(String name, ImageIcon icon, String style, int width, int height, Object value) {
        PNGraphCell cell = new PNGraphCell(value, new mxGeometry(0, 0, width, height), style, PNComponent.PLACE);
        cell.setVertex(true);
        addTemplate(name, icon, cell);
    }
    
    public void addTransitionTemplate(String name, ImageIcon icon, String style, int width, int height, Object value) {
        PNGraphCell cell = new PNGraphCell(value, new mxGeometry(0, 0, width, height), style, PNComponent.TRANSITION);
        cell.setVertex(true);
        addTemplate(name, icon, cell);
    }

    public void addTemplate(final String name, ImageIcon icon, mxCell cell) {
        mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
        final mxGraphTransferable t = new mxGraphTransferable(new Object[]{cell}, bounds);

        if (icon != null) {
            if (icon.getIconWidth() > 32 || icon.getIconHeight() > 32) {
                icon = new ImageIcon(icon.getImage().
                        getScaledInstance(32, 32, 0));
            }
        }

        final JLabel entry = new JLabel(icon);
        entry.setPreferredSize(new Dimension(50, 50));
        entry.setBackground(PalettePanel.this.getBackground().brighter());
        entry.setFont(new Font(entry.getFont().getFamily(), 0, 10));

        entry.setVerticalTextPosition(JLabel.BOTTOM);
        entry.setHorizontalTextPosition(JLabel.CENTER);
        entry.setIconTextGap(0);

        entry.setToolTipText(name);
        entry.setText(name);
        entry.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                setSelectionEntry(entry, t);
            }
        });
      

        DragGestureListener dragGestureListener = new DragGestureListener() {
            public void dragGestureRecognized(DragGestureEvent e) {
                if (!entry.isEnabled()) {
                    return;
                }
                e.startDrag(null, MXConstants.EMPTY_IMAGE, new Point(), t, null);
            }
        };

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(entry, DnDConstants.ACTION_COPY, dragGestureListener);
        add(entry);
    }
    
    
}
