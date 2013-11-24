package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import de.uni.freiburg.iig.telematik.swat.editor.actions.FontStyleAction;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.KeyValueAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoRedoAction;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;
	
	private boolean ignoreZoomChange = false;


	public ToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
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
        

		// Gets the list of available fonts from the local graphics environment
		// and adds some frequently used fonts at the beginning of the list
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		List<String> fonts = new ArrayList<String>();
		fonts.addAll(Arrays.asList(new String[] { "Helvetica", "Verdana",
				"Times New Roman", "Garamond", "Courier New", "-" }));
		fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));

		final JComboBox fontCombo = new JComboBox(fonts.toArray());
		fontCombo.setEditable(true);
		fontCombo.setMinimumSize(new Dimension(120, 0));
		fontCombo.setPreferredSize(new Dimension(120, 0));
		fontCombo.setMaximumSize(new Dimension(120, 100));
		add(fontCombo);
		
//		private PNEditor editor =pnEditor;

		fontCombo.addActionListener(new ActionListener()
		{
			

			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				String font = fontCombo.getSelectedItem().toString();

				if (font != null && !font.equals("-"))
				{
					mxGraph graph = pnEditor.getGraphComponent().getGraph();
					graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, font);
				}
			}
		});

		final JComboBox sizeCombo = new JComboBox(new Object[] { "6pt", "8pt",
				"9pt", "10pt", "12pt", "14pt", "18pt", "24pt", "30pt", "36pt",
				"48pt", "60pt" });
		sizeCombo.setEditable(true);
		sizeCombo.setMinimumSize(new Dimension(65, 0));
		sizeCombo.setPreferredSize(new Dimension(65, 0));
		sizeCombo.setMaximumSize(new Dimension(65, 100));
		add(sizeCombo);

		sizeCombo.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				mxGraph graph = pnEditor.getGraphComponent().getGraph();
				graph.setCellStyles(mxConstants.STYLE_FONTSIZE, sizeCombo
						.getSelectedItem().toString().replace("pt", ""));
			}
		});

		addSeparator();

		add(pnEditor.bind("Bold", new FontStyleAction(true),
				"/images/bold.gif"));
		add(pnEditor.bind("Italic", new FontStyleAction(false),
				"/images/italic.gif"));

		addSeparator();

//		add(pnEditor.bind("Left", new KeyValueAction(mxConstants.STYLE_ALIGN,
//				mxConstants.ALIGN_LEFT),
//				"/images/left.gif"));
//		add(pnEditor.bind("Center", new KeyValueAction(mxConstants.STYLE_ALIGN,
//				mxConstants.ALIGN_CENTER),
//				"/images/center.gif"));
//		add(pnEditor.bind("Right", new KeyValueAction(mxConstants.STYLE_ALIGN,
//				mxConstants.ALIGN_RIGHT),
//				"/images/right.gif"));
//
//		addSeparator();

		add(pnEditor.bind("Font", new ColorAction("Font",
				mxConstants.STYLE_FONTCOLOR),
				"/images/fontcolor.gif"));
		add(pnEditor.bind("Stroke", new ColorAction("Stroke",
				mxConstants.STYLE_STROKECOLOR),
				"/images/linecolor.gif"));
		add(pnEditor.bind("Fill", new ColorAction("Fill",
				mxConstants.STYLE_FILLCOLOR),
				"/images/fillcolor.gif"));

		addSeparator();

		final mxGraphView view = pnEditor.getGraphComponent().getGraph()
				.getView();
		final JComboBox zoomCombo = new JComboBox(new Object[] { "400%",
				"200%", "150%", "100%", "75%", "50%", mxResources.get("page"),
				mxResources.get("width"), mxResources.get("actualSize") });
		zoomCombo.setEditable(true);
		zoomCombo.setMinimumSize(new Dimension(75, 0));
		zoomCombo.setPreferredSize(new Dimension(75, 0));
		zoomCombo.setMaximumSize(new Dimension(75, 100));
		zoomCombo.setMaximumRowCount(9);
		add(zoomCombo);

		// Sets the zoom in the zoom combo the current value
		mxIEventListener scaleTracker = new mxIEventListener()
		{
			/**
			 * 
			 */
			public void invoke(Object sender, mxEventObject evt)
			{
				ignoreZoomChange = true;

				try
				{
					zoomCombo.setSelectedItem((int) Math.round(100 * view
							.getScale())
							+ "%");
				}
				finally
				{
					ignoreZoomChange = false;
				}
			}
		};

		// Installs the scale tracker to update the value in the combo box
		// if the zoom is changed from outside the combo box
		view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
		view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE,
				scaleTracker);

		// Invokes once to sync with the actual zoom value
		scaleTracker.invoke(null, null);

		zoomCombo.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				mxGraphComponent graphComponent = pnEditor.getGraphComponent();

				// Zoomcombo is changed when the scale is changed in the diagram
				// but the change is ignored here
				if (!ignoreZoomChange)
				{
					String zoom = zoomCombo.getSelectedItem().toString();

					if (zoom.equals(mxResources.get("page")))
					{
						graphComponent.setPageVisible(true);
						graphComponent
								.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
					}
					else if (zoom.equals(mxResources.get("width")))
					{
						graphComponent.setPageVisible(true);
						graphComponent
								.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
					}
					else if (zoom.equals(mxResources.get("actualSize")))
					{
						graphComponent.zoomActual();
					}
					else
					{
						try
						{
							zoom = zoom.replace("%", "");
							double scale = Math.min(16, Math.max(0.01,
									Double.parseDouble(zoom) / 100));
							graphComponent.zoomTo(scale, graphComponent
									.isCenterZoom());
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(pnEditor, ex
									.getMessage());
						}
					}
				}
			}
		});
	}
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

