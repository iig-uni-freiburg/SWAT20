package de.unifreiburg.iig.bpworkbench2.model;

import java.awt.Graphics;
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
import javax.swing.UIManager;

import de.unifreiburg.iig.bpworkbench2.controller.SWAT2Controller;
import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;

/**
 * Model for Buttons. Holds buttons like "open", "save", ... With
 * {@link #getButtonPanel()} the buttons are available inside a {@link JPanel}.
 * Each button can be accesed through get(enum)
 * 
 * @author richard
 * 
 */
@SuppressWarnings("serial")
public class buttons implements Observer {
	// private JButton[] buttons;
	private LinkedHashMap<String, JButton> buttons = new LinkedHashMap<String, JButton>();
	private JPanel buttonPanel = new JPanel();
	private JPanel viewPanel = new JPanel();
	private Logger log = BPLog.getLogger(SplitGui.class.getName());
	private static buttons myButtons = new buttons();
	private JRadioButton edit = new JRadioButton("Edit");
	private JRadioButton analysis = new JRadioButton("Analyse");

	private buttons() {
		// create Buttons and put them into buttons

		// new Button
		buttons.put(ButtonName.NEW_BTN,
				new JButton(UIManager.getIcon("FileView.fileIcon")));
		buttons.get(ButtonName.NEW_BTN).setToolTipText(
				"Create or view single file");

		// load Button
		buttons.put(ButtonName.OPEN_BTN,
				new JButton(UIManager.getIcon("FileView.directoryIcon")));
		buttons.get(ButtonName.OPEN_BTN).setToolTipText("Open a directory");

		// save Button
		buttons.put(ButtonName.SAVE_BTN,
				new JButton(UIManager.getIcon("FileView.floppyDriveIcon")));
		buttons.get(ButtonName.SAVE_BTN).setToolTipText("Save current file");

		// new File Button with logo
		Icon newFileIcon = new ImageIcon(
				SWAT2Controller.class.getResource("../ressources/addFile.png"));
		buttons.put(ButtonName.NEW_FILE_BTN, new JButton(newFileIcon));
		// buttons.get(ButtonName.NEW_FILE_BTN).setToolTipText("create new file");

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		// add Buttons
		for (JButton button : buttons.values()) {
			buttonPanel.add(button);
		}

		// create Analysis and Editor Mode RadioButtons.
		// Listen to key 'e' and 'a'
		edit.setMnemonic(KeyEvent.VK_E);
		edit.setMnemonic(KeyEvent.VK_A);

		edit.setSelected(true);

		analysis.setActionCommand("analysis");
		edit.setActionCommand("edit");

		// add to group
		ButtonGroup group = new ButtonGroup();
		group.add(analysis);
		group.add(edit);

		// add group to viewPanel
		viewPanel.setLayout(new GridLayout(1, 0));
		viewPanel.add(edit);
		viewPanel.add(analysis);
	}

	public static buttons getInstance() {
		return myButtons;
	}

	public JRadioButton getEditBtn() {
		return edit;
	}

	public JRadioButton getAnalyseBtn() {
		return analysis;
	}

	/**
	 * If buttons should extend JComponent this will be handy...
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		buttons.get("saveButton").paint(g);

	}

	/**
	 * Returns a {@link JPanel} with BOX X_AXIS Layout containing all
	 * {@link JButton}s within the model
	 * 
	 * @return
	 */
	public JPanel getButtonPanel() {
		return buttonPanel;
	}

	/**
	 * Returns the mode selection radio buttons (edit or view)
	 * 
	 * @return
	 */
	public JPanel getEditorViewPanel() {
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

	/**
	 * Contains JButton Names (SAVE_BTN, OPEN_BTN...)
	 * 
	 * @author richard
	 * 
	 */
	public class ButtonName {
		public static final String SAVE_BTN = "saveButton";
		public static final String OPEN_BTN = "openButon";
		public static final String HELP_BTN = "helpButon";
		public static final String NEW_BTN = "newButton";
		public static final String NEW_PROJECT_BTN = "newProjectButton";
		public static final String NEW_FILE_BTN = "newFileButton";
		public static final String ANALYSE_BTN = "analyseButton";
		public static final String EDIT_BTN = "editButton";
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// Check if Mode changed:
		if (arg0 instanceof EditAnalyseModel) {
			EditAnalyseModel eam = (EditAnalyseModel) arg0;
			edit.setSelected(eam.isInEditMode());
			analysis.setSelected(!eam.isInEditMode());
		}

	}
}
