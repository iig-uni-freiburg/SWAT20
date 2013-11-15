package de.uni.freiburg.iig.telematik.swat.editor.menu;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoRedoAction;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	public ToolBar(PNEditor pnEditor, int orientation) throws ParameterException {
        super(orientation);
        Validate.notNull(pnEditor);
        
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), getBorder()));
        setFloatable(false);

        add(pnEditor.bind("Save", new SaveAction(pnEditor), "/images/save.gif"));
        addSeparator();

        add(pnEditor.bind("Cut", TransferHandler.getCutAction(), "/images/cut.gif"));
        add(pnEditor.bind("Copy", TransferHandler.getCopyAction(), "/images/copy.gif"));
        add(pnEditor.bind("Paste", TransferHandler.getPasteAction(), "/images/paste.gif"));
        addSeparator();
        addSeparator();

        add(pnEditor.bind("Undo", new UndoRedoAction(pnEditor, true), "/images/undo.gif"));
        add(pnEditor.bind("Redo", new UndoRedoAction(pnEditor, false), "/images/redo.gif"));

        addSeparator();
//        Action temp = new AbstractAction("", new ImageIcon(Editor.class.getResource("/images/connector.gif"))) {
//
//            public void actionPerformed(ActionEvent e) {
//                Simulation s = new Simulation((Graph) editor.getGraphComponent().getGraph());
////                s.step();
//            }
//        };
//        add(temp);
//        add(new JLabel("Steps:"));
//        final JTextField field = new JTextField(String.valueOf(Properties.getInstance().getTests()), 8);
//        field.setMaximumSize(new Dimension(50, 50));
//        field.addKeyListener(new KeyAdapter() {
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//                char c = e.getKeyChar();
//                if (!Character.isDigit(c)) {
//                    e.consume();
//                }
//                super.keyPressed(e);
//                SwingUtilities.invokeLater(new Thread() {
//
//                    @Override
//                    public void run() {
//                        if (!field.getText().equals("")) {
//                            Properties.getInstance().setTests(Integer.parseInt(field.getText()));
//                        }
//                    }
//                });
//            }
//        });
//        add(field);
//        Action temp = new AbstractAction("", new ImageIcon(PNMLEditor.class.getResource("/images/connector.gif"))) {
//
//            public void actionPerformed(ActionEvent e) {
//
////                try {
////                    Simulation s = new Simulation((Graph) editor.getGraphComponent().getGraph());
////                    s.run();
////                    editor.getControlPanel().updateSimulation(s);
////                } catch (Exception ex) {
////                    JOptionPane.showMessageDialog(null, "Error. Make sure graph isn't empty and doesn't have terminal vertices");
////                }
//
//            }
//        };
//        add(temp);
    }
}
