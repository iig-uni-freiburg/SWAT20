package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.mxgraph.model.*;
import com.mxgraph.swing.util.*;
import com.mxgraph.util.*;

import de.unifreiburg.iig.bpworkbench2.editor.relict.mxConstants;

public class PalettePanel extends JPanel {

    protected JLabel selectedEntry = null;

    public PalettePanel() {
        setLayout(new GridLayout(getComponentCount(), 1));
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

    public void addEdgeTemplate(final String name, ImageIcon icon,
            String style, int width, int height, Object value) {
        mxGeometry geometry = new mxGeometry(0, 0, width, height);
        geometry.setTerminalPoint(new mxPoint(0, height), true);
        geometry.setTerminalPoint(new mxPoint(width, 0), false);
        geometry.setRelative(true);

        mxCell cell = new mxCell(value, geometry, style);
        cell.setEdge(true);

        addTemplate(name, icon, cell);
    }

    public void addTemplate(final String name, ImageIcon icon, String style,
            int width, int height, Object value) {
        mxCell cell = new mxCell(value, new mxGeometry(0, 0, width, height),
                style);
        cell.setVertex(true);

        addTemplate(name, icon, cell);
    }

    public void addTemplate(final String name, ImageIcon icon, mxCell cell) {
        mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
        final mxGraphTransferable t = new mxGraphTransferable(
                new Object[]{cell}, bounds);

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
                e.startDrag(null, mxConstants.EMPTY_IMAGE, new Point(),
                        t, null);
            }
        };

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(entry,
                DnDConstants.ACTION_COPY, dragGestureListener);

        add(entry);
    }
}
