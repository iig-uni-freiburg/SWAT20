package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;

import de.unifreiburg.iig.bpworkbench2.logging.BPLog;
import de.unifreiburg.iig.bpworkbench2.model.EditAnalyseModel;
import de.unifreiburg.iig.bpworkbench2.model.buttons;
import de.unifreiburg.iig.bpworkbench2.model.files.OpenFileModel;

@SuppressWarnings("serial")
public class SplitGui implements Serializable, Observer {
	private MultiSplitPane msp;
	public JFrame window;
	private JEditorPane editor = new JEditorPane();
	// private JTree tree;
	private TreeView tv;
	private TabView tabView;
	private Logger log = BPLog.getLogger(SplitGui.class.getName()); // Logger
	private MenuView menuView = MenuView.getInstance(); // menu

	private static SplitGui gui = new SplitGui();

	// private SplitGui mySplitGui = new SplitGui();

	public static void main(String[] args) {
		// SplitGui myGui = new SplitGui();
		SplitGui myGui = gui;
		myGui.show();
		OpenFileModel.getInstance().addObserver(myGui);
		OpenFileModel.getInstance().setFolder(new File("/tmp"));

	}

	public static SplitGui getGui() {
		return gui;
	}

	/**
	 * Creates JFrame and draws GUI elements in it. Registers needed observers
	 * to corresponding models
	 */
	public void show() {
		// generate frame for the gui
		window = new JFrame("Gesine Testing");
		// TODO: Add close handler to save unsaved files
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// set windows size
		window.setSize(600, 380);
		window.setVisible(true);

		// get the gui layout and set the model
		msp = new MultiSplitPane();
		msp.getMultiSplitLayout().setModel(getMSPModel());
		// msp = getMultiSplitPane();

		// fill the gui layout
		addElementsToSplitPane(msp);
		window.add(msp);
		// change size of dividers
		msp.getMultiSplitLayout().setDividerSize(3);

		window.setJMenuBar(menuView);

	}

	/**
	 * inserts gui elements inside the MultiSplitpane
	 * 
	 * @param msp
	 */
	private void addElementsToSplitPane(MultiSplitPane msp) {
		// get Buttons
		buttons bts = buttons.getInstance();

		// insert the menu line
		JButton menuLine = new JButton("menuLine with Action Listener");
		menuLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				log.log(Level.INFO, "klick on Button");
				OpenFileModel.getInstance().addFile("Datei ...");
			}
		});
		msp.add(menuLine, "menuLine");
		// add the buttons from the button-model
		msp.add(bts.getButtonPanel(), "iconLine");

		msp.add(new JButton("Bottom Line"), "bottomLine");
		msp.add(new JButton("Left Center"), "center.left");
		// msp.add(new JButton("controls"), "controls");

		// add view or edit buttons
		msp.add(bts.getEditorViewPanel(), "controls");

		msp.add(new JButton("properties"), "properties");

		// add the tab view
		// editor.setPreferredSize(new Dimension(200, 200));
		msp.add(tabView, "editor");

		// msp.add(new JScrollPane(editor), "editor");
		JButton console = new JButton("console");
		// console.setPreferredSize(new Dimension(20, 20));
		msp.add(console, "console");
		// msp.add(tabView.getTab(), "console");

		msp.add(new JScrollPane(tv), "center.left");
		// msp.add(tree, "center.left");
		/*
		 * tree.addTreeSelectionListener(new TreeSelectionListener() {
		 * 
		 * @Override public void valueChanged(TreeSelectionEvent arg0) { try {
		 * editor.setText("Selection path: " +
		 * arg0.getNewLeadSelectionPath().toString() + " OFile-Model: " +
		 * OpenFileModel.getInstance().getIndexOf( ((DefaultMutableTreeNode)
		 * arg0 .getNewLeadSelectionPath() .getLastPathComponent())
		 * .getUserObject())); int newIndex =
		 * OpenFileModel.getInstance().getIndexOf( ((DefaultMutableTreeNode)
		 * arg0 .getNewLeadSelectionPath()
		 * .getLastPathComponent()).getUserObject());
		 * OpenFileModel.getInstance().setOpenFileIndex(newIndex);
		 * 
		 * } catch (NullPointerException e) {
		 * 
		 * }
		 * 
		 * } });
		 */

	}

	/**
	 * Generates the MultiSplitPane model
	 * 
	 * @return
	 */
	private MultiSplitPane getMultiSplitPane() {
		// get a MultiSplitPane instance
		MultiSplitPane multiSplitPane = new MultiSplitPane();
		// set its model
		multiSplitPane.getMultiSplitLayout().setModel(getMSPModel());

		return multiSplitPane;
	}

	private Split getCenter_Right() {
		// create leafs
		Leaf controls = new Leaf("controls");
		Leaf properties = new Leaf("properties");

		// create splitNode (container) which holds both leafs.
		Split center_right = new Split();
		// Set it to column mode
		center_right.setRowLayout(false);
		// add leafs as children
		center_right
				.setChildren(getAsList(controls, new Divider(), properties));

		// set the weight of the split to 0.2 (this is not the individual leafs)
		center_right.setWeight(0.2);
		return center_right;
	}

	private Split getCenter() {
		Leaf center_left = getLeaf("center.left", 0.2);
		// Leaf center = getLeaf("center", 0.6);
		Split center = getEditorAndConsole();
		Split centerSplit = new Split();
		centerSplit.setRowLayout(true);
		centerSplit.setChildren(getAsList(center_left, new Divider(), center,
				new Divider(), getCenter_Right()));
		centerSplit.setWeight(0.65);
		return centerSplit;
	}

	private Split getEditorAndConsole() {
		Leaf editor = getLeaf("editor", 0.7);// was weight=0.7
		Leaf console = getLeaf("console", 0.3);// was weight=0.3
		Split editorSplit = new Split();
		editorSplit.setRowLayout(false);
		editorSplit.setWeight(0.6);
		editorSplit.setChildren(getAsList(editor, new Divider(), console));
		return editorSplit;
	}

	/**
	 * Returns Model for MultiSplitPane
	 * 
	 * @return
	 */
	private Split getMSPModel() {
		// MenuLine, iconLine and bottom only
		Leaf menuLine = new Leaf("menuLine");// weight was 0.1
		Leaf iconLine = getLeaf("iconLine"); // weight was 0.15
		Leaf bottom = getLeaf("bottomLine");// weight was 0.1
		Split gui = new Split();
		gui.setRowLayout(false);
		gui.setChildren(getAsList(menuLine, new Divider(), iconLine,
				new Divider(), getCenter(), new Divider(), bottom));
		return gui;
	}

	/**
	 * wraper to get new MunltiSplitPane leafs
	 * 
	 * @param name
	 *            The name of the new leaf
	 * @param weight
	 *            The weight of this leaf to its siblings. Weight is between 0
	 *            and 1 inclusive
	 * @return the leaf
	 */
	private Leaf getLeaf(String name, double weight) {
		Leaf temp = new Leaf(name);
		temp.setWeight(weight);
		return temp;
	}

	/**
	 * wraper to get new MultiSplitPane leafs
	 * 
	 * @param name
	 *            The name of the new leaf
	 * @return the leaf
	 */
	private Leaf getLeaf(String name) {
		return new Leaf(name);
	}

	/**
	 * Create Gui Object and models. The Gui object creates the needed views on
	 * the models and adds them as observers
	 */
	private SplitGui() {
		// get Views on OpenFileModel
		tv = TreeView.getTreeView();
		// tree = tv.getTree();
		tabView = new TabView();
		// add Views to the OpenFileModel's Observer list
		OpenFileModel.getInstance().addObserver(tv);
		OpenFileModel.getInstance().addObserver(tabView);
		EditAnalyseModel.getModel().addObserver(buttons.getInstance());
		EditAnalyseModel.getModel().addObserver(menuView);
	}

	private void setTreeView(TreeView tv) {
		// TODO: remove TreeView from MultiSplitPane and add new tree
		this.tv = tv;
		// this.tree = tv.getTree();
	}

	private void setTabView(TabView tabView) {
		// TODO: remove old tabView and add new TabView to msp
		this.tabView = tabView;
	}

	public SplitGui getInstance() {
		// return mySplitGui;
		return null;
	}

	private List getAsList(Node... leafs) {
		return Arrays.asList(leafs);
	}

	@Override
	public void update(Observable o, Object arg) {
		// log.log(Level.INFO, "redraw gui");
		// Buttons might have changed

	}

}
