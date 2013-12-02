package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperty;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView.PropertiesField;

public class PNCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -8137148608944410589L;
	private ImageIcon placeIcon = new ImageIcon(getClass().getResource("/images/ellipse.png"));
	private ImageIcon transitionIcon = new ImageIcon(getClass().getResource("/images/rectangle.png"));
	private ImageIcon arcIcon = new ImageIcon(getClass().getResource("/images/arrow.png"));

	@Override
	public Component getTreeCellEditorComponent(final JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		Component container = super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
		final PNTreeNode node = (PNTreeNode) value;
		Component result = null;
		container.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {

			}

			@Override
			public void focusLost(FocusEvent arg0) {
				tree.stopEditing();
			}

		});

		switch (node.getFieldType()) {
		case LEAF:
			result = node.getTable();
			break;

		}

		return result;
	}

	public PNCellEditor(JTextField name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getCellEditorValue() {
		return super.getCellEditorValue();
	}

	@Override
	public boolean isCellEditable(EventObject event) {
		if (event.getSource() instanceof JTree) {
			JTree tree = (JTree) event.getSource();
			PNTreeNode node = (PNTreeNode) tree.getLastSelectedPathComponent();

			if (node != null) {
				switch (node.getFieldType()) {
				case ARCS:
					break;
				case LEAF:
					MouseEvent mouseEvent = (MouseEvent) event;
					Point mouseLocation = mouseEvent.getPoint();
					Rectangle rectLastSelected = tree.getPathBounds(new TreePath(node.getPath()));
					return rectContainsPoint(new Point((int) rectLastSelected.getMinX(), (int) rectLastSelected.getMinY()),
							new Point((int) rectLastSelected.getMaxX(), (int) rectLastSelected.getMaxY()), mouseLocation);
				case PLACES:
					break;
				case ROOT:
					break;

				case TRANSITIONS:
					break;
				default:
					break;

				}
			}
		}

		return false;
	}

	// Check if cursor location lies in edited field
	private boolean rectContainsPoint(Point start, Point end, Point point) {
		return point.equals(max(start, min(end, point)));
	}

	private Point min(Point p1, Point p2) {
		return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
	}

	private Point max(Point p1, Point p2) {
		return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
	}

}
