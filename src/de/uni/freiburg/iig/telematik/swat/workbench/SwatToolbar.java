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
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
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

	private void createButtons() {
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.SAVE));
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.SAVE_ALL));
		standardItems.add(getSwitchworkingDirectoryButton());
		standardItems.add(getNewPTNetButton());
		standardItems.add(getNewCPNButton());
		standardItems.add(getNewIFNetButton());
		standardItems.add(getEditRadioButton());
		standardItems.add(getAnalysisRadioButton());
		
		ButtonGroup group = new ButtonGroup();
		group.add(getAnalysisRadioButton());
		group.add(getEditRadioButton());
		getEditRadioButton().setSelected(true);
		//addSeparator();
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
		//TODO Adjust Icon
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW);
		newButton.addActionListener(new createNewAction());
		//		newButton.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				String netName = requestFileName("Please choose a name for the new net:", "New P/T-Net");
		//				if(netName != null){
		//					try {
		//						File file = getAbsolutePathToWorkingDir(netName);
		//						PTNet newNet = new PTNet();
		//						AbstractGraphicalPTNet<?, ?, ?, ?, ?, ?> test;
		//					} catch (PropertyException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					} catch (ParameterException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					} catch (IOException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					}
		//
		//					//TODO Put net in components
		//				}
		//			}
		//		});
		return newButton;
	}
	
	private JButton getNewCPNButton(){
		//TODO Adjust Icon
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW);
		newButton.addActionListener(new createNewAction());
		//		newButton.addActionListener(new ActionListener() {
		//			
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				String netName = requestFileName("Please choose a name for the new net:", "New P/T-Net");
		//				if(netName != null){
		//					CPN newNet = new CPN();
		//					//TODO Put net in components
		//				}
		//			}
		//		});
		return newButton;
	}
	
	private JButton getNewIFNetButton(){
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW);
		newButton.addActionListener(new createNewAction());
		//		newButton.addActionListener(new ActionListener() {
		//			
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				String netName = requestFileName("Please choose a name for the new net:", "New P/T-Net");
		//				if(netName != null){
		//					//IFNet newNet = new IFNet();
		//					try {
		//						// Test new file name
		//						File file = getAbsolutePathToWorkingDir(netName);
		//						GraphicalPTNet newNet = new GraphicalPTNet();
		//						SwatComponents.getInstance().putIntoSwatComponent(newNet, file);
		//						//Inform Tree View of changed components
		//						treeView.removeAndUpdateSwatComponents();
		//					} catch (PropertyException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					} catch (ParameterException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					} catch (IOException e1) {
		//						// TODO Auto-generated catch block
		//						e1.printStackTrace();
		//					}
		//
		//				}
		//			}
		//		});
		newButton.setToolTipText("Create new IFnet");
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
			}
		}
		
	}
	
	private enum ToolbarButtonType {
		NEW, SAVE, SAVE_ALL, OPEN, IMPORT, SWITCH_DIRECTORY;
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

	@Override
	public void actionPerformed(ActionEvent e) {

		String netName = requestFileName("Please choose a name for the new net:", "New P/T-Net");
		if(netName != null){
			//IFNet newNet = new IFNet();
			try {
				// Test new file name
				File file = getAbsolutePathToWorkingDir(netName);
				GraphicalPTNet newNet = new GraphicalPTNet();
				SwatComponents.getInstance().putIntoSwatComponent(newNet, file);
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
