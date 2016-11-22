package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.swat.analysis.prism.PrismFunctionValidator;
import de.uni.freiburg.iig.telematik.swat.analysis.prism.searcher.PrismSearcher;
import de.uni.freiburg.iig.telematik.swat.analysis.prism.searcher.PrismSearcherFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatNewNetToolbar.ToolbarNewNetButtonType;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AboutAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.EditTimeContextAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.ExportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.NewNetAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.PTImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.RenameAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveActiveComponentAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SelectResourceContextEditAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SendExceptionsAsEmail;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SetCostAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SetDeadlineAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SetDefaultResourceContextAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SetDefaultTimeContextAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SwitchWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.setRecurringAction;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.LolaPathChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.PrismPathChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;

/**
 * MenuBar for SWAT20. Observes the EditAnalyzeModel. Menu entries can be
 * accessed through method {@link #getMenu(String)} with {@link MenuNames} as
 * Input
 * 
 * @author richard
 * 
 */
public class SwatMenuBar extends JMenuBar implements ActionListener, SwatStateListener {
	
	int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    int commandAndShift = commandKey | InputEvent.SHIFT_DOWN_MASK;
    int altKey = InputEvent.ALT_DOWN_MASK;

	private static final long serialVersionUID = 4130953102523669184L;

	private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
	private static final String ACTION_COMMAND_ANALYSIS_MODE = "analysisMode";

	private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
	private static int ICON_SIZE = 26;

	JRadioButtonMenuItem editModeButton = null;
	JRadioButtonMenuItem analysisModeButton = null;
	JCheckBoxMenuItem recurrent;
	
	

	public SwatMenuBar() {
		super();
		try {
			SwatState.getInstance().addListener(this);
		} catch (ParameterException e) {
			// Cannot happen, since this is never null.
		}

		add(getFileMenu());
		add(getEditMenu());
		add(getSimulationMenu());
		add(getSettingsMenu());
		add(Box.createHorizontalGlue());
		add(getHelpEntry());

	}

	private JMenu getSimulationMenu() {
		JMenu simulationMenu = new JMenu("Simulation");
		simulationMenu.add(new SelectResourceContextEditAction());
		simulationMenu.add(new SetDefaultResourceContextAction());
		simulationMenu.add(new EditTimeContextAction());
		simulationMenu.add(new SetDefaultTimeContextAction());
		simulationMenu.add(new SetDeadlineAction());
		simulationMenu.add(new SetCostAction());
		simulationMenu.add(getRecurrentToggle());
		return simulationMenu;
	}
	
	private JCheckBoxMenuItem getRecurrentToggle(){
		recurrent = new JCheckBoxMenuItem("recurrent");
		recurrent.setAction(new setRecurringAction());
		SwatTabView.getInstance().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Component c = ((JTabbedPane) e.getSource()).getSelectedComponent();
				if(c instanceof RTPNEditorComponent){
					recurrent.setEnabled(true);
					TimedNet net = ((RTPNEditorComponent)c).getNetContainer().getPetriNet();
					recurrent.setSelected(net.isRecurring());
				} else {
					recurrent.setSelected(false);
					recurrent.setEnabled(false);
				}
				
			}
		});
		return recurrent;
	}

	private JMenu getFileMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveAll = getSaveAllEntry();
		JMenuItem save = getSaveEntry();
		JMenuItem quit = getExitEntry();
		JMenuItem importEntry = getImportEntry();
		JMenuItem newMenu = getNewMenu();		
		
		saveAll.setAccelerator(KeyStroke.getKeyStroke('S', commandAndShift));
		save.setAccelerator(KeyStroke.getKeyStroke('S', commandKey));
		importEntry.setAccelerator(KeyStroke.getKeyStroke('I', commandKey));
		quit.setAccelerator(KeyStroke.getKeyStroke('Q', commandKey));

		fileMenu.add(newMenu);
		fileMenu.add(new SwitchWorkingDirectoryAction(UIManager.getIcon("FileView.directoryIcon")));
		fileMenu.add(saveAll);
		fileMenu.add(save);
		fileMenu.add(new DeleteAction("Delete", null));
		fileMenu.add(new RenameAction("Rename", null));
		fileMenu.add(new ExportAction("Export active tab"));
		fileMenu.add(importEntry);
		fileMenu.addSeparator();
		fileMenu.add(quit);
		fileMenu.addSeparator();

		return fileMenu;
	}

	
	private JMenuItem getNewPTNEntry() {
		JMenuItem ptEntry = new JMenuItem("PT-Net");
		ptEntry.setAccelerator(KeyStroke.getKeyStroke('N', commandKey));
		ptEntry.addActionListener(new NewNetAction(ToolbarNewNetButtonType.NEW_PT));
		return ptEntry;
	}
	
	private JMenuItem getNewCPNEntry() {
		JMenuItem cpEntry = new JMenuItem("CP-Net");
		cpEntry.setAccelerator(KeyStroke.getKeyStroke('N', commandAndShift));
		cpEntry.addActionListener(new NewNetAction(ToolbarNewNetButtonType.NEW_CPN));
		return cpEntry;
	}
	
	private JMenuItem getNewIFNEntry() {
		JMenuItem ifEntry = new JMenuItem("IF-Net");
		ifEntry.setAccelerator(KeyStroke.getKeyStroke('N', altKey));
		ifEntry.addActionListener(new NewNetAction(ToolbarNewNetButtonType.NEW_IF));
		return ifEntry;
	}
	
	private JMenu getNewMenu() {
		JMenu newMenu = new JMenu("New");
		newMenu.add(getNewPTNEntry());
		newMenu.add(getNewCPNEntry());
		newMenu.add(getNewIFNEntry());
		return newMenu;
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
		JMenuItem exit = new JMenuItem("Quit");

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
		editModeButton.setAccelerator(KeyStroke.getKeyStroke('E', altKey));
		editMenu.add(editModeButton);
		

		analysisModeButton = new JRadioButtonMenuItem("Analyse Mode");
		analysisModeButton.setActionCommand(ACTION_COMMAND_ANALYSIS_MODE);
		analysisModeButton.addActionListener(this);
		analysisModeButton.setAccelerator(KeyStroke.getKeyStroke('A', altKey));
		editMenu.add(analysisModeButton);

		ButtonGroup editOrAnalyseGroup = new ButtonGroup();
		editOrAnalyseGroup.add(editModeButton);
		editOrAnalyseGroup.add(analysisModeButton);

		return editMenu;
	}

	private JMenuItem getPrismPathSettingEntry() {
		JMenuItem prismPathSetting = new JMenuItem("Set Prism Model Checker Path...");
		prismPathSetting.setAccelerator(KeyStroke.getKeyStroke('J', commandKey));
		prismPathSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrismPathChooser chooser = new PrismPathChooser(SwingUtilities.getWindowAncestor(getParent()));
				String prismPath = chooser.chooseFile();
				if (prismPath != null) {
					try {
						PrismSearcher searcher = PrismSearcherFactory.getPrismSearcher();
						File fullPath=searcher.validatePrismPath(prismPath);
						SwatProperties.getInstance().setPrismPath(fullPath.getAbsolutePath());
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
		settings.add(getProperties());

		return settings;
	}

	private JMenuItem getProperties() {
		JMenuItem propertiesSettingEntry = new JMenuItem("Properties");
		propertiesSettingEntry.setAccelerator(KeyStroke.getKeyStroke('M', commandKey));
		return propertiesSettingEntry;
		
	}
	
	private JMenuItem getLolaPathSettingEntry() {
		JMenuItem lolaPathSetting = new JMenuItem("Set LoLA Model Checker Path...");
		lolaPathSetting.setAccelerator(KeyStroke.getKeyStroke('J', commandAndShift));
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
