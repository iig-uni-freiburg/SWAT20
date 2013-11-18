package de.uni.freiburg.iig.telematik.swat.workbench;

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
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SwitchWorkingDirectoryAction;
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

	}

	private JMenu getFileMenu() {
		JMenu fileMenu = new JMenu("File");

		JMenuItem open = new JMenuItem("Switch working directory", UIManager.getIcon("FileView.directoryIcon"));
		open.setAction(new SwitchWorkingDirectoryAction());

		// TODO: Add appropriate actions.
		JMenuItem saveAll = getSaveAllEntry();
		JMenuItem save = new JMenuItem("Save", UIManager.getIcon("FileView.floppyDriveIcon"));
		JMenuItem addFile = new JMenuItem("Add file...", new ImageIcon(getClass().getResource("../resources/addFile.png")));

		JMenuItem exit = new JMenuItem("Exit");

		fileMenu.add(open);
		fileMenu.add(saveAll);
		fileMenu.add(save);
		fileMenu.add(addFile);
		fileMenu.add(exit);

		return fileMenu;
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

	private JMenu getSettingsMenu() {
		JMenu settings = new JMenu("Settings");

		JMenuItem prismPathSetting = new JMenuItem("Set Prism Model Checker Path...");
		prismPathSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrismPathChooser chooser = new PrismPathChooser(SwingUtilities.getWindowAncestor(getParent()));
				String prismPath = chooser.chooseFile();
				if (prismPath != null) {
					try {
						SwatProperties.getInstance().setPrismPath(prismPath);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getParent()), "Cannot set Prism path.\n Reason: "
								+ e1.getMessage(), "Error while setting Prism path", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		settings.add(prismPathSetting);

		return settings;
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
