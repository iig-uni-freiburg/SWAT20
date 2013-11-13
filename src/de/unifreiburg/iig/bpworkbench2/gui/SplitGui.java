package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.Frame;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;

import de.unifreiburg.iig.bpworkbench2.model.EditAnalyzeModel;
import de.unifreiburg.iig.bpworkbench2.model.files.OpenFileModel;

@SuppressWarnings("serial")
/**
 * Creates JFrame and draws GUI elements (observers) in it. Registers observers
 * to their models
 */
public class SplitGui implements Serializable, Observer {
	private MultiSplitPane msp;
	public JFrame window;
	private TreeView tv;
	private TabView tabView;
	private MenuView menuView; // menu

	private static SplitGui gui = new SplitGui();

	// private SplitGui mySplitGui = new SplitGui();

	public static void main(String[] args) {
		// SplitGui myGui = new SplitGui();
		SplitGui myGui = gui;
		myGui.show();
		OpenFileModel.getInstance().addObserver(myGui);
		OpenFileModel.getInstance().setFolder(new File("/tmp")); // TODO: just
																	// for
																	// testing
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
		window = new JFrame("SWAT 2.0");
		// TODO: Add close handler to save unsaved files
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// set windows size
		window.setSize(600, 380);
		window.setExtendedState(Frame.MAXIMIZED_BOTH);

		// get the gui layout and set the model
		msp = new MultiSplitPane();

		// fill the gui layout, add it to the frame
		addElementsToSplitPane(msp);
		msp.setPreferredSize(msp.getMultiSplitLayout().getModel().getBounds().getSize());
		window.add(msp);
		// change size of dividers
		msp.getMultiSplitLayout().setDividerSize(3);

		// set the menu bar
		window.setJMenuBar(menuView);
		window.setVisible(true);
		window.pack();
	}

	/**
	 * inserts gui elements inside the MultiSplitpane.
	 * 
	 * @param msp
	 */
	private void addElementsToSplitPane(MultiSplitPane msp) {

		// set the model behind the MultiSplitPane
		msp.getMultiSplitLayout().setModel(getMSPModel());
		// get Buttons
		Buttons buttons = Buttons.getInstance();

		// add the buttons from the button-model
		msp.add(buttons.getButtonPanel(), "iconLine");

		msp.add(new JButton("Bottom Line"), "bottomLine");
		msp.add(new JButton("Left Center"), "center.left");

		// add view and edit buttons
		msp.add(buttons.getEditorViewPanel(), "controls");

		// fill the gap (will be properties view)
		msp.add(new JButton("properties"), "properties");

		// add the tab view
		msp.add(tabView, "editor");

		// fill the gap (will be console output)
		JButton console = new JButton("console");
		// console.setPreferredSize(new Dimension(200, 50));
		// console.setPreferredSize(new Dimension(20, 20));
		msp.add(console, "console");
		// msp.add(tabView.getTab(), "console");

		// add the TreeView to the left
		msp.add(new JScrollPane(tv), "center.left");

	}

	/** Generates the MultiSplitPane model **/
	private MultiSplitPane getMultiSplitPane() {
		// get a MultiSplitPane instance
		MultiSplitPane multiSplitPane = new MultiSplitPane();
		// set its model
		multiSplitPane.getMultiSplitLayout().setModel(getMSPModel());

		return multiSplitPane;
	}

	private Split getCenter_Right() {
		// create leafs
		Leaf controls = getLeaf("controls");
		Leaf properties = getLeaf("properties");

		// create splitNode (container) which holds both leafs.
		Split center_right = new Split();
		// Set it to column mode
		center_right.setRowLayout(false);
		// add leafs as children
		center_right.setChildren(getAsList(controls, new Divider(), properties));

		// set the weight of the split to 0.2 (this is not the individual leafs)
		center_right.setWeight(0.15);
		return center_right;
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

	private Split getEditorAndConsole() {
		Leaf editor = getLeaf("editor", 0.8);// was weight=0.7
		Leaf console = getLeaf("console", 0.2);// was weight=0.3
		Split editorSplit = new Split();
		editorSplit.setRowLayout(false);
		editorSplit.setChildren(getAsList(editor, new Divider(), console));
		editorSplit.setWeight(0.7);
		return editorSplit;
	}

	/**
	 * Returns Model for MultiSplitPane
	 * 
	 * @return
	 */
	private Split getMSPModel() {
		// MenuLine, iconLine and bottom only
		// Leaf menuLine = getLeaf("menuLine");// weight was 0.1
		Leaf iconLine = getLeaf("iconLine"); // weight was 0.15
		Leaf bottom = getLeaf("bottomLine");// weight was 0.1
		Split gui = new Split();
		gui.setRowLayout(false);
		// gui.setChildren(getAsList(menuLine, new Divider(), iconLine, new
		// Divider(), getCenter(), new Divider(), bottom));
		gui.setChildren(getAsList(iconLine, new Divider(), getCenter(), new Divider(), bottom));
		return gui;
	}

	/**
	 * wraper to get new MunltiSplitPane leafs with given weighting factor
	 * 
	 * @param name
	 *            The name the new leaf should have
	 * @param weight
	 *            The weight of this leaf to its siblings. Weight is between 0
	 *            and 1 inclusive. Weights of siblings must add up to 1
	 * @return the {@link MultiSplitPane} {@link Leaf} itself
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
	 * the models and registers them as observers: ({@link TreeView},
	 * {@link TabView} , {@link MenuView}).
	 */
	private SplitGui() {
		// generate Views
		tv = TreeView.getTreeView();
		tabView = new TabView();
		Buttons bts = Buttons.getInstance();
		menuView = MenuView.getInstance();

		// add Views to the OpenFileModel's Observer list
		OpenFileModel ofm = OpenFileModel.getInstance();
		ofm.addObserver(tv);
		ofm.addObserver(tabView);
		ofm.addObserver(bts);

		// add Views to the EditOrAnalyze Model
		EditAnalyzeModel eam = EditAnalyzeModel.getModel();
		eam.addObserver(menuView);
		eam.addObserver(bts);
	}

	private List<Node> getAsList(Node... leafs) {
		return Arrays.asList(leafs);
	}

	@Override
	public void update(Observable o, Object arg) {
		// log.log(Level.INFO, "redraw gui");
		// Buttons might have changed

	}

	public void revalidate() {
		// msp.revalidate();
		// window.pack();
		// msp.revalidate();

	}

}
