package de.uni.freiburg.iig.telematik.swat.workbench;

import de.invation.code.toval.graphic.dialog.MessageDialog;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.uni.freiburg.iig.telematik.swat.analysis.AnalysisController;
import de.uni.freiburg.iig.telematik.swat.ext.MultiSplitLayout;
import de.uni.freiburg.iig.telematik.swat.ext.MultiSplitLayout.Divider;
import de.uni.freiburg.iig.telematik.swat.ext.MultiSplitLayout.Leaf;
import de.uni.freiburg.iig.telematik.swat.ext.MultiSplitLayout.Split;
import de.uni.freiburg.iig.telematik.swat.ext.MultiSplitPane;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogViewViewer;
import de.uni.freiburg.iig.telematik.swat.misc.errorhandling.ErrorStorage;
import de.uni.freiburg.iig.telematik.swat.patterns.PatternException;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.CloseSWATAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTabViewListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import javax.swing.UnsupportedLookAndFeelException;

public class Workbench extends JFrame implements SwatTreeViewListener, SwatTabViewListener, SwatStateListener, ComponentListener {

    private static final long serialVersionUID = 6109154620023481119L;

//	public static final Dimension PREFERRED_SIZE_WORKBENCH = new Dimension(1024,768);
    private static final int MIN_WORKBENCH_HEIGHT = 700;
    private static final int MIN_CONSOLE_PANEL_HEIGHT = 300;
    private static final Dimension PREFERRED_SIZE_PROPERTIES_PANEL = new Dimension(200, MIN_WORKBENCH_HEIGHT);
    private static final Dimension PREFERRED_SIZE_TREEVIEW_PANEL = new Dimension(200, 600);
    private static final Dimension PREFERRED_SIZE_CONSOLE_PANEL = new Dimension(400, 40);
    private static final Dimension MINIMUM_SIZE_TAB_PANEL = new Dimension(600, MIN_WORKBENCH_HEIGHT - MIN_CONSOLE_PANEL_HEIGHT);
    private static final boolean SHOW_STACK_TRACES = true;

//	private MultiSplitPane splitPane = null;
    private SwatToolbar toolbar = null;
    private SwatTreeView treeView = null;
    private SwatTabView tabView = null;
    private SwatMenuBar menuBar = null;
    private static JTabbedPane messagePanel = null;
    private JPanel properties = null;
    private JPanel content = null;
    private static JTextArea console = null;
    private static JTextArea errors = null;
    private static Workbench myWorkbench;

    private Workbench() throws Exception {
        super();
        setLookAndFeel();
        setUpGUI();
        pack();
        Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        int wdwLeft = (int) ((screenSize.width / 2.0) - ((getWidth() + MessageDialog.PREFERRED_SIZE.width + 10) / 2.0));
        int wdwTop = screenSize.height / 2 - getHeight() / 2;
        setLocation(wdwLeft, wdwTop);
        SwatState.getInstance().setOperatingMode(Workbench.this, OperatingMode.EDIT_MODE);
        SwatState.getInstance().addListener(this);
        SwatComponents.getInstance().addComponentListener(getTreeView());
    }

    public static Workbench getInstance() throws Exception {
        if (myWorkbench == null) {
            myWorkbench = new Workbench();
        }
        return myWorkbench;
    }

    /**
     * Changes Look and Feel if running on Linux *
     */
    private void setLookAndFeel() {
        if (System.getProperty("os.name").toLowerCase().contains("nux")) {
            try {
                setLocationByPlatform(true);
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                MessageDialog.getInstance().addMessage("Could not set Look and Feel. Using standard");
            }
        } else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                UIManager
                        .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setUpGUI() {
        setTitle("SWAT 2.0");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // set close action
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	new CloseSWATAction().actionPerformed(new ActionEvent(this, 0, "close"));
            }
        });
        
//		setPreferredSize(PREFERRED_SIZE_WORKBENCH);
        setResizable(true);

        try {
            setContentPane(getContent());
        } catch (Exception ex) {
            MessageDialog.getInstance().addMessage("Could not create GUI, reason "+ex.getMessage());
        }
        setJMenuBar(getSwatMenu());
        pack();
        setVisible(true);
    }

    private JComponent getContent() throws Exception {
        if (content == null) {
            content = new JPanel(new BorderLayout());
//			String layoutDef = "(COLUMN top (ROW left (COLUMN center center.bottom) right) bottom)";
//			MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
            content.add(getSwatToolbar(), BorderLayout.PAGE_START);

            Leaf left = new Leaf("left");
            left.setWeight(0.2);
            Leaf right = new Leaf("right");
            right.setWeight(0.2);
            Leaf center = new Leaf("center");
            center.setWeight(0.7);
            Leaf centerBottom = new Leaf("center.bottom");
            centerBottom.setWeight(0.3);

            MultiSplitLayout.Split centerCol = new Split();
            centerCol.setRowLayout(false);
            List centerColChildren = Arrays.asList(center, new Divider(), centerBottom);
            centerCol.setChildren(centerColChildren);
            centerCol.setWeight(0.6);
            MultiSplitLayout.Split centerRow = new Split();
            centerRow.setRowLayout(true);
            List centerRowChildren = Arrays.asList(left, new Divider(), centerCol, new Divider(), right);
            centerRow.setChildren(centerRowChildren);

            MultiSplitPane multiSplitPane = new MultiSplitPane();
            multiSplitPane.getMultiSplitLayout().setModel(centerRow);
//			multiSplitPane.setDividerPainter(new DividerPainter() {
//				
//				@Override
//				public void paint(Graphics g, Divider divider) {
//					Rectangle rect = divider.getBounds();
//					g.drawRect(rect.x, rect.y, rect.width, rect.height);
//				}
//			});
   
            multiSplitPane.add(new JScrollPane(getTreeView()), "left");
            multiSplitPane.add(getPropertiesPanel(), "right");
            multiSplitPane.add(getTabView(), "center");
            multiSplitPane.add(getMessagePanel(), "center.bottom");
            content.add(multiSplitPane, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setPreferredSize(new Dimension(500, 40));
            bottomPanel.setMinimumSize(new Dimension(500, 40));
            bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
            content.add(bottomPanel, BorderLayout.PAGE_END);

//			
//			content.add(getSwatToolbar(), BorderLayout.NORTH);
//
//			//content.add(getTreeView(), BorderLayout.WEST);
//
//			//content.add(new JScrollPane(getPropertiesPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//			//		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.EAST);
//			content.add(getPropertiesPanel(), BorderLayout.EAST);
//			//content.add(propertieSplit, BorderLayout.EAST);
//			JSplitPane centerPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
//			centerPanel.add(getTabView());
//			centerPanel.add(getMessagePanel());
//			JSplitPane middlePanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
//			JScrollPane scrollPane = new JScrollPane(getTreeView(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//			scrollPane.setSize(PREFERRED_SIZE_TREEVIEW_PANEL);
//			middlePanel.add(scrollPane);
//			middlePanel.add(centerPanel);
//
//			//middlePanel.setMinimumSize(MINIMUM_SIZE_TAB_PANEL);
//			//middlePanel.add(propertieSplit);
//			//content.add(centerPanel, BorderLayout.CENTER);
//			//JSplitPane thirdSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
//			//thirdSplit.add(middlePanel);
//			//thirdSplit.add(getPropertiesPanel());
//			//thirdSplit.setSize(PREFERRED_SIZE_PROPERTIES_PANEL);
//			//thirdSplit.setPreferredSize(PREFERRED_SIZE_PROPERTIES_PANEL);
//			content.add(middlePanel, BorderLayout.CENTER);
//			centerPanel.setDividerLocation(0.8);
//			//content.add(thirdSplit, BorderLayout.CENTER);
        }
        return content;
    }

    public SwatToolbar getSwatToolbar() throws Exception {
        if (toolbar == null) {
            toolbar = new SwatToolbar(getTabView(), getTreeView());
        }
        return toolbar;
    }

    private JPanel getPropertiesPanel() {
        if (properties == null) {
            properties = new JPanel(new BorderLayout());
            properties.setPreferredSize(PREFERRED_SIZE_PROPERTIES_PANEL);
            properties.setMinimumSize(PREFERRED_SIZE_PROPERTIES_PANEL);
//			properties.setSize(PREFERRED_SIZE_PROPERTIES_PANEL);
        }
        return properties;
    }

    public SwatTabView getTabView() throws Exception {
        if (tabView == null) {
            tabView = SwatTabView.getInstance();
            tabView.setMinimumSize(MINIMUM_SIZE_TAB_PANEL);
            tabView.setPreferredSize(MINIMUM_SIZE_TAB_PANEL);
            tabView.addTabViewListener(this);
        }
        return tabView;
    }

    public SwatMenuBar getSwatMenu() {
        if (menuBar == null) {
            menuBar = new SwatMenuBar();
        }
        return menuBar;
    }

    public final SwatTreeView getTreeView() throws Exception {
        UIManager.put("Tree.rendererFillBackground", false);
        if (treeView == null) {
            treeView = SwatTreeView.getInstance();
            treeView.setPreferredSize(PREFERRED_SIZE_TREEVIEW_PANEL);
            treeView.setMinimumSize(PREFERRED_SIZE_TREEVIEW_PANEL);
            treeView.addTreeViewListener(this);
        }
        return treeView;
    }

    /** return the name of the currently active componen
         * @return t**/
    public String getNameOfCurrentComponent() {
        Object o = tabView.getSelectedComponent();
        if (o instanceof PNEditorComponent) {
            return ((PNEditorComponent) o).getNetContainer().getPetriNet().getName();
        } else if (o instanceof LogFileViewer) {
            return ((LogFileViewer) o).getName();
        } else if (o instanceof LogViewViewer) {
            return ((LogViewViewer) o).getName();
        }
        return "";
    }
    
    /** return true if current viewed component is a LogFileViewer and the corresponding log is not yet parse
         * @return d**/
    public boolean getCurrentComponentNeedsParsing(){
    	Object o = tabView.getSelectedComponent();
    	if(o instanceof PNEditorComponent) {
    		return false;
        } else if (o instanceof LogFileViewer) {
    		return !((LogFileViewer)o).getModel().hasLogReaderSet();
    	} else if (o instanceof LogViewViewer) {
    		return !((LogViewViewer)o).getModel().hasLogReaderSet();
    	}
    	return false;
    }
    
    /** return true if current viewed component is a LogFileViewer and the corresponding log is bigger than 2 M
         * @return B**/
    public boolean getCurrentComponentsSourceSizeTooBig(){
    	Object o = tabView.getSelectedComponent();
    	if(o instanceof PNEditorComponent) {
    		return false;
        } else if (o instanceof LogFileViewer) {
    		return ((LogFileViewer)o).getModel().getFileReference().length()>2097152l;
    	} else if (o instanceof LogViewViewer) {
    		return ((LogViewViewer)o).getModel().getFileReference().length()>2097152l;
    	}
    	return false;
    }

    /** get the type of the currently active componen
         * @return t**/
    public SwatComponentType getTypeOfCurrentComponent() {
        Object o = tabView.getSelectedComponent();
        if (o instanceof PNEditorComponent) {
            return SwatComponentType.PETRI_NET;
        } else if (o instanceof LogFileViewer) {
                LogFileViewer lfv = (LogFileViewer) o;
                switch (lfv.getModel().getType()) {
                        case Aristaflow:
                                return SwatComponentType.ARISTAFLOW_LOG;
                        case MXML:
                                return SwatComponentType.MXML_LOG;
                        default:
                                return SwatComponentType.XES_LOG;
                }
        }  else if (o instanceof LogViewViewer) {
                return SwatComponentType.LOG_VIEW;
        } else {
            return null;
        }
    }

    /** return the hash code of the currently active componen
         * @return t**/
    public int getHashOfCurrentComponent() {
        Object o = tabView.getSelectedComponent();
        if (o instanceof PNEditorComponent) {
            return ((PNEditorComponent) o).getNetContainer().getPetriNet().hashCode();
        } else if (o instanceof LogFileViewer) {
            return ((LogFileViewer) o).hashCode();
        } else if (o instanceof LogViewViewer) {
            return ((LogViewViewer) o).hashCode();
        }
        return -1;
    }

    private static JComponent getMessagePanel() {
        if (messagePanel == null) {
            messagePanel = new JTabbedPane();
            messagePanel.add(getErrorArea(), "Errors");
            messagePanel.add(getConsoleArea(), "Console");
            messagePanel.setPreferredSize(PREFERRED_SIZE_CONSOLE_PANEL);
            messagePanel.setMinimumSize(PREFERRED_SIZE_CONSOLE_PANEL);
//			messagePanel.setMaximumSize(PREFERRED_SIZE_CONSOLE_PANEL);
        }
        return messagePanel;
    }

    private static JTextArea getConsoleArea() {
        if (console == null) {
            console = new JTextArea();
            console.setEditable(false);
        }
        return console;
    }

    private static JTextArea getErrorArea() {
        if (errors == null) {
            errors = new JTextArea();
            errors.setEditable(false);
        }
        return errors;
    }

	public static void consoleMessage(String message) {
		if (myWorkbench != null) {
			getConsoleArea().append(
					Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE)
							+ ":" + Calendar.getInstance().get(Calendar.SECOND) + " - ");
			getConsoleArea().append(message);
			getConsoleArea().append("\n\r");
			try {
				messagePanel.setSelectedIndex(1);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		} // else: do not instantiate SWAT
	}

    public static void errorMessage(String message, Exception e, boolean showPopup) {
        String messageToShow = "";
        if (!message.isEmpty()) {
            messageToShow = message + ": ";
        } if (e != null) {
            messageToShow += e.getMessage();
        }
        if (showPopup) {
            JOptionPane.showMessageDialog(myWorkbench, messageToShow);
        }
        errorMessage(messageToShow);

        ErrorStorage.getInstance().addMessage(message, e);
        if (SHOW_STACK_TRACES) e.printStackTrace();
    }

	private static void errorMessage(String message) {
		if (myWorkbench != null) {
			getConsoleArea().append(
					Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE)
							+ ":" + Calendar.getInstance().get(Calendar.SECOND) + " - ");
			getConsoleArea().append(message);
			getConsoleArea().append("\n\r");
			try {
				messagePanel.setSelectedIndex(0);
			} catch (ArrayIndexOutOfBoundsException e) {
				// only for message Panel
			}
		} // else: do not instantiate SWAT
	}

    @Override
    public void componentSelected(SwatTreeNode node) {
        try {
            //Information from TreeView: New node activated -> relay to TabView
            getTabView().componentSelected(node);
            //Update Toolbar
            updateToolbar();
        } catch (Exception ex) {
            errorMessage("Could not select Component", ex, false);
        }
    }

    @Override
    public void componentActivated(SwatTreeNode node) {
        try {
            if (getTabView().containsComponent(node)) return;
            
            ViewComponent swatComponent = null;
            // add SwatTreeNode to tab and get its swatComponent to make its propertyView
            swatComponent = getTabView().addNewTab(node);
            getPropertiesPanel().removeAll();
            
            if (SwatState.getInstance().getOperatingMode() == OperatingMode.EDIT_MODE) {
                getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
            } else if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
                String name = node.getDisplayName();
                if (swatComponent instanceof PNEditorComponent) {
                    if (node.getObjectType() == SwatComponentType.PETRI_NET_ANALYSIS) {
                        name = ((SwatTreeNode) node.getParent()).getDisplayName();
                    }
                }
                //Here: Analyze Panel is loaded
                //getPropertiesPanel().add(AnalyzePanelController.getInstance().getAnalyzePanel(name, swatComponent).getContent());
                try {
                    getPropertiesPanel().add(getPropertiesPanel().add(AnalysisController.getInstance(swatComponent).getAnalyzePanel()));
                } catch (PatternException e) {
                    errorMessage("Cannot load analysis panel", e, true);
                }
            }
            getPropertiesPanel().validate();
            getPropertiesPanel().repaint();
            getPropertiesPanel().updateUI();
            
            //Update Toolbar
            updateToolbar();
        } catch (Exception ex) {
            errorMessage("Could not select Component", ex, false);
        }
    }

//	@SuppressWarnings("rawtypes")
//	private WorkbenchComponent getWorkbenchComponent(SwatTreeNode node) {
//		switch (node.getObjectType()) {
//		case LABELING:
//			// TODO:
//			break;
//		case PETRI_NET:
//			AbstractGraphicalPN petriNet = (AbstractGraphicalPN) node.getUserObject();
//			if(petriNet instanceof GraphicalPTNet){
//				return new PTNetEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getPetriNetFile(petriNet.getPetriNet().getName()));
//			} else if(petriNet instanceof GraphicalCPN){
////				return new CPNEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getFile(petriNet));
//			} else if(petriNet instanceof GraphicalIFNet){
////				return new IFNetEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getFile(petriNet));
//			}
//			break;
////		case LOG_FILE:
////			// User Object is of type LogFileViewer
////			return (LogFileViewer) node.getUserObject();
//		}
//		return null;
//	}
    @Override
    public void activeTabChanged(int index, ViewComponent component) {
        try {
            updateToolbar();
        } catch (Exception ex) {
            errorMessage("Could not update Toolbar", ex, false);
        }
    	// needed to clear the property panel after the last tab is closed
    	getPropertiesPanel().removeAll();
        getPropertiesPanel().repaint();
        
        if (index < 0) {
            return; //no tabs inside
        }		// Update Properties Panel & Toolbar according to active tab
        getPropertiesPanel().removeAll();

        //pack();
        if (SwatState.getInstance().getOperatingMode() == OperatingMode.EDIT_MODE) {
            getPropertiesPanel().add(new ScrollPane().add(component.getPropertiesView()));
        } else if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
            try {
            	if(!(getCurrentComponentNeedsParsing() && getCurrentComponentsSourceSizeTooBig()))
                getPropertiesPanel().add(AnalysisController.getInstance(component).getAnalyzePanel());
            	else {
            		//set Edit Radio Button to edit
            		//getSwatToolbar().setEditMode(true);
            		SwatState.getInstance().setOperatingMode(null, OperatingMode.EDIT_MODE);
            	}
            } catch (PatternException e) {
                errorMessage("Cannot load analysis panel", e, true);
            } catch (Exception ex) {
                errorMessage("Cannot load analysis panel", ex, true);
            }

        }
        getPropertiesPanel().repaint();
        getPropertiesPanel().updateUI();
    }

    @Override
    public void operatingModeChanged() {
        try {
            // Update Properties View & Toolbar
            ViewComponent swatComponent = (ViewComponent) getTabView().getSelectedComponent();
            if (swatComponent == null) return;
            getPropertiesPanel().removeAll();
            if (SwatState.getInstance().getOperatingMode() == OperatingMode.EDIT_MODE) {
                getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
            } else if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
                if (swatComponent instanceof PNEditorComponent) {
                    try {
                        getPropertiesPanel().add(AnalysisController.getInstance(swatComponent).getAnalyzePanel());
                    } catch (PatternException e) {
                        errorMessage("Cannot load analysis panel", e, true);
                    }
                    // ((PNEditor)swatComponent).setEnabled(false);
                } else if ((swatComponent instanceof LogFileViewer) || (swatComponent instanceof LogViewViewer)) {
                    try {
                        getPropertiesPanel().add(AnalysisController.getInstance(swatComponent).getAnalyzePanel());
                    } catch (PatternException e) {
                        errorMessage("Cannot load analysis panel", e, true);
                    }
                }
                // getPropertiesPanel().add(((LogFileViewer) swatComponent).getPropertiesView());
            }
            // pack();
            getPropertiesPanel().repaint();
            getPropertiesPanel().updateUI();
            // getPropertiesPanel().repaint();
            updateToolbar();
        } catch (NullPointerException e) {
            // Can happens when Arista-Flow analysis is called with no active tabs
            errorMessage("Nothing to display: No active tab", e, true);

        } catch (Exception ex) {
           errorMessage("could not change operating mode", ex, true);
        }
    }

    /**
     * check if a PNEditor got activated and update Toolbar if needed *
     */
    private void updateToolbar() throws Exception {
        if (getTabView().getSelectedComponent() instanceof PNEditorComponent) {
            try {
                PNEditorComponent editor = (PNEditorComponent) getTabView().getSelectedComponent();
                getSwatToolbar().clear();

                if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
                    //getSwatToolbar().add(((AnalyzePanelPN)AnalyzePanelController.getInstance().getAnalyzePanel(getTabView().getSelectedComponent().getName(), (ViewComponent) editor)).getToolBar());
                } else {
                    getSwatToolbar().add(editor.getEditorToolbar());
                }
            } catch (Exception e) {

            }
        } else {
            //remove Toolbar-Addons
            getSwatToolbar().clear();
        }
        getSwatToolbar().validate();
        getSwatToolbar().repaint();
    }

    @Override
    public void componentAdded(Object component) throws ProjectComponentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentRemoved(Object component) throws ProjectComponentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentRenamed(Object component, String oldName, String newName) throws ProjectComponentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentsChanged() throws ProjectComponentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
