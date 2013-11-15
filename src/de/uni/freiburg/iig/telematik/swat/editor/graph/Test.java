package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.EventObject;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * @see http://stackoverflow.com/a/15738813/230513
 * @see http://stackoverflow.com/q/15625424/230513
 */
public class Test {

    private static Icon one;
    private static Icon two;

    private void display() {
        JFrame f = new JFrame("Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout());
        final JTree tree = new JTree();
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        final TreeRenderer renderer = new TreeRenderer();
        tree.setCellRenderer(renderer);
        tree.setCellEditor(new TreeEditor(tree, renderer));
        tree.setEditable(true);
        tree.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startEditing");
     
        
        JPanel panel = new JPanel();
        JButton button = new JButton("first");
        JButton button2 = new JButton("second");
        button.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				   System.out.println(tree.getSelectionPath());
				     System.out.println(tree.getSelectionModel());
				TreePath path = tree.getSelectionPath();
				TreeSelectionModel path2 = tree.getSelectionModel();
//				path2.
//				tree.getSelectionModel()
				tree.setSelectionPath(path);
			}
        	
        });
        panel.add(button);
        f.add(button);
        f.add(button2);
        f.add(new JScrollPane(tree));
        
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static class TreeRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            String s = node.getUserObject().toString();
            if ("colors".equals(s)) {
                setOpenIcon(one);
                setClosedIcon(one);
            } else if ("sports".equals(s)) {
                setOpenIcon(two);
                setClosedIcon(two);
            } else {
                setOpenIcon(getDefaultOpenIcon());
                setClosedIcon(getDefaultClosedIcon());
            }
            super.getTreeCellRendererComponent(
                tree, value, sel, exp, leaf, row, hasFocus);
            return this;
        }
    }

    private static class TreeEditor extends DefaultTreeCellEditor {

        public TreeEditor(JTree tree, DefaultTreeCellRenderer renderer) {
            super(tree, renderer);
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value,
            boolean isSelected, boolean exp, boolean leaf, int row) {
            Component c = super.getTreeCellEditorComponent(
                tree, value, isSelected, exp, leaf, row);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            String s = node.getUserObject().toString();
            if ("colors".equals(s)) {
                editingIcon = one;
            } else if ("sports".equals(s)) {
                editingIcon = two;
            }
            return c;
        }

        @Override
        protected boolean canEditImmediately(EventObject event) {
            if ((event instanceof MouseEvent)
                && SwingUtilities.isLeftMouseButton((MouseEvent) event)) {
                MouseEvent me = (MouseEvent) event;

                return ((me.getClickCount() >= 1)
                    && inHitRegion(me.getX(), me.getY()));
            }
            return (event == null);
        }
    }

    public static void main(String[] args) throws Exception {
        one = new ImageIcon(ImageIO.read(
            new URL("http://i.imgur.com/HtHJkfI.png")));
        two = new ImageIcon(ImageIO.read(
            new URL("http://i.imgur.com/w5jAp5c.png")));
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Test().display();
            }
        });
    }
}