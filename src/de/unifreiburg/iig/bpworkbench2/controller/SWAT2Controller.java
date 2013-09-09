package de.unifreiburg.iig.bpworkbench2.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import de.unifreiburg.iig.bpworkbench2.gui.MenuView;
import de.unifreiburg.iig.bpworkbench2.gui.MenuView.MenuNames;
import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.gui.TreeView;
import de.unifreiburg.iig.bpworkbench2.gui.buttons;
import de.unifreiburg.iig.bpworkbench2.gui.buttons.ButtonName;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;
import de.unifreiburg.iig.bpworkbench2.model.EditAnalyzeModel;
import de.unifreiburg.iig.bpworkbench2.model.files.OpenFileModel;
import de.unifreiburg.iig.bpworkbench2.model.files.UserFile;

public class SWAT2Controller {
	public int test;

	public static void main(String args[]) {

		// Set look and feel
		setLookandFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		// Generate GUI. The GUI automatically registers with the OpenFileModel
		SplitGui gui = SplitGui.getGui();
		// Show gui
		gui.show();

		// Set a starting point for the project
		String startPath = System.getProperty("user.home");
		OpenFileModel.getInstance().setFolder(new File(startPath));

		// add Observers if needed

		/* ---add Handlers (Controller)--- */

		// add Handler to TreeView
		TreeView.getTreeView().addTreeSelectionListener(new SelectionListener());

		// add Handler to Buttons, etc...
		// get button model
		buttons bts = buttons.getInstance();
		MenuView mv = MenuView.getInstance();

		// Open Button
		bts.getButton(buttons.ButtonName.OPEN_BTN).addActionListener(new OpenListener());
		mv.getMenu(MenuNames.OPEN_MENU).addActionListener(new OpenListener());

		// Save Button
		bts.getButton(ButtonName.SAVE_BTN).addActionListener(new SaveListener());
		mv.getMenu(MenuNames.SAVE_MENU).addActionListener(new SaveListener());

		// New Button
		bts.getButton(ButtonName.NEW_BTN).addActionListener(new NewFileListener());
		mv.getMenu(MenuNames.NEW_MENU).addActionListener(new NewFileListener());

		// Add GUI as Observer of Button change
		// bts.addObserver(gui);

		// RadioButtonHandler
		EditOrAnalyseListener editAnalyseListener = new EditOrAnalyseListener();
		bts.getAnalyseBtn().addItemListener(editAnalyseListener);
		bts.getEditBtn().addItemListener(editAnalyseListener);

		// Menu entry handler
		MenuView menuView = MenuView.getInstance();
		menuView.getAnalyseMenuEntry().addItemListener(editAnalyseListener);
		menuView.getEditMenuEntry().addItemListener(editAnalyseListener);
	}

	private static void setLookandFeel(String LaF) {
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel(UIManager.getAuxiliaryLookAndFeels()[0]);
			UIManager.setLookAndFeel(LaF);
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

	}
}

// Listener and Handlers from here on...

class SaveListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		OpenFileModel.getInstance().save();
	}

}

class OpenListener implements ActionListener {
	Logger log = BPLog.getLogger(SplitGui.class.getName());

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(SplitGui.getGui().window);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// File was chosen
			File file = fc.getSelectedFile();
			// Try to open the directory
			log.log(Level.INFO, "Opening Project: " + file.getName() + ".\n");
			OpenFileModel.getInstance().setFolder(file);
		} else {
			log.log(Level.SEVERE, "Open command cancelled by user.\n");

		}

	}
}

class NewFileListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Logger log = BPLog.getLogger(SplitGui.class.getName());
		OpenFileModel ofm = OpenFileModel.getInstance();
		// Ask user for new FileName
		JFileChooser fc = new JFileChooser(ofm.getProject());
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(SplitGui.getGui().window);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			// file was not correctly chosen
			log.log(Level.SEVERE, "Add file command cancelled by user.\n");
			return;
		}

		// File was chosen correctly
		File file = fc.getSelectedFile();
		// Is file in project directory?
		if (!file.getParent().equals(ofm.getProject().getPath())) {
			JOptionPane.showMessageDialog(SplitGui.getGui().window, file.getParentFile() + " not in project directory: "
					+ ofm.getProject().getPath());
			return;
		}
		// Add File
		log.log(Level.INFO, "Adding file: " + file.getName() + ".\n");
		// Create a new file in the current directory
		UserFile uFile = new UserFile(ofm.getProject().getAbsolutePath() + File.separatorChar + file.getName());
		ofm.addFile(uFile);
	}

}

/**
 * listens for changes in the TreeView and activates the marked object inside
 * the OpenFileModel
 **/
class SelectionListener implements TreeSelectionListener {

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		try {
			// test if same object is marked as before:
			// get the new object that the user selected
			Object newObj = ((DefaultMutableTreeNode) (arg0.getNewLeadSelectionPath().getLastPathComponent())).getUserObject();
			// get the old object that was selected before
			Object oldObj = ((DefaultMutableTreeNode) arg0.getOldLeadSelectionPath().getLastPathComponent()).getUserObject();
			// if both are the same do nothing. The index didn't change.
			// or if there wasn't a marked Object before
			if (newObj == oldObj || oldObj == null || newObj == null) {
				return;
			}
			// search the marked Object inside the OpenFileModel and set the
			// active file accordingly
			// get index of just marked object
			OpenFileModel ofm = OpenFileModel.getInstance();
			// get index of newly selected Object
			int newIndex = ofm.getIndexOf(newObj);
			// set index inside the model
			OpenFileModel.getInstance().setOpenFileIndex(newIndex);

		} catch (NullPointerException e) {

		}

	}
}

/**
 * UpdateListener adds itself automatically on creation to uFile. works with
 * JEditor
 * 
 * @author richard
 * 
 */
class updateListener implements DocumentListener {
	private UserFile uFile;

	public updateListener(UserFile uFile) {
		this.uFile = uFile;
		// add this listener to the uFile's Editor
		// uFile.getEditor().getDocument().addDocumentListener(this);
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		uFile.setUnsavedChanges();
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub

	}
}

/** State Listener for Edit / Analyze mode. MenuBar and Buttons */
class EditOrAnalyseListener implements ItemListener {

	@Override
	public void itemStateChanged(ItemEvent arg0) {

		if (arg0.getSource() instanceof JRadioButton) {
			// One of the radio buttons
			JRadioButton rbtn = (JRadioButton) arg0.getSource();
			reactOnJRadioButton(rbtn);
		}

		if (arg0.getSource() instanceof JRadioButtonMenuItem) {
			// One of the menu entries
			JRadioButtonMenuItem rbtn = (JRadioButtonMenuItem) arg0.getSource();
			reactOnJRadioButtonMenu(rbtn);
		}

	}

	private void reactOnJRadioButtonMenu(JRadioButtonMenuItem rbtn) {
		// react only on state change if button got enabled. Ignore
		// deactivating the Button. Otherwise a click would fire two actions:
		// A state change to active and the state change to inactive
		if (!rbtn.isSelected()) {
			return;
		}
		Logger log = BPLog.getLogger(SplitGui.class.getName());
		// which button was pressed? Check for action command
		if (rbtn.getActionCommand().equals("analysis")) {
			// activate analysis view
			log.log(Level.INFO, "Activating ANALYSE view");
			EditAnalyzeModel.getModel().setEditMode(false);
		} else if (rbtn.getActionCommand().equals("edit")) {
			// activate edit view
			log.log(Level.INFO, "Activating EDIT view");
			EditAnalyzeModel.getModel().setEditMode(true);
		}

	}

	private void reactOnJRadioButton(JRadioButton rbtn) {
		// react only on state change if button got enabled. Ignore
		// deactivating of the Button. Otherwise a click would fire two actions:
		// A state change to active and the state change to inactive
		if (!rbtn.isSelected()) {
			return;
		}
		Logger log = BPLog.getLogger(SplitGui.class.getName());
		// which button was pressed? Check for action command
		if (rbtn.getActionCommand().equals("analysis")) {
			// activate analysis view
			log.log(Level.INFO, "Activating ANALYSE view");
			EditAnalyzeModel.getModel().setEditMode(false);
		} else if (rbtn.getActionCommand().equals("edit")) {
			// activate edit view
			log.log(Level.INFO, "Activating EDIT view");
			EditAnalyzeModel.getModel().setEditMode(true);
		}
	}

}
