package de.unifreiburg.iig.bpworkbench2.gui;

import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

import de.unifreiburg.iig.bpworkbench2.controller.SWAT2Controller;
import de.unifreiburg.iig.bpworkbench2.model.EditAnalyseModel;

public class MenuView extends JMenuBar implements Observer {
	private LinkedHashMap<String, JMenuItem> menus = new LinkedHashMap<String, JMenuItem>();
	private JRadioButtonMenuItem editMode = new JRadioButtonMenuItem(
			"Edit Mode");
	private JRadioButtonMenuItem analyseMode = new JRadioButtonMenuItem(
			"Analyse Mode");
	private static MenuView myMenuView = new MenuView();

	private MenuView() {
		super();
		JMenu file, edit;
		file = new JMenu("File");
		edit = new JMenu("Edit");
		this.add(file);
		this.add(edit);

		// Create menu items for file
		JMenuItem open = new JMenuItem("Open Dir...",
				UIManager.getIcon("FileView.directoryIcon"));
		JMenuItem saveAll = new JMenuItem("Save all");
		JMenuItem save = new JMenuItem("Save",
				UIManager.getIcon("FileView.floppyDriveIcon"));
		JMenuItem addFile = new JMenuItem("Add file...", new ImageIcon(
				SWAT2Controller.class.getResource("../ressources/addFile.png")));

		file.add(open);
		file.add(saveAll);
		file.add(save);
		file.add(addFile);

		// Create menu items for edit
		// Button Group
		ButtonGroup editOrAnalyseGroup = new ButtonGroup();
		editOrAnalyseGroup.add(editMode);
		editOrAnalyseGroup.add(analyseMode);
		editMode.setSelected(true);
		editMode.setActionCommand("edit");
		analyseMode.setActionCommand("analysis");
		edit.add(editMode);
		edit.add(analyseMode);

		this.add(file);
		this.add(edit);

		// put all entries into menus-map
		menus.put(MenuNames.OPEN_MENU, open);
		menus.put(MenuNames.SAVE_MENU, save);
		menus.put(MenuNames.NEW_FILE_MENU, addFile);

	}

	public static MenuView getInstance() {
		return myMenuView;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof EditAnalyseModel) {
			// The Mode changed
			EditAnalyseModel eam = (EditAnalyseModel) o;
			// set selected mode
			editMode.setSelected(eam.isInEditMode());
			analyseMode.setSelected(!eam.isInEditMode());
		}

	}

	public JRadioButtonMenuItem getAnalyseMenuEntry() {
		return analyseMode;
	}

	public JRadioButtonMenuItem getEditMenuEntry() {
		return editMode;
	}

	public JMenuItem getMenu(String key) {
		return menus.get(key);
	}

	public class MenuNames {
		public static final String SAVE_MENU = "saveMenu";
		public static final String OPEN_MENU = "openButon";
		public static final String HELP_MENU = "helpButon";
		public static final String NEW_MENU = "newMenu";
		public static final String NEW_PROJECT_MENU = "newProjectMenu";
		public static final String NEW_FILE_MENU = "newFileMenu";
		public static final String ANALYSE_MENU = "analyseMenu";
		public static final String EDIT_MENU = "editMenu";
		public static final String SAVEALL_MENU = "saveAllMenu";
	}

}
