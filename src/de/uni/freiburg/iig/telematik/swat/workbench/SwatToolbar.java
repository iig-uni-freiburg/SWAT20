package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.graphic.PNParserDialog;
import de.uni.freiburg.iig.telematik.swat.editor.menu.WrapLayout;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveActiveComponentAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SwitchWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/**
 * Model for Buttons. Holds buttons like "open", "save", ... With
 * {@link #getButtonPanel()} the buttons are available inside a {@link JPanel}.
 * Each button can be accesed through get(enum)
 * 
 * @author richard
 * 
 */
public class SwatToolbar extends JToolBar implements ActionListener, SwatStateListener {

	private static final long serialVersionUID = -4279345402764581310L;
	
	private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
	private static final String ACTION_COMMAND_ANALYSIS_MODE = "analysisMode";
	private static int ICON_SIZE = 32;
	private static final int ICON_SPACING = 5;
	
	private JRadioButton rdbtnEdit = null;
	private JRadioButton rdbtnAnalysis = null;

	private JButton openButton = null;

	private SwatTabView tabView = null;
	private SwatTreeView treeView = null;

	private List<Component> standardItems = new LinkedList<Component>();

	public SwatToolbar(SwatTabView tabView, SwatTreeView treeView) {
		this.tabView = tabView;
		this.treeView = treeView;

		setFloatable(false);
		setRollover(true);

		//HACK!: To show PNEditor-Icons (they seem to be bigger) 
		//setPreferredSize(new Dimension(200, ICON_SIZE + 30));
		
		setLayout(new WrapLayout(3));

		createButtons();
		addStandardButtons();

		try {
			SwatState.getInstance().addListener(this);
		} catch (ParameterException e) {
			// Cannot happen, since this is never null.
		}

		// try to get ICONSize
		try {
			ICON_SIZE = SwatProperties.getInstance().getIconSize().getSize();
		} catch (Exception e) {
			// Cannot read property. Ignore and stay with default value (32)
		}

	}

	private void addStandardButtons() {
		for (Component component : standardItems) {
			add(component);
		}
		addSeparator();
	}

	/** Put Buttons into linkedList {@link #standardItems} for later use **/
	private void createButtons() {
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.SAVE));
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.SAVE_ALL));
		standardItems.add(getSwitchworkingDirectoryButton());
		standardItems.add(getNewPTNetButton());
		standardItems.add(getNewCPNButton());
		standardItems.add(getNewIFNetButton());
		standardItems.add(getImportButon());
		standardItems.add(getEditRadioButton());
		standardItems.add(getAnalysisRadioButton());
		
		ButtonGroup group = new ButtonGroup();
		group.add(getAnalysisRadioButton());
		group.add(getEditRadioButton());
		getEditRadioButton().setSelected(true);
	}
	
	private Component getImportButon() {
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.IMPORT);
		return newButton;
	}

	private JRadioButton getAnalysisRadioButton(){
		if(rdbtnAnalysis == null){
			rdbtnAnalysis = new JRadioButton("Analyse");
			rdbtnAnalysis.setMnemonic(KeyEvent.VK_A);
			rdbtnAnalysis.setActionCommand(ACTION_COMMAND_ANALYSIS_MODE);
			rdbtnAnalysis.addActionListener(this);
		}
		return rdbtnAnalysis;
	}
	
	private JRadioButton getEditRadioButton(){
		if(rdbtnEdit == null){
			rdbtnEdit = new JRadioButton("Edit");
			rdbtnEdit.setMnemonic(KeyEvent.VK_E);
			rdbtnEdit.setActionCommand(ACTION_COMMAND_EDIT_MODE);
			rdbtnEdit.addActionListener(this);
		}
		return rdbtnEdit;
	}
	
	private JButton getSwitchworkingDirectoryButton(){
		if (openButton == null)
			openButton = new SwatToolbarButton(ToolbarButtonType.SWITCH_DIRECTORY);
		// newButton.addActionListener(new
		// OpenWorkingDirectoryAction(SwingUtilities.getWindowAncestor(this)));
		return openButton;
	}
	
	
	private JButton getNewPTNetButton(){
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW_PT);
		//newButton.addActionListener(new createNewAction(ToolbarButtonType.NEW_PT));

		return newButton;
	}
	
	private JButton getNewCPNButton(){
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW_CPN);
		//newButton.addActionListener(new createNewAction(ToolbarButtonType.NEW_CPN));
		return newButton;
	}
	
	private JButton getNewIFNetButton(){
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW_IF);
		return newButton;
	}
	
	private String requestFileName(String message, String title){
		return new FileNameDialog(SwingUtilities.getWindowAncestor(getParent()), message, title, false).requestInput();
	}
	
	private File getAbsolutePathToWorkingDir(String name) throws PropertyException, ParameterException, IOException {
		File file = new File(SwatProperties.getInstance().getWorkingDirectory(), name);
		if (file.exists())
			throw new ParameterException("File already exists");
		//TODO: Validate, test if SWATComponent already contains net with same name... etc?
		return file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_COMMAND_ANALYSIS_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatToolbar.this, OperatingMode.ANALYSIS_MODE);
			} else if (e.getActionCommand().equals(ACTION_COMMAND_EDIT_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatToolbar.this, OperatingMode.EDIT_MODE);
			}
		} catch (ParameterException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JPanel panel = new JPanel();
		panel.add(new SwatToolbar(new SwatTabView(), new SwatTreeView()));
		new DisplayFrame(panel, true);
	}

	@Override
	public void operatingModeChanged() {
		switch(SwatState.getInstance().getOperatingMode()){
		case ANALYSIS_MODE:
			getAnalysisRadioButton().setSelected(true);
			break;
		case EDIT_MODE:
			getEditRadioButton().setSelected(true);
			break;
		}
		repaint();
	}

	public void addOpenActionListener(ActionListener listener) {
		getSwitchworkingDirectoryButton().addActionListener(listener);
	}

	public void clear() {
		this.removeAll();
		//createButtons();
		addStandardButtons();
	}


	
	private final class ImportAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net = PNParserDialog.showPetriNetDialog(SwingUtilities
					.getWindowAncestor(SwatToolbar.this));
			if (net == null)
				return;
			String fileName = requestFileName("Name for imported net?", "New name?");
			try {
				File file = getAbsolutePathToWorkingDir(fileName);
				SwatComponents.getInstance().putIntoSwatComponent(net, file);
				treeView.removeAndUpdateSwatComponents();
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private class SwatToolbarButton extends JButton{

		private static final long serialVersionUID = 9184814296174960480L;
		private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
		
		public SwatToolbarButton(ToolbarButtonType type){
			super(new ImageIcon(SwatToolbar.this.getClass().getResource(
					String.format(iconNameFormat, ICON_SIZE, type.toString().toLowerCase(), ICON_SIZE))));
			setBorder(BorderFactory.createEmptyBorder(0, ICON_SPACING, 0, ICON_SPACING));
			setBorderPainted(false);
			switch(type){
			case IMPORT:
				setToolTipText("Import PT-Net from filesystem");
				addActionListener(new ImportAction());
				break;
			case NEW:
				break;
			case OPEN:
				break;
			case SAVE:
				addActionListener(new SaveActiveComponentAction(tabView));
				break;
			case SAVE_ALL:
				addActionListener(new SaveAllAction());
				break;
			case SWITCH_DIRECTORY:
				addActionListener(new SwitchWorkingDirectoryAction(treeView, tabView));
				break;
			case NEW_CPN:
				setToolTipText("Create new CPnet");
				addActionListener(new createNewAction(type));
				break;
			case NEW_PT:
				setToolTipText("Create new PTnet");
				addActionListener(new createNewAction(type));
				break;
			case NEW_IF:
				setToolTipText("Create new IFnet");
				addActionListener(new createNewAction(type));
				break;
			}
		}
		
	}
	
	private enum ToolbarButtonType {
		NEW, SAVE, SAVE_ALL, OPEN, IMPORT, SWITCH_DIRECTORY, NEW_PT, NEW_CPN, NEW_IF;
	}

	// class openActionListener implements ActionListener {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// WorkingDirectoryDialog dialog = new
	// WorkingDirectoryDialog(SwingUtilities.getWindowAncestor(SwatToolbar.this));
	// String workingDirectory = dialog.getSimulationDirectory();
	// try { // Update Properties and reload
	// SwatComponents.SwatProperties.getInstance().setWorkingDirectory(workingDirectory);
	// SwatProperties.getInstance().addKnownWorkingDirectory(workingDirectory);
	// SwatProperties.getInstance().store();
	// SwatComponents.getInstance().reload(); // Inform TabView, etc...
	// tabView.removeAll();
	// treeView.removeAndUpdateSwatComponents();
	// } catch (ParameterException e2) {
	// JOptionPane.showMessageDialog(null, e2.getMessage(),
	// "Parameter Exception", JOptionPane.ERROR_MESSAGE);
	// e2.printStackTrace();
	// } catch (IOException e3) {
	// JOptionPane.showMessageDialog(null, e3.getMessage(), "IO Exception",
	// JOptionPane.ERROR_MESSAGE);
	// e3.printStackTrace();
	// } catch (PropertyException e1) {
	// JOptionPane.showMessageDialog(null, e1.getMessage(),
	// "Property Exception", JOptionPane.ERROR_MESSAGE);
	// e1.printStackTrace();
	// }
	//
	// }
	//
	// }


	class createNewAction implements ActionListener {

		private ToolbarButtonType type;

		public createNewAction(SwatToolbar.ToolbarButtonType type) {
			this.type = type;
		}

	@Override
	public void actionPerformed(ActionEvent e) {

			String netName = requestFileName("Please choose a name for the new net:", "New Petri-Net");
		if(netName != null){
			//IFNet newNet = new IFNet();
				//AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> newNet = null;
			try {
				// Test new file name
				File file = getAbsolutePathToWorkingDir(netName);
					switch (type) {
					case NEW_CPN:
						SwatComponents.getInstance().putIntoSwatComponent(new GraphicalCPN(), file);
						//newNet = new GraphicalCPN();
						break;
					case NEW_PT:
						SwatComponents.getInstance().putIntoSwatComponent(new GraphicalPTNet(), file);
						//newNet = new GraphicalPTNet();
						break;
					case NEW_IF:
						SwatComponents.getInstance().putIntoSwatComponent(new GraphicalIFNet(), file);
						//newNet = new GraphicalIFNet();
						break;

					default:
						break;
					}
					//GraphicalPTNet 
					//SwatComponents.getInstance().putIntoSwatComponent(newNet, file);
				//Inform Tree View of changed components
				treeView.removeAndUpdateSwatComponents();
			} catch (PropertyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParameterException e1) {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(SwatToolbar.this), e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private String requestFileName(String message, String title){
			return new FileNameDialog(SwingUtilities.getWindowAncestor(SwatToolbar.this.getParent()), message, title, false)
					.requestInput();
	}

		private File getAbsolutePathToWorkingDir(String name) throws PropertyException, ParameterException, IOException {
			File file = new File(SwatProperties.getInstance().getWorkingDirectory(), name + ".pnml");
			if (file.exists())
				throw new ParameterException("File already exists");
			//TODO: Validate, test if SWATComponent already contains net with same name... etc?
			return file;
	}
}
}
