package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.prism.PrismPathChooser;
import de.unifreiburg.iig.bpworkbench2.SwatState;
import de.unifreiburg.iig.bpworkbench2.SwatState.OperatingMode;
import de.unifreiburg.iig.bpworkbench2.SwatStateListener;
import de.unifreiburg.iig.bpworkbench2.controller.SWAT2Controller;
import de.unifreiburg.iig.bpworkbench2.helper.SwatProperties;

/**
 * MenuBar for SWAT20. Observes the EditAnalyzeModel. Menu entries can be
 * accessed through method {@link #getMenu(String)} with {@link MenuNames} as
 * Input
 * 
 * @author richard
 * 
 */
public class SwatMenuBar extends JMenuBar implements ActionListener,SwatStateListener {

	private static final long serialVersionUID = 4130953102523669184L;
	
	private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
	private static final String ACTION_COMMAND_ANALYSIS_MODE = "editMode";
	
	JRadioButtonMenuItem editModeButton = null;
	JRadioButtonMenuItem analysisModeButton = null;
	
//	private LinkedHashMap<String, JMenuItem> menus = new LinkedHashMap<String, JMenuItem>();

	public SwatMenuBar() {
		super();
		try {
			SwatState.getInstance().addListener(this);
		} catch (ParameterException e) {
			// Cannot happen, since this is never null.
		}
		add(getFileMenu());
		add(getEditMenu());
		add(getSettingsMenu());

//		// put all entries into menus-map
//		menus.put(MenuNames.OPEN_MENU, open);
//		menus.put(MenuNames.SAVE_MENU, save);
//		menus.put(MenuNames.NEW_FILE_MENU, addFile);
//		menus.put(MenuNames.EXIT_MENU, exit);

	}
	
	private JMenu getFileMenu(){
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem open = new JMenuItem("Open Dir...", UIManager.getIcon("FileView.directoryIcon"));
		JMenuItem saveAll = new JMenuItem("Save all");
		JMenuItem save = new JMenuItem("Save", UIManager.getIcon("FileView.floppyDriveIcon"));
		JMenuItem addFile = new JMenuItem("Add file...", new ImageIcon(SWAT2Controller.class.getResource("../ressources/addFile.png")));
		JMenuItem exit = new JMenuItem("Exit");

		fileMenu.add(open);
		fileMenu.add(saveAll);
		fileMenu.add(save);
		fileMenu.add(addFile);
		fileMenu.add(exit);
		
		return fileMenu;
	}
	
	private JMenu getEditMenu(){
		JMenu editMenu = new JMenu("Edit");
		
		editModeButton = new JRadioButtonMenuItem("Edit Mode");
		editModeButton.setActionCommand(ACTION_COMMAND_EDIT_MODE);
		editModeButton.addActionListener(this);
		analysisModeButton = new JRadioButtonMenuItem("Analyse Mode");
		analysisModeButton.setActionCommand(ACTION_COMMAND_ANALYSIS_MODE);
		ButtonGroup editOrAnalyseGroup = new ButtonGroup();
		editOrAnalyseGroup.add(editModeButton);
		editOrAnalyseGroup.add(analysisModeButton);
		editModeButton.setSelected(true);
		editModeButton.setActionCommand("edit");
		analysisModeButton.setActionCommand("analysis");
		editMenu.add(editModeButton);
		editMenu.add(analysisModeButton);
		
		return editMenu;
	}
	
	private JMenu getSettingsMenu(){
		JMenu settings = new JMenu("Settings");
		
		JMenuItem prismPathSetting = new JMenuItem("Set Prism Model Checker Path...");
		prismPathSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrismPathChooser chooser = new PrismPathChooser(SwingUtilities.getWindowAncestor(getParent()));
				String prismPath = chooser.chooseFile();
				if(prismPath != null){
					try {
						SwatProperties.getInstance().setPrismPath(prismPath);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(getParent(), "Cannot set Prism path.\n Reason: "+ e1.getMessage(), "Error while setting Prism path", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		settings.add(prismPathSetting);
		
		return settings;
	}

//	@Override
//	public void update(Observable o, Object arg) {
//		if (o instanceof EditAnalyzeModel) {
//			// The Mode (Analyze / Edit) changed
//			EditAnalyzeModel eam = (EditAnalyzeModel) o;
//			// set selected mode
//			editMode.setSelected(eam.isInEditMode());
//			analyseMode.setSelected(!eam.isInEditMode());
//		}
//
//	}

//	public JRadioButtonMenuItem getAnalyseMenuEntry() {
//		return analyseMode;
//	}
//
//	public JRadioButtonMenuItem getEditMenuEntry() {
//		return editMode;
//	}

//	/**
//	 * returns the menu entrie identified by key. Key is one of
//	 * {@link MenuNames}.
//	 **/
//	public JMenuItem getMenu(String key) {
//		return menus.get(key);
//	}

//	/** Statics as identifier for JMenuItem **/
//	public class MenuNames {
//		public static final String SAVE_MENU = "saveMenu";
//		public static final String OPEN_MENU = "openButon";
//		public static final String HELP_MENU = "helpButon";
//		public static final String NEW_MENU = "newMenu";
//		public static final String NEW_PROJECT_MENU = "newProjectMenu";
//		public static final String NEW_FILE_MENU = "newFileMenu";
//		public static final String ANALYSE_MENU = "analyseMenu";
//		public static final String EDIT_MENU = "editMenu";
//		public static final String SAVEALL_MENU = "saveAllMenu";
//		public static final String EXIT_MENU = "exitMenu";
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_COMMAND_ANALYSIS_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatMenuBar.this, OperatingMode.ANALYSIS_MODE);
			} else if (e.getActionCommand().equals(ACTION_COMMAND_EDIT_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatMenuBar.this, OperatingMode.EDIT_MODE);
			}
		} catch (ParameterException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void operatingModeChanged() {
		switch(SwatState.getInstance().getOperatingMode()){
		case ANALYSIS_MODE:
			analysisModeButton.setSelected(true);
			break;
		case EDIT_MODE:
			editModeButton.setSelected(true);
			break;
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setJMenuBar(new SwatMenuBar());
		frame.pack();
		frame.setVisible(true);
	}
}
