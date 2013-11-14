package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.invation.code.toval.graphic.DisplayFrame;
import de.invation.code.toval.graphic.FileNameChooser;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveActiveComponentAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SwitchWorkingDirectoryAction;

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
	
	private static final Dimension PREFERRED_SIZE = new Dimension(100,50);
	private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
	private static final String ACTION_COMMAND_ANALYSIS_MODE = "analysisMode";
	
	private JRadioButton rdbtnEdit = null;
	private JRadioButton rdbtnAnalysis = null;

	public SwatToolbar() {
		setFloatable(false);
		setRollover(true);
		setPreferredSize(PREFERRED_SIZE);
		
		add(getSwitchworkingDirectoryButton());
		add(getSaveActiveComponentButton());
		add(getSaveAllButton());
		add(getNewPTNetButton());
		add(getNewCPNButton());
		add(getNewIFNetButton());
		add(getEditRadioButton());
		add(getAnalysisRadioButton());
		
		ButtonGroup group = new ButtonGroup();
		group.add(getAnalysisRadioButton());
		group.add(getEditRadioButton());
		getEditRadioButton().setSelected(true);
		
		try {
			SwatState.getInstance().addListener(this);
		} catch (ParameterException e) {
			// Cannot happen, since this is never null.
		}
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
		JButton newButton = new JButton(UIManager.getIcon("FileView.directoryIcon"));
		newButton.addActionListener(new SwitchWorkingDirectoryAction());
		return newButton;
	}
	
	private JButton getSaveActiveComponentButton(){
		JButton newButton = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
		newButton.addActionListener(new SaveActiveComponentAction());
		return newButton;
	}
	
	private JButton getSaveAllButton(){
		//TODO Adjust Icon
		JButton newButton = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
		newButton.addActionListener(new SaveAllAction());
		return newButton;
	}
	
	private JButton getNewPTNetButton(){
		//TODO Adjust Icon
		JButton newButton = new JButton(UIManager.getIcon("FileView.fileIcon"));
		newButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String netName = requestFileName("Please choose a name for the new net:", "New P/T-Net");
				if(netName != null){
					PTNet newNet = new PTNet();
					//TODO Put net in components
				}
			}
		});
		return newButton;
	}
	
	private JButton getNewCPNButton(){
		//TODO Adjust Icon
		JButton newButton = new JButton(UIManager.getIcon("FileView.fileIcon"));
		newButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String netName = requestFileName("Please choose a name for the new net:", "New P/T-Net");
				if(netName != null){
					CPN newNet = new CPN();
					//TODO Put net in components
				}
			}
		});
		return newButton;
	}
	
	private JButton getNewIFNetButton(){
		//TODO Adjust Icon
		JButton newButton = new JButton(UIManager.getIcon("FileView.fileIcon"));
		newButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String netName = requestFileName("Please choose a name for the new net:", "New P/T-Net");
				if(netName != null){
					IFNet newNet = new IFNet();
					//TODO Put net in components
				}
			}
		});
		return newButton;
	}
	
	private String requestFileName(String message, String title){
		return new FileNameChooser(SwingUtilities.getWindowAncestor(getParent()), message, title, false).requestInput();
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
		panel.add(new SwatToolbar());
		new DisplayFrame(panel, true);
	}

	@Override
	public void operatingModeChanged() {
		System.out.println("mode changed tool bar");
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
}
