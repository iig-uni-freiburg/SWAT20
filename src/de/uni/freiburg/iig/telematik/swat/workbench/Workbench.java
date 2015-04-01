package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.util.Calendar;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.uni.freiburg.iig.telematik.swat.bernhard.AnalyzePanelController;
import de.uni.freiburg.iig.telematik.swat.bernhard.AnalyzePanelPN;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.misc.errorhandling.ErrorStorage;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTabViewListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

public class Workbench extends JFrame implements SwatTreeViewListener, SwatTabViewListener, SwatStateListener {

	private static final long serialVersionUID = 6109154620023481119L;
	
	public static final Dimension PREFERRED_SIZE_WORKBENCH = new Dimension(1024,768);
	private static final Dimension PREFERRED_SIZE_PROPERTIES_PANEL = new Dimension(200, 768);
	private static final Dimension PREFERRED_SIZE_TREEVIEW_PANEL = new Dimension(250, 500);
	private static final Dimension PREFERRED_SIZE_CONSOLE_PANEL = new Dimension(300,80);
	private static final Dimension MINIMUM_SIZE_TAB_PANEL = new Dimension(300, 550);
	
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

	private Workbench() {
		super();
		setLookAndFeel();
		setUpGUI();
		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		int wdwLeft = (int) ((screenSize.width/2.0) - ((PREFERRED_SIZE_WORKBENCH.width + MessageDialog.PREFERRED_SIZE.width + 10)/2.0));
	    int wdwTop = screenSize.height / 2 - PREFERRED_SIZE_WORKBENCH.height / 2; 
		pack();
	    setLocation(wdwLeft, wdwTop);
		SwatState.getInstance().setOperatingMode(Workbench.this, OperatingMode.EDIT_MODE);
		SwatState.getInstance().addListener(this);
		SwatComponents.getInstance().addSwatComponentListener(treeView);
	}
	
	public static Workbench getInstance() {
		if (myWorkbench == null) {
			myWorkbench = new Workbench();
		}
		return myWorkbench;

	}

	/** Changes Look and Feel if running on Linux **/
	private void setLookAndFeel() {
		if (System.getProperty("os.name").toLowerCase().contains("nux")) {
			try {
				setLocationByPlatform(true);
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Could not set Look and Feel. Using standard");
			}
		} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				UIManager
						.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setUpGUI(){
		setTitle("SWAT 2.0");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(PREFERRED_SIZE_WORKBENCH);
		setResizable(true);
		
		setContentPane(getContent());
		setJMenuBar(getSwatMenu());
		pack();
		setVisible(true);
	}
	
	private JComponent getContent(){
		if(content == null){
			content = new JPanel(new BorderLayout());
			content.add(getSwatToolbar(), BorderLayout.NORTH);

			//content.add(getTreeView(), BorderLayout.WEST);

			//content.add(new JScrollPane(getPropertiesPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			//		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.EAST);
			content.add(getPropertiesPanel(), BorderLayout.EAST);
			//content.add(propertieSplit, BorderLayout.EAST);
			JSplitPane centerPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
			centerPanel.add(getTabView());
			centerPanel.add(getMessagePanel());
			JSplitPane middlePanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			JScrollPane scrollPane = new JScrollPane(getTreeView(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setSize(PREFERRED_SIZE_TREEVIEW_PANEL);
			middlePanel.add(scrollPane);
			middlePanel.add(centerPanel);

			//middlePanel.setMinimumSize(MINIMUM_SIZE_TAB_PANEL);
			//middlePanel.add(propertieSplit);
			//content.add(centerPanel, BorderLayout.CENTER);
			//JSplitPane thirdSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			//thirdSplit.add(middlePanel);
			//thirdSplit.add(getPropertiesPanel());
			//thirdSplit.setSize(PREFERRED_SIZE_PROPERTIES_PANEL);
			//thirdSplit.setPreferredSize(PREFERRED_SIZE_PROPERTIES_PANEL);
			content.add(middlePanel, BorderLayout.CENTER);
			centerPanel.setDividerLocation(0.8);
			//content.add(thirdSplit, BorderLayout.CENTER);
		}
		return content;
	}
	
	public SwatToolbar getSwatToolbar() {
		if(toolbar == null){
			toolbar = new SwatToolbar(getTabView(), getTreeView());
		}
		return toolbar;
	}
	
	private JPanel getPropertiesPanel(){
		if(properties == null){
			properties = new JPanel(new BorderLayout());
			properties.setPreferredSize(PREFERRED_SIZE_PROPERTIES_PANEL);
			properties.setMinimumSize(PREFERRED_SIZE_PROPERTIES_PANEL);
			properties.setSize(PREFERRED_SIZE_PROPERTIES_PANEL);
		}
		return properties;
	}
	
	public SwatTabView getTabView() {
		if(tabView == null){
			tabView = SwatTabView.getInstance();
			tabView.setMinimumSize(MINIMUM_SIZE_TAB_PANEL);
			tabView.setPreferredSize(MINIMUM_SIZE_TAB_PANEL);
			tabView.addTabViewListener(this);
		}
		return tabView;
	}
	
	public SwatMenuBar getSwatMenu() {
		if(menuBar == null){
			menuBar = new SwatMenuBar(getTabView(), getTreeView());
		}
		return menuBar;
	}
	
	public SwatTreeView getTreeView() {
		 UIManager.put("Tree.rendererFillBackground", false);
		if(treeView == null){
			treeView = SwatTreeView.getInstance();
			treeView.setMinimumSize(PREFERRED_SIZE_TREEVIEW_PANEL);
			treeView.addTreeViewListener(this);
		}
		return treeView;
	}
	
	private static JComponent getMessagePanel() {
		if(messagePanel == null){
			messagePanel = new JTabbedPane();
			messagePanel.add(getErrorArea(), "Errors");
			messagePanel.add(getConsoleArea(), "Console");
			messagePanel.setPreferredSize(PREFERRED_SIZE_CONSOLE_PANEL);
			messagePanel.setMinimumSize(PREFERRED_SIZE_CONSOLE_PANEL);
			messagePanel.setMaximumSize(PREFERRED_SIZE_CONSOLE_PANEL);
		}
		return messagePanel;
	}
	
	private static JTextArea getConsoleArea() {
		if(console == null){
			console = new JTextArea();
			console.setEditable(false);
		}
		return console;
	}
	
	private static JTextArea getErrorArea() {
		if(errors == null){
			errors = new JTextArea();
			errors.setEditable(false);
		}
		return errors;
	}
	
	public static void consoleMessage(String message) {
		getConsoleArea().append(
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":"
						+ Calendar.getInstance().get(Calendar.SECOND) + " - ");
		getConsoleArea().append(message);
		getConsoleArea().append("\n\r");
		try {
			messagePanel.setSelectedIndex(1);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	public static void errorMessage(String message, Exception e, boolean showPopup) {
		String messageToShow = "";
		if (!message.isEmpty())
			messageToShow = message + " ";
		if (e != null)
			messageToShow += e.getMessage();
		if (showPopup)
			JOptionPane.showMessageDialog(myWorkbench, messageToShow);
		errorMessage(messageToShow);

		try {
			ErrorStorage.getInstance().addMessage(message, e);
		} catch (Exception ex) {
		}

	}

	private static void errorMessageWithNotification(String message) {
		JOptionPane.showMessageDialog(getInstance(), message);
		errorMessage(message);
	}

	private static void errorMessage(String message) {
		getConsoleArea().append(
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":"
						+ Calendar.getInstance().get(Calendar.SECOND) + " - ");
		getConsoleArea().append(message);
		getConsoleArea().append("\n\r");
		try {
			messagePanel.setSelectedIndex(0);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	@Override
	public void componentSelected(SwatTreeNode node) {
		//Information from TreeView: New node activated -> relay to TabView
		getTabView().componentSelected(node);

		//Update Toolbar
		updateToolbar();
	}

	@Override
	public void componentActivated(SwatTreeNode node) {

		if (getTabView().containsComponent(node))
			return;

		ViewComponent swatComponent = null;
		// add SwatTreeNode to tab and get its swatComponent to make its propertyView
		swatComponent = getTabView().addNewTab(node);
		getPropertiesPanel().removeAll();

		if (SwatState.getInstance().getOperatingMode() == OperatingMode.EDIT_MODE) {
			getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
		}

		else if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
			String name = node.getDisplayName();
			if (swatComponent instanceof PNEditorComponent) {
				if (node.getObjectType() == SwatComponentType.PETRI_NET_ANALYSIS) {
					name = ((SwatTreeNode) node.getParent()).getDisplayName();
				}
			}
			//Here: Analyze Panel is loaded
			getPropertiesPanel().add(AnalyzePanelController.getInstance().getAnalyzePanel(name, swatComponent).getContent());
		}
		getPropertiesPanel().validate();
		getPropertiesPanel().repaint();
		getPropertiesPanel().updateUI();

		//Update Toolbar
		updateToolbar();
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
		if (index < 0)
			return; //no tabs inside
		// Update Properties Panel & Toolbar according to active tab
		getPropertiesPanel().removeAll();
		
		//pack();
		if(SwatState.getInstance().getOperatingMode() == OperatingMode.EDIT_MODE) {
			getPropertiesPanel().add(new ScrollPane().add(component.getPropertiesView()));
		} else if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
			getPropertiesPanel().add(AnalyzePanelController.getInstance().getAnalyzePanel(component.getName(), component).getContent());
				
		}
		getPropertiesPanel().repaint();
		getPropertiesPanel().updateUI();
		updateToolbar();
	}

	@Override
	public void operatingModeChanged() {
		try {
			//Update Properties View & Toolbar
		ViewComponent swatComponent = (ViewComponent) getTabView().getSelectedComponent();
		getPropertiesPanel().removeAll();
		if(SwatState.getInstance().getOperatingMode() == OperatingMode.EDIT_MODE) {
			getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
		} else if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
			if(swatComponent instanceof PNEditorComponent){
				getPropertiesPanel().add(AnalyzePanelController.getInstance().getAnalyzePanel(swatComponent.getName(), swatComponent).getContent());
				//((PNEditor)swatComponent).setEnabled(false);
			}
				if (swatComponent instanceof LogFileViewer)
				getPropertiesPanel().add(AnalyzePanelController.getInstance().getAnalyzePanel(swatComponent.getName(), swatComponent).getContent());
				//getPropertiesPanel().add(((LogFileViewer) swatComponent).getPropertiesView());
		}
		
			//pack();
			getPropertiesPanel().repaint();
			getPropertiesPanel().updateUI();
		//getPropertiesPanel().repaint();
		updateToolbar();
		} catch (NullPointerException e) {
			//Can happens when Arista-Flow analysis is called with no active tabs

		}
	}

	/** check if a PNEditor got activated and update Toolbar if needed **/
	private void updateToolbar() {
		if (getTabView().getSelectedComponent() instanceof PNEditorComponent) {
			try {
				PNEditorComponent editor = (PNEditorComponent) getTabView().getSelectedComponent();
				getSwatToolbar().clear();
			
				if (SwatState.getInstance().getOperatingMode() == OperatingMode.ANALYSIS_MODE) {
					getSwatToolbar().add(((AnalyzePanelPN)AnalyzePanelController.getInstance().getAnalyzePanel(getTabView().getSelectedComponent().getName(), (ViewComponent) editor)).getToolBar());
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

}
