/**
 * @author richard
 * Opens main window of BPWorkbench.
 * 
 */
package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import de.unifreiburg.iig.bpworkbench2.model.NumberModel;

@SuppressWarnings("serial")
public class MainGUI extends JFrame implements Observer {
	private JPanel treeView;
	private JTree tree;

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO: look for changes and apply/render them

	}

	public static void main(String args[]) {
		MainGUI test = new MainGUI();
		test.setDefaultCloseOperation(EXIT_ON_CLOSE);
		NumberModel model1 = new NumberModel();
		NumberModel model2 = new NumberModel();
		System.out.println("model1 has " + model1.getAmount() + " Money");
		System.out.println("model2 has " + model2.getAmount() + " Money");
		System.out.println("Change Amount of Model1 to 333");
		model1.setAmount(333);
		System.out.println("model1 has " + model1.getAmount() + " Money");
		System.out.println("model2 has " + model2.getAmount() + " Money");
	}

	public MainGUI() {
		getContentPane().setLayout(new BorderLayout());
		JFrame topFrame = new JFrame();
		topFrame.getContentPane().setLayout(new BorderLayout());
		// this.add(topFrame, BorderLayout.NORTH);
		this.setSize(400, 600);
		this.add(addTreeView(), BorderLayout.WEST);
		this.setVisible(true);
		// this.pack();
	}

	public JPanel addTreeView() {
		DefaultMutableTreeNode left = new DefaultMutableTreeNode("Project Tree");

		// Create some entries as tree model
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top Node");
		DefaultMutableTreeNode category1 = new DefaultMutableTreeNode(
				"This is the first category");
		DefaultMutableTreeNode category2 = new DefaultMutableTreeNode(
				"This is the second category");
		DefaultMutableTreeNode category2_1 = new DefaultMutableTreeNode(
				"This is a new category under category 2");
		category2.add(category2_1);
		top.add(category1);
		top.add(category2);
		tree = new JTree(top);
		JScrollPane treeViewScroll = new JScrollPane(tree);
		JPanel result = new JPanel();
		result.add(treeViewScroll);

		return result;
	}

	/**
	 * Register this window with needed observables
	 */
	public void registerObservers() {
		// Model.addObserver(this);
	}

}
