package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import de.uni.freiburg.iig.telematik.swat.sciff.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTabViewListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class Workbench extends JFrame implements SwatTreeViewListener, SwatTabViewListener, SwatStateListener {

	private static final long serialVersionUID = 6109154620023481119L;
	
	private static final Dimension PREFERRED_SIZE_WORKBENCH = new Dimension(1024,768);
	private static final Dimension PREFERRED_SIZE_PROPERTIES_PANEL = new Dimension(200,768);
	private static final Dimension PREFERRED_SIZE_TREEVIEW_PANEL = new Dimension(200,768);
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
		setLookAndFeel();
		// Check if there is a path to a working directory.
		if (!checkWorkingDirectory()) {
			// There is no path and it is either not possible to set a path or the user aborted the corresponding dialog.
			System.exit(0);
		}
		// Trigger the loading of swat components
		SwatComponents.getInstance();
		MessageDialog.getInstance().addMessage("Starting workbench...");

		setUpGUI();

		try {
			SwatState.getInstance().setOperatingMode(Workbench.this, OperatingMode.EDIT_MODE);
			SwatState.getInstance().addListener(this);
		} catch (ParameterException e) {}

	}
	
	private void setLookAndFeel() {
		if (System.getProperty("os.name").toLowerCase().contains("nux")) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (Exception e) {
				MessageDialog.getInstance().addMessage("Could not set Look and Feel. Using standard");
			}
		}
	}

	private boolean checkWorkingDirectory(){
		try {
			SwatProperties.getInstance().getWorkingDirectory();
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Internal exception: Cannot load/create swat property file:\n" + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (PropertyException e) {
			// There is no recent working directory
			// -> Let the user choose a path for the working directory
			return chooseWorkingDirectory();
		} catch (ParameterException e) {
			// Value for wokring directory is invalid, possibly due to moved directories
			// -> Remove entry for actual working directory
			try {
				SwatProperties.getInstance().removeWorkingDirectory();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Internal exception: Cannot fix corrupt property entries:\n" + e1.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			// -> Let the user choose a path for the working directory
			return chooseWorkingDirectory();
		}
	}
	
	private boolean chooseWorkingDirectory(){
		String workingDirectory = WorkingDirectoryDialog.showDialog(Workbench.this);
		if(workingDirectory == null)
			return false;
		try {
			SwatProperties.getInstance().setWorkingDirectory(workingDirectory);
			return true;
		} catch (ParameterException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Parameter", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (PropertyException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private void setUpGUI(){
		setTitle("SWAT 2.0");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(PREFERRED_SIZE_WORKBENCH);
		setResizable(true);
		
		setContentPane(getContent2());
		setJMenuBar(getSwatMenu());
		pack();
		setVisible(true);
	}
	
	private JComponent getContent2(){
		if(content == null){
			content = new JPanel(new BorderLayout());
			content.add(getSwatToolbar(), BorderLayout.NORTH);
			content.add(getTreeView(), BorderLayout.WEST);
			content.add(getPropertiesPanel(), BorderLayout.EAST);
			JSplitPane centerPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
			centerPanel.add(getTabView());
			centerPanel.add(getMessagePanel());
			content.add(centerPanel, BorderLayout.CENTER);
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
		}
		return properties;
	}
	
	private SwatTabView getTabView(){
		if(tabView == null){
			tabView = new SwatTabView();
			tabView.setMinimumSize(MINIMUM_SIZE_TAB_PANEL);
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
			treeView = new SwatTreeView();
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
		new Workbench();
	}

	@Override
	public void componentSelected(SwatTreeNode node) {
		//Information from TreeView: New node activated -> relay to TabView
		// Or: Let SwatTabView implement SwatTreeChangeListener and add it to
		getTabView().componentSelected(node);

		//Update Toolbar
		updateToolbar();
	}

	@Override
	public void componentActivated(SwatTreeNode node) {
		SwatComponent swatComponent = null;
		if (!getTabView().containsComponent(node)) {
			System.out.println("tab view does not contain element");
			// add SwatTreeNode to tab and get its swatComponent to make its propertyView
			swatComponent = getTabView().addNewTab(node);
			getPropertiesPanel().removeAll();
			getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
			pack();
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
		// Update Properties Panel according to active tab
		getPropertiesPanel().removeAll();
		getPropertiesPanel().add(new ScrollPane().add(component.getPropertiesView()));
		//pack();
		getPropertiesPanel().repaint();

		//Update Toolbar
		updateToolbar();
	}

	@Override
	public void operatingModeChanged() {
		//Update Properties View
		SwatComponent swatComponent = (SwatComponent) getTabView().getSelectedComponent();
		getPropertiesPanel().removeAll();
		getPropertiesPanel().add(new ScrollPane().add(swatComponent.getPropertiesView()));
		pack();
		//getPropertiesPanel().repaint();

		//Update Toolbar
		updateToolbar();
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
