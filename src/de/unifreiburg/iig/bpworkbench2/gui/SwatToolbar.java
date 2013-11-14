package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import de.invation.code.toval.graphic.DisplayFrame;
import de.unifreiburg.iig.bpworkbench2.controller.SWAT2Controller;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;
import de.unifreiburg.iig.bpworkbench2.model.EditAnalyzeModel;

/**
 * Model for Buttons. Holds buttons like "open", "save", ... With
 * {@link #getButtonPanel()} the buttons are available inside a {@link JPanel}.
 * Each button can be accesed through get(enum)
 * 
 * @author richard
 * 
 */
public class SwatToolbar extends JToolBar implements Observer {

	private static final long serialVersionUID = -4279345402764581310L;
	
	private static final String SAVE_BTN_NAME = "saveButton";
	private static final String OPEN_BTN_NAME = "openButon";
	private static final String HELP_BTN_NAME = "helpButon";
	private static final String NEW_BTN_NAME = "newButton";
	private static final String NEW_PROJECT_BTN_NAME = "newProjectButton";
	private static final String NEW_FILE_BTN_NAME = "newFileButton";
	private static final String ANALYSE_BTN_NAME = "analyseButton";
	private static final String EDIT_BTN_NAME = "editButton";
	
	private LinkedHashMap<String, JButton> buttons = new LinkedHashMap<String, JButton>();
	private Logger log = BPLog.getLogger(SplitGui.class.getName());
	private static SwatToolbar myButtons = new SwatToolbar();
	private JRadioButton edit = new JRadioButton("Edit");
	private JRadioButton analysis = new JRadioButton("Analyse");

	private SwatToolbar() {
		
		setFloatable(false);
		setRollover(true);
		// setPreferredSize(new Dimension(100, 50));

		// create JButtons and put them into HashMap -------------------------

		// new Button
		buttons.put(NEW_BTN_NAME, new JButton(UIManager.getIcon("FileView.fileIcon")));
		buttons.get(NEW_BTN_NAME).setToolTipText("Create or view single file");
		add(buttons.get(NEW_BTN_NAME));

		// load Button
		buttons.put(OPEN_BTN_NAME, new JButton(UIManager.getIcon("FileView.directoryIcon")));
		buttons.get(OPEN_BTN_NAME).setToolTipText("Open a directory");
		add(buttons.get(OPEN_BTN_NAME));

		// save Button
		buttons.put(SAVE_BTN_NAME, new JButton(UIManager.getIcon("FileView.floppyDriveIcon")));
		buttons.get(SAVE_BTN_NAME).setToolTipText("Save current file");
		add(buttons.get(SAVE_BTN_NAME));

		// new File Button with logo
		Icon newFileIcon = new ImageIcon(SWAT2Controller.class.getResource("../ressources/addFile.png"));
		buttons.put(NEW_FILE_BTN_NAME, new JButton(newFileIcon));
		buttons.get(NEW_FILE_BTN_NAME).setToolTipText("Create new file");
		add(buttons.get(NEW_FILE_BTN_NAME));

		// create Analysis and Editor Mode RadioButtons -----------------------
		// Listen to key 'e' and 'a'
		edit.setMnemonic(KeyEvent.VK_E);
		analysis.setMnemonic(KeyEvent.VK_A);

		edit.setSelected(true);

		analysis.setActionCommand("analysis");
		edit.setActionCommand("edit");

		// add JRadioButtons to group
		ButtonGroup group = new ButtonGroup();
		group.add(analysis);
		group.add(edit);
	}

	public static SwatToolbar getInstance() {
		return myButtons;
	}

	public JRadioButton getEditBtn() {
		return edit;
	}

	public JRadioButton getAnalyseBtn() {
		return analysis;
	}

	/**
	 * Returns a {@link JPanel} with BOX X_AXIS Layout containing all
	 * {@link JButton}s within the model
	 * 
	 * @return
	 */
	public JPanel getButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		// add Buttons
		for (JButton button : buttons.values()) {
			buttonPanel.add(button);
		}

		validate();
		return buttonPanel;
	}

	/**
	 * Returns the mode selection radio buttons (edit or view)
	 * 
	 * @return
	 */
	public JPanel getEditorViewPanel() {
		JPanel viewPanel = new JPanel();
		viewPanel.setLayout(new GridLayout(1, 0));
		viewPanel.add(edit);
		viewPanel.add(analysis);
		return viewPanel;
	}

	/**
	 * Return {@link JButton} with corresponding identifier (see
	 * {@link ButtonName})
	 * 
	 * @param buttonName
	 *            a String identifiying the button. For enumeration see
	 *            {@link ButtonName}
	 * @return the corresponding JButton
	 */
	public JButton getButton(String buttonName) {
		return buttons.get(buttonName);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// Check if Mode changed:
		if (arg0 instanceof EditAnalyzeModel) {
			EditAnalyzeModel eam = (EditAnalyzeModel) arg0;
			// Set edit and analysis checkbox according to model
			edit.setSelected(eam.isInEditMode());
			analysis.setSelected(!eam.isInEditMode());
		}

	}
	
	public static void main(String[] args) {
		JPanel panel = new JPanel();
		panel.add(new SwatToolbar());
		new DisplayFrame(panel, true);
	}
}
