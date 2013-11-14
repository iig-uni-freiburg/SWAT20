package de.unifreiburg.iig.bpworkbench2;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;

import de.unifreiburg.iig.bpworkbench2.gui.SwatMenuBar;
import de.unifreiburg.iig.bpworkbench2.gui.TabView;
import de.unifreiburg.iig.bpworkbench2.gui.TreeView;

public class Workbench extends JFrame {

	private static final long serialVersionUID = 6109154620023481119L;
	
	private static final Dimension PREFERRED_SIZE = new Dimension(1024,768);
	
	private MultiSplitPane splitPane = null;
	private TreeView treeView = null;
	private TabView tabView = null;
	private SwatMenuBar menuBar = null;
	private JTextArea console = null;
	private JPanel properties = null;
	
	public Workbench(){
		setTitle("SWAT 2.0");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(PREFERRED_SIZE);
		setResizable(true);
		
		setContentPane(getContent2());
		setJMenuBar(getSwatMenu());
		pack();
		setVisible(true);
	}
	
	private JComponent getContent2(){
		JPanel content = new JPanel(new BorderLayout());
		
		content.add(getSwatMenu(), BorderLayout.NORTH);
		content.add(getTreeView(), BorderLayout.PAGE_START);
		content.add(getD)
		
		return content;
	}
	
	private JComponent getContent(){
		if(splitPane == null){
			String layoutDef = "(COLUMN (ROW weight=1.0 left (COLUMN middle.top middle middle.bottom) right) bottom)";
			MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
			
			
			splitPane = new MultiSplitPane();
			splitPane.getMultiSplitLayout().setDividerSize(3);
			
			// set the model behind the MultiSplitPane
			// MenuLine, iconLine and bottom only
			// Leaf menuLine = getLeaf("menuLine");// weight was 0.1
			Leaf iconLine = new Leaf("iconLine"); // weight was 0.15
			Leaf bottom = new Leaf("bottomLine");// weight was 0.1
			Split splitModel = new Split();
			splitModel.setRowLayout(false);
			// gui.setChildren(getAsList(menuLine, new Divider(), iconLine, new
			// Divider(), getCenter(), new Divider(), bottom));
			splitModel.setChildren(getAsList(iconLine, new Divider(), getCenter(), new Divider(), bottom));
			splitPane.getMultiSplitLayout().setModel(splitModel);
			
//			// get Buttons
//			SwatToolbar buttons = SwatToolbar.getInstance();

//			// add the buttons from the button-model
//			msp.add(buttons.getButtonPanel(), "iconLine");
//
//			msp.add(new JButton("Bottom Line"), "bottomLine");
//			msp.add(new JButton("Left Center"), "center.left");

//			// add view and edit buttons
//			msp.add(buttons.getEditorViewPanel(), "controls");

			add(getDefaultPropertiesPanel(), "properties");

			splitPane.add(getTabView(), "editor");

			splitPane.add(getConsole(), "console");
			
			// add the TreeView to the left
			JScrollPane scrollPaneTreeView = new JScrollPane();
			scrollPaneTreeView.setViewportView(getTreeView());
			splitPane.add(scrollPaneTreeView, "center.left");
		}
		return splitPane;
	}
	
	private Split getCenter() {
		Leaf center_left = getLeaf("center.left", 0.15);
		// Leaf center = getLeaf("center", 0.6);
		Split center = getEditorAndConsole();
		Split centerSplit = new Split();
		centerSplit.setRowLayout(true);
		centerSplit.setChildren(getAsList(center_left, new Divider(), center, new Divider(), getCenter_Right()));
		centerSplit.setWeight(0.7);
		return centerSplit;
	}
	
	private JPanel getPropertiesPanel(){
		if(properties == null){
			properties = new JPanel();
			properties.setPreferredSize(preferredSize)
		}
		return properties;
	}
	
	private TabView getTabView(){
		if(tabView == null){
			tabView = new TabView();
		}
		return tabView;
	}
	
	private SwatMenuBar getSwatMenu(){
		if(menuBar == null){
			menuBar = new SwatMenuBar();
		}
		return menuBar;
	}
	
	private TreeView getTreeView(){
		if(treeView == null){
			treeView = TreeView.getTreeView();
		}
		return treeView;
	}
	
	private JComponent getConsole(){
		if(console == null){
			console = new JTextArea();
			console.setEditable(false);
		}
		return console;
	}
	
	public void consoleMessage(String message){
		console.append(message);
	}

}
