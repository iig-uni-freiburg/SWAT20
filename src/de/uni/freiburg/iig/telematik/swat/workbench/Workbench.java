package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class Workbench extends JFrame implements SwatTreeViewListener {

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
		// Check if there is a path to a working directory.
		if (!checkWorkingDirectory()) {
			// There is no path and it is either not possible to set a path or the user aborted the corresponding dialog.
			System.exit(0);
		}
		// Trigger the loading of swat components
		SwatComponents.getInstance();
		
		setUpGUI();
		
		try {
			SwatState.getInstance().setOperatingMode(Workbench.this, OperatingMode.EDIT_MODE);
		} catch (ParameterException e) {}
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
	
//	private JComponent getContent(){
//		if(content == null){
//			content = new JPanel(new BorderLayout());
//			content.add(getSwatMenu(), BorderLayout.NORTH);
//			content.add(getSplitPane(), BorderLayout.CENTER);
//		}
//		return content;
//	}
//	
//	private JComponent getSplitPane(){
//		if(splitPane == null){
////			String layoutDef = "(COLUMN (ROW weight=1.0 left (COLUMN middle.top middle middle.bottom) right) bottom)";
//			String layoutDef = "(ROW left (COLUMN middle.top middle.bottom) right)";
//			MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
//			splitPane = new MultiSplitPane();
//			splitPane.getMultiSplitLayout().setModel(modelRoot);
//			
//			splitPane.add(getTreeView(), "left");
//			splitPane.add(getPropertiesPanel(), "right");
//			splitPane.add(getTabView(), "middle.top");
//			splitPane.add(getConsole(), "middle.bottom");
////			
////			splitPane = new MultiSplitPane();
////			splitPane.getMultiSplitLayout().setDividerSize(3);
////			
////			// set the model behind the MultiSplitPane
////			// MenuLine, iconLine and bottom only
////			// Leaf menuLine = getLeaf("menuLine");// weight was 0.1
////			Leaf iconLine = new Leaf("iconLine"); // weight was 0.15
////			Leaf bottom = new Leaf("bottomLine");// weight was 0.1
////			Split splitModel = new Split();
////			splitModel.setRowLayout(false);
////			// gui.setChildren(getAsList(menuLine, new Divider(), iconLine, new
////			// Divider(), getCenter(), new Divider(), bottom));
////			splitModel.setChildren(getAsList(iconLine, new Divider(), getCenter(), new Divider(), bottom));
////			splitPane.getMultiSplitLayout().setModel(splitModel);
////			
//////			// get Buttons
//////			SwatToolbar buttons = SwatToolbar.getInstance();
////
//////			// add the buttons from the button-model
//////			msp.add(buttons.getButtonPanel(), "iconLine");
//////
//////			msp.add(new JButton("Bottom Line"), "bottomLine");
//////			msp.add(new JButton("Left Center"), "center.left");
////
//////			// add view and edit buttons
//////			msp.add(buttons.getEditorViewPanel(), "controls");
////
////			add(getDefaultPropertiesPanel(), "properties");
////
////			splitPane.add(getTabView(), "editor");
////
////			splitPane.add(getConsole(), "console");
////			
////			// add the TreeView to the left
////			JScrollPane scrollPaneTreeView = new JScrollPane();
////			scrollPaneTreeView.setViewportView(getTreeView());
////			splitPane.add(scrollPaneTreeView, "center.left");
//		}
//		return splitPane;
//	}
	
//	private Split getCenter() {
//		Leaf center_left = getLeaf("center.left", 0.15);
//		// Leaf center = getLeaf("center", 0.6);
//		Split center = getEditorAndConsole();
//		Split centerSplit = new Split();
//		centerSplit.setRowLayout(true);
//		centerSplit.setChildren(getAsList(center_left, new Divider(), center, new Divider(), getCenter_Right()));
//		centerSplit.setWeight(0.7);
//		return centerSplit;
//	}
	
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
		}
		return tabView;
	}
	
	private SwatMenuBar getSwatMenu(){
		if(menuBar == null){
			menuBar = new SwatMenuBar();
		}
		return menuBar;
	}
	
	private SwatTreeView getTreeView(){
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
		// Or: Let SwatTabView implement SwatTreeChangeListener and add it to
		// SwatTreeView.addTreeViewListener
		getTabView().componentSelected(node);
	}

	@Override
	public void componentActivated(SwatTreeNode node) {
		SwatComponent swatComponent = null;
		try {
			swatComponent = getSwatComponent(node);
		} catch (ParameterException e) {
			JOptionPane.showMessageDialog(this, "Cannot convert selected tree node to swat component", "SWAT Exception", JOptionPane.ERROR_MESSAGE);
		}
		if(!getTabView().containsComponent(node)){
			getTabView().addNewTab(node);
			getPropertiesPanel().removeAll();
			getPropertiesPanel().add(swatComponent.getPropertiesView());
			pack();
			// update currently viewed "file"
			// SwatState.getInstance().setActiveFile(getPathForSwatComponent(swatComponent));
		}
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



}
