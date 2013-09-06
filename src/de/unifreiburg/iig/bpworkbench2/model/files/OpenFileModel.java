package de.unifreiburg.iig.bpworkbench2.model.files;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;

/**
 * This model stores a list of all open files and keeps track of unsaved changes
 * for each. To get the instance use {@link #getInstance()}. <br>
 * It offers a {@link DefaultTreeModel} which will update on all changes and can
 * be used with a JTree. Get it with {@link #getTreeModel()}
 * 
 * @author richard
 * 
 */
public class OpenFileModel extends Observable {
	private ArrayList<UserFile> openFiles = new ArrayList<UserFile>();
	private int currentlyViewedFile = 0;
	private static OpenFileModel myOpenFileModel = new OpenFileModel();
	// Static so that always the same JTree is meant
	// private DefaultMutableTreeNode root;
	// private DefaultTreeModel dtm;
	// private JTree tree;

	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("Project");
	private DefaultTreeModel dtm = new DefaultTreeModel(root);
	private JTree tree = new JTree(dtm);
	private Logger log = BPLog.getLogger(SplitGui.class.getName());
	private UserFile projectName;

	private OpenFileModel() {
		// root = new DefaultMutableTreeNode("Project");
		// dtm = new DefaultTreeModel(root);
		// tree = new JTree(dtm);
		// tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);

	}

	/**
	 * Get the model's instance
	 * 
	 * @return The model's singleton instance
	 */
	public static OpenFileModel getInstance() {
		return myOpenFileModel;
		// Or maybe make more than one FileModel possible? For having different
		// open projects (Or one FileModel represents all open Projects)
	}

	public void addFile(File file) {
		openFiles.add(new UserFile(file));
		update();
	}

	private void addFileSilently(File file) {
		openFiles.add(new UserFile(file));
	}

	public void addFile(String file) {
		openFiles.add(new UserFile(file));
		update();

		log.log(Level.INFO, "Now: " + getSize() + " Elements in FileModel");
	}

	public void addFile(UserFile file) {
		openFiles.add(file);
		update();

	}

	public void removeFile(File file) {
		openFiles.remove(file);
		update();
	}

	public UserFile getFile(int i) {
		return openFiles.get(i);
	}

	public UserFile getOpenFile() {

		return openFiles.get(currentlyViewedFile);

	}

	public int getOpenFileIndex() {
		return currentlyViewedFile;
	}

	public int getIndexOf(Object o) {
		for (int i = 0; i < openFiles.size(); i++) {
			if (openFiles.get(i) == o) {
				return i;
			}
		}

		return -1;
	}

	public UserFile getProject() {
		return projectName;
	}

	public void setOpenFileIndex(int i) {
		setOpenFileIndex(i, true);
	}

	public void setOpenFileIndex(int i, boolean notify) {
		// System.out.print("markiert: " + i);
		if (i == currentlyViewedFile || i == -1) {
			// noting changed really
			return;
		} else {
			log.log(Level.FINE, "setOpenFileIndex to: " + i);
			currentlyViewedFile = i;
			if (notify) {
				log.log(Level.FINE,
						"Notify Observers about open File Index change");
				setChanged();
				notifyObservers("indexChange");
			}
		}
	}

	public void setOpenFileIndex(UserFile uFile) {
		int newIndex = getIndexOf(uFile);
		setOpenFileIndex(newIndex);
	}

	public ArrayList<UserFile> getFiles() {
		return openFiles;
	}

	public int getSize() {
		return openFiles.size();
	}

	public JTree getTree() {
		return tree;
	}

	public void manipulateTreeModel(DefaultTreeModel model) {

	}

	/**
	 * Set the current open file as "has unsaved changes" and notify observers
	 */
	public void setUnsavedChange() {
		openFiles.get(currentlyViewedFile).setUnsavedChanges();
		update("unsavedChange");
	}

	/**
	 * Set the current open file as "has unsaved changes"
	 * 
	 * @param quiet
	 *            if true: Do not update observers. <br>
	 *            otherwise: Update observers
	 */
	public void setUnsavedChanges(boolean quiet) {
		if (quiet) {
			// only update the currently active file
			openFiles.get(currentlyViewedFile).setUnsavedChanges();
		} else {
			setUnsavedChange();
		}
	}

	private void update(String string) {
		setChanged();
		notifyObservers(string);

	}

	/**
	 * Inform observers and also represent internal JTree represantation
	 */
	private void update() {
		log.log(Level.INFO, "OpenFileModel changed. Notifiying observers");
		// store current selection

		// currentlyViewedFile = openFiles.indexOf(tree
		// .getLastSelectedPathComponent());

		// ((DefaultMutableTreeNode) dtm.getRoot()).removeAllChildren();
		// root.removeAllChildren();
		// for (UserFile uFile : openFiles) {
		// ((DefaultMutableTreeNode) dtm.getRoot())
		// .add(new DefaultMutableTreeNode(uFile));
		// root.add(new DefaultMutableTreeNode(uFile));
		// }
		// tree.setSelectionRow(currentlyViewedFile);
		// dtm.reload();
		setChanged();
		notifyObservers();
	}

	/**
	 * Set which folder the model should represent.
	 */
	public void setFolder(File dir) {
		log.log(Level.INFO, "Opening Directory " + dir.toString());
		// check if file is a directory
		if (!dir.isDirectory()) {
			log.log(Level.SEVERE, dir.toString() + "is not a directory");
			JOptionPane.showMessageDialog(null, dir.toString()
					+ " is no a (valid) directory", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// dir is a folder. Proceed
		// clear old model and parse the directory
		openFiles.clear();
		parseList(dir);
	}

	public void clear() {
		// ask for unsaved changes?
		openFiles.clear();
		update();
	}

	/**
	 * 
	 * @return true iff currently viewed file has unsaved changes
	 */
	public boolean hasUnsavedChange() {
		return openFiles.get(currentlyViewedFile).hasUnsavedChanges();
	}

	private void parseList(File dir) {
		// create fileList out of *.txt files
		File[] filelist = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String fileName) {
				return (fileName.endsWith(".txt"));
			}
		});
		for (File file : filelist) {
			addFileSilently(file);
		}
		projectName = new UserFile(dir);
		update();

	}

	public void setUnsavedChange(UserFile userFile) {
		// For later use?
	}

	/**
	 * Save currently viewed file
	 */
	public void save() {
		try {
			openFiles.get(currentlyViewedFile).save();
			openFiles.get(currentlyViewedFile).setSaved();
			update();
		} catch (IndexOutOfBoundsException e) {
			// No files open, but save button has fired
			log.log(Level.FINER, "Could not save antything: no files are open");
		}

	}

	/**
	 * Enum of possible changes
	 * 
	 * @author richard
	 * 
	 */
	public enum actions {
		// Express the individual actions as an enum. Another possible idea
		// would be to store every change in an individual object, aka implement
		// the command pattern
		CUR_INDEX_CHANGE(1), FILE_NAME_CHANGE(10), SAVED_CURRENT(20), SAVED_ALL(
				30);
		public int value;

		private actions(int value) {
			this.value = value;
		}

	}

}

// Ab hier nichts mehr wichtiges

@SuppressWarnings("serial")
class OpenFile implements UserOpenFile {

	File openFile = new File("");// Stores File
	boolean hasUnsavedChanges = false;// are there unsaved changes

	/**
	 * Return name of file. Append a "*" if this File has unsaved changes
	 */
	public String toString() {
		return hasUnsavedChanges ? "*" + openFile.toString() : openFile
				.toString();
	}

	public OpenFile(String fileName) {
		openFile = new File(fileName);
	}

	/**
	 * public void setUnsaved() { OpenFileModel.getInstance().setChanged(); }
	 */
	public OpenFile(File file, boolean hasUnsavedChanges) {
		openFile = file;
		this.hasUnsavedChanges = hasUnsavedChanges;
	}

	public OpenFile(File file) {
		openFile = file;
		hasUnsavedChanges = false;
	}
}

/**
 * Class to store OpenFile Objects. Can be nested, as both implement
 * {@link #UserFile}
 * 
 * @author richard
 * 
 */
@SuppressWarnings("serial")
class OpenProject implements UserOpenFile {

	ArrayList<OpenFile> openFiles = new ArrayList<OpenFile>();
}

/**
 * Interface to make it possible to nest OpenFile and OpenProjects, if later
 * needed.
 * 
 * @author richard
 * 
 */
interface UserOpenFile extends Serializable {

}
