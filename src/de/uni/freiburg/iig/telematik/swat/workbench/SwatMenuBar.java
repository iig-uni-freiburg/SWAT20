package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
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
import de.uni.freiburg.iig.telematik.swat.analysis.prism.PrismFunctionValidator;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AboutAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.ExportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.PTImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.RenameAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveActiveComponentAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SendExceptionsAsEmail;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SwitchWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.LolaPathChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.PrismPathChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/**
 * MenuBar for SWAT20. Observes the EditAnalyzeModel. Menu entries can be
 * accessed through method {@link #getMenu(String)} with {@link MenuNames} as
 * Input
 * 
 * @author richard
 * 
 */
public class SwatMenuBar extends JMenuBar implements ActionListener, SwatStateListener {

	private static final long serialVersionUID = 4130953102523669184L;

	private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
	private static final String ACTION_COMMAND_ANALYSIS_MODE = "analysisMode";

	private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
	private static int ICON_SIZE = 26;

	JRadioButtonMenuItem editModeButton = null;
	JRadioButtonMenuItem analysisModeButton = null;

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
		add(Box.createHorizontalGlue());
		add(getHelpEntry());

	}

	private JMenu getFileMenu() {
		JMenu fileMenu = new JMenu("File");

		JMenuItem saveAll = getSaveAllEntry();
		JMenuItem save = getSaveEntry();

		JMenuItem exit = getExitEntry();

		JMenuItem importEntry = getImportEntry();

		fileMenu.add(new SwitchWorkingDirectoryAction(UIManager.getIcon("FileView.directoryIcon")));
		fileMenu.add(saveAll);
		fileMenu.add(save);
		fileMenu.add(new DeleteAction("Delete", null));
		fileMenu.add(new RenameAction("Rename", null));
		fileMenu.add(new ExportAction("Export active tab"));
		fileMenu.add(importEntry);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		fileMenu.addSeparator();

		return fileMenu;
	}

	private JMenu getHelpEntry() {
		JMenu helpEntry = new JMenu("Help");
                helpEntry.add(new AboutAction("about..."));
		helpEntry.add(new SendExceptionsAsEmail("Send Bug Report...", null));
		return helpEntry;
	}

	private JMenuItem getImportEntry() {
		JMenuItem importEntry = new JMenuItem("Import");
		importEntry.addActionListener(new PTImportAction());
		return importEntry;
	}

	private JMenuItem getSaveEntry() {
		JMenuItem save = new JMenuItem("Save", UIManager.getIcon("FileView.floppyDriveIcon"));
		//save.setRolloverEnabled(true);
		save.addActionListener(new SaveActiveComponentAction());
		return save;
	}

	private JMenuItem getExitEntry() {
		JMenuItem exit = new JMenuItem("Exit");

		try {
			ImageIcon icon = new ImageIcon(this.getClass().getResource(String.format(iconNameFormat, ICON_SIZE, "close_window", ICON_SIZE)));
			exit.setIcon(icon);
		} catch (Exception e) {
			//do nothing. Could not load icon
		}

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Save all Components
				new SaveAllAction().actionPerformed(new ActionEvent(this, 0, "save"));
				System.exit(0);
			}
		});

		return exit;
	}

	private JMenuItem getSaveAllEntry() {
		JMenuItem saveAll = new JMenuItem("Save all");
		saveAll.addActionListener(new SaveAllAction());
		return saveAll;
	}

	private JMenu getEditMenu() {
		JMenu editMenu = new JMenu("Edit");

		editModeButton = new JRadioButtonMenuItem("Edit Mode");
		editModeButton.setActionCommand(ACTION_COMMAND_EDIT_MODE);
		editModeButton.addActionListener(this);
		editMenu.add(editModeButton);

		analysisModeButton = new JRadioButtonMenuItem("Analyse Mode");
		analysisModeButton.setActionCommand(ACTION_COMMAND_ANALYSIS_MODE);
		analysisModeButton.addActionListener(this);
		editMenu.add(analysisModeButton);

		ButtonGroup editOrAnalyseGroup = new ButtonGroup();
		editOrAnalyseGroup.add(editModeButton);
		editOrAnalyseGroup.add(analysisModeButton);

		return editMenu;
	}

	private JMenuItem getPrismPathSettingEntry() {
		JMenuItem prismPathSetting = new JMenuItem("Set Prism Model Checker Path...");
		prismPathSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrismPathChooser chooser = new PrismPathChooser(SwingUtilities.getWindowAncestor(getParent()));
				String prismPath = chooser.chooseFile();
				if (prismPath != null) {
					try {
						SwatProperties.getInstance().setPrismPath(prismPath);
						if (!PrismFunctionValidator.checkPrism())
							Workbench.errorMessage("Could not verify PRISM executable", null, true);
					} catch (Exception e1) {
						Workbench.errorMessage("Could not set PRISM path", e1, true);
					}
				}
			}
		});
		return prismPathSetting;
	}

	private JMenu getSettingsMenu() {
		JMenu settings = new JMenu("Settings");
		settings.add(getPrismPathSettingEntry());
		settings.add(getLolaPathSettingEntry());

		return settings;
	}

	private JMenuItem getLolaPathSettingEntry() {
		JMenuItem lolaPathSetting = new JMenuItem("Set LoLA Model Checker Path...");
		lolaPathSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LolaPathChooser chooser = new LolaPathChooser(SwingUtilities.getWindowAncestor(getParent()));
				String lolaPath = chooser.chooseFile();
				if (lolaPath != null) {
					try {
						SwatProperties.getInstance().setLolaPath(lolaPath);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getParent()), "Cannot set Prism path.\n Reason: "
								+ e1.getMessage(), "Error while setting LoLA path", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		return lolaPathSetting;
	}

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
		switch (SwatState.getInstance().getOperatingMode()) {
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
