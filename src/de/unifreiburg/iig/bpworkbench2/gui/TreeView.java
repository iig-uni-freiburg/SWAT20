package de.unifreiburg.iig.bpworkbench2.gui;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.unifreiburg.iig.bpworkbench2.logging.BPLog;
import de.unifreiburg.iig.bpworkbench2.model.files.OpenFileModel;
import de.unifreiburg.iig.bpworkbench2.model.files.UserFile;

@SuppressWarnings("serial")
public class TreeView extends JTree implements Observer {

	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	// private JTree tree;
	private Logger log = BPLog.getLogger(SplitGui.class.getName());
	private static TreeView myTreeView = new TreeView();

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof OpenFileModel) {
			log.log(Level.FINE, "the OpenFileModel changed");
			reactOnFileModelChange(arg0, arg1);
		}

	}

	private void reactOnFileModelChange(Observable arg0, Object arg1) {
		// TODO: _DO_ react on index Change. Otherwise the marked index of the
		// tree and the model differ
		if (arg1 != null && arg1.equals("indexChange")) {
			// The active file changed. Do not represent this in the treeView.
			// Otherwise the treeView handler would fire and change the
			// FileModel again
			log.log(Level.FINEST,
					"Ignored change in OpenFileModel. Only active Index changed");
			return;// Do nothing
		}
		OpenFileModel ofm = (OpenFileModel) arg0;
		fileModelToTree(ofm);

		// inform the tree that the undlerlying model has changed
		// ((DefaultTreeModel) tree.getModel()).reload();
		treeModel.reload();
		log.log(Level.INFO, "TreeView updated due to change in "
				+ OpenFileModel.class.getName());
	}

	/**
	 * Creates a JTree out of the Model
	 * 
	 * @return
	 */
	// public JTree getTree() {
	// return this;
	// }

	public static TreeView getTreeView() {
		return myTreeView;
	}

	private TreeView() {

		root = new DefaultMutableTreeNode("Project Gesine");
		treeModel = new DefaultTreeModel(root);
		this.setModel(treeModel);
		this.setShowsRootHandles(true);
		this.setEditable(false);
	}

	/**
	 * Rebuild TreeView out of OpenFileModel
	 * 
	 * @param ofm
	 *            The OpenFileModel
	 */
	private void fileModelToTree(OpenFileModel ofm) {
		root.removeAllChildren();
		for (UserFile uFile : ofm.getFiles()) {
			root.add(new DefaultMutableTreeNode(uFile));
		}
		this.setSelectionInterval(ofm.getOpenFileIndex() - 1,
				ofm.getOpenFileIndex());
		root.setUserObject(ofm.getProject());
		// treeModel.reload();
	}
}
