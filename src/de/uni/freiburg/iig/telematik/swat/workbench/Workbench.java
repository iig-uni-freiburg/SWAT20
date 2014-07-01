package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;
import de.uni.freiburg.iig.telematik.swat.sciff.presenter.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTabViewListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

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
	private JComponent messagePanel = null;
	private JPanel properties = null;
	private JPanel content = null;
	private JTextArea console = null;
	private JTextArea errors = null;

	public Workbench() {
		super();
		//setLookAndFeel();
		setUpGUI();
		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		int wdwLeft = (int) ((screenSize.width/2.0) - ((PREFERRED_SIZE_WORKBENCH.width + MessageDialog.PREFERRED_SIZE.width + 10)/2.0));
	    int wdwTop = screenSize.height / 2 - PREFERRED_SIZE_WORKBENCH.height / 2; 
		pack();
	    setLocation(wdwLeft, wdwTop);
		SwatState.getInstance().setOperatingMode(Workbench.this, OperatingMode.EDIT_MODE);
		SwatState.getInstance().addListener(this);
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
	
	private SwatToolbar getSwatToolbar(){
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
	
	private SwatTabView getTabView(){
		if(tabView == null){
			tabView = new SwatTabView();
			tabView.setMinimumSize(MINIMUM_SIZE_TAB_PANEL);
			tabView.setPreferredSize(MINIMUM_SIZE_TAB_PANEL);
			tabView.addTabViewListener(this);
		}
		return tabView;
	}
	
	private SwatMenuBar getSwatMenu() {
		if(menuBar == null){
			menuBar = new SwatMenuBar(getTabView(), getTreeView());
		}
		return menuBar;
	}
	
	private SwatTreeView getTreeView(){
		 UIManager.put("Tree.rendererFillBackground", false);
		if(treeView == null){
			treeView = SwatTreeView.getInstance();
			treeView.setPreferredSize(PREFERRED_SIZE_TREEVIEW_PANEL);
			treeView.setMinimumSize(PREFERRED_SIZE_TREEVIEW_PANEL);
			treeView.addTreeViewListener(this);
		}
		return treeView;
	}
	
	private JComponent getMessagePanel(){
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
	
	private JTextArea getConsoleArea(){
		if(console == null){
			console = new JTextArea();
			console.setEditable(false);
		}
		return console;
	}
	
	private JTextArea getErrorArea(){
		if(errors == null){
			errors = new JTextArea();
			errors.setEditable(false);
		}
		return errors;
	}
	
	public void consoleMessage(String message){
		getConsoleArea().append(message);
	}

	public static void main(String[] args) {
		if (args.length == 1) {
			try {
				SwatProperties.getInstance().setWorkingDirectory(args[0]);
				//SwatProperties.getInstance().store();
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		new Workbench();
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
		SwatComponent swatComponent = null;
		if (!getTabView().containsComponent(node)) {
			// add SwatTreeNode to tab and get its swatComponent to make its propertyView
			swatComponent = getTabView().addNewTab(node);
			getPropertiesPanel().removeAll();
			getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
			getPropertiesPanel().validate();
			getPropertiesPanel().repaint();
		}

		//Update Toolbar
		updateToolbar();
	}
	
	@SuppressWarnings("rawtypes")
	private SwatComponent getSwatComponent(SwatTreeNode node) throws ParameterException{
		switch (node.getObjectType()) {
		case LABELING:
			// TODO:
			break;
		case PETRI_NET:
			AbstractGraphicalPN petriNet = (AbstractGraphicalPN) node.getUserObject();
			if(petriNet instanceof GraphicalPTNet){
				return new PTNetEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getFile(petriNet));
			} else if(petriNet instanceof GraphicalCPN){
//				return new CPNEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getFile(petriNet));
			} else if(petriNet instanceof GraphicalIFNet){
//				return new IFNetEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getFile(petriNet));
			}
			break;
		case LOG_FILE:
			// User Object is of type LogFileViewer
			return (LogFileViewer) node.getUserObject();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private String getPathForSwatComponent(SwatComponent component) {
		if (component instanceof PTNetEditor) {
			try {
				return ((PTNetEditor) component).getFileReference().getCanonicalPath();
			} catch (IOException e) {
				MessageDialog.getInstance().addMessage("Cannot get file for AbstractGraphicalPN");
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}

	@Override
	public void activeTabChanged(int index, SwatComponent component) {
		if (index < 0)
			return; //no tabs inside
		// Update Properties Panel & Toolbar according to active tab
		getPropertiesPanel().removeAll();
		getPropertiesPanel().add(new ScrollPane().add(component.getPropertiesView()));
		//pack();
		getPropertiesPanel().repaint();

		updateToolbar();
	}

	@Override
	public void operatingModeChanged() {
		try {
			//Update Properties View & Toolbar
		SwatComponent swatComponent = (SwatComponent) getTabView().getSelectedComponent();
		getPropertiesPanel().removeAll();
		getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
			//pack();
			getPropertiesPanel().repaint();
		//getPropertiesPanel().repaint();
		updateToolbar();
		} catch (NullPointerException e) {
			//Can happens when Arista-Flow analysis is called with no active tabs

		}
	}

	/** check if a PNEditor got activated and update Toolbar if needed **/
	private void updateToolbar() {
		if (getTabView().getSelectedComponent() instanceof PNEditor) {
			try {
			PNEditor editor = (PNEditor) getTabView().getSelectedComponent();
			getSwatToolbar().clear();
			getSwatToolbar().add(editor.getEditorToolbar());
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
