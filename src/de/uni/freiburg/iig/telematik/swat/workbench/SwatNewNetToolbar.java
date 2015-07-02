package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.NewNetAction;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.menu.WrapLayout;

/**
 * Model for Buttons. Holds buttons like "open", "save", ... With
 * {@link #getButtonPanel()} the buttons are available inside a {@link JPanel}.
 * Each button can be accesed through get(enum)
 * 
 * @author richard
 * 
 */
public class SwatNewNetToolbar extends JToolBar implements ActionListener, SwatStateListener {

	private static final long serialVersionUID = -4279345402764581310L;

	private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
	private static final String ACTION_COMMAND_ANALYSIS_MODE = "analysisMode";
	private static final int ICON_SPACING = 5;
	private SwatTreeView treeView = null;

	private List<Component> standardItems = new LinkedList<Component>();

	private WorkbenchPopupToolBar popupFontToolBar;

	public SwatNewNetToolbar(SwatTabView tabView, SwatTreeView treeView) {
		this.treeView = treeView;

		setFloatable(false);
		setRollover(true);

		// HACK!: To show PNEditor-Icons (they seem to be bigger)
		// setPreferredSize(new Dimension(200, ICON_SIZE + 30));

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
                    EditorProperties.getInstance().getIconSize().getSize();
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

	/**
	 * Put Buttons into linkedList {@link #standardItems} for later use
	 * 
	 * @throws IOException
	 * @throws PropertyException
	 * @throws ParameterException
	 **/
	private void createButtons() {

		try {
			standardItems.add(getNewPTNetButton());
			standardItems.add(getNewCPNButton());
			standardItems.add(getNewIFNetButton());
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private JButton getNewPTNetButton() throws ParameterException, PropertyException, IOException {
		JButton newButton = new SwatToolbarButton(ToolbarNewNetButtonType.NEW_PT, this);
		return newButton;
	}

	private JButton getNewCPNButton() throws ParameterException, PropertyException, IOException {
		JButton newButton = new SwatToolbarButton(ToolbarNewNetButtonType.NEW_CPN, this);
		return newButton;
	}

	private JButton getNewIFNetButton() throws ParameterException, PropertyException, IOException {
		JButton newButton = new SwatToolbarButton(ToolbarNewNetButtonType.NEW_IF, this);
		return newButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_COMMAND_ANALYSIS_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatNewNetToolbar.this, OperatingMode.ANALYSIS_MODE);
			} else if (e.getActionCommand().equals(ACTION_COMMAND_EDIT_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatNewNetToolbar.this, OperatingMode.EDIT_MODE);
			}
		} catch (ParameterException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void operatingModeChanged() {
		switch (SwatState.getInstance().getOperatingMode()) {
		case ANALYSIS_MODE:
			// getAnalysisRadioButton().setSelected(true);
			break;
		case EDIT_MODE:
			// getEditRadioButton().setSelected(true);
			break;
		}
		repaint();
	}

	public void clear() {
		this.removeAll();
		addStandardButtons();
	}

	private class SwatToolbarButton extends JButton {

		private static final long serialVersionUID = 9184814296174960480L;

		public SwatToolbarButton(ToolbarNewNetButtonType type, SwatNewNetToolbar swatNewNetToolbar) throws ParameterException, PropertyException, IOException {
			super(IconFactory.getIcon(type.toString().toLowerCase()));
			setBorder(BorderFactory.createEmptyBorder(0, ICON_SPACING, 0, ICON_SPACING));
			setBorderPainted(false);
			switch (type) {
			case NEW_CPN:
				setToolTipText("Create new CPnet");
				addActionListener(new NewNetAction(type, swatNewNetToolbar));
				break;
			case NEW_PT:
				setToolTipText("Create new PTnet");
				addActionListener(new NewNetAction(type, swatNewNetToolbar));
				break;
			case NEW_IF:
				setToolTipText("Create new IFnet");
				addActionListener(new NewNetAction(type, swatNewNetToolbar));
				break;
			}
		}

	}

	public enum ToolbarNewNetButtonType {
		NEW_PT, NEW_CPN, NEW_IF
	}

	public void setToolbar(WorkbenchPopupToolBar popupFontToolBar) {
		this.popupFontToolBar = popupFontToolBar;

	}
	
	public WorkbenchPopupToolBar getToolBar() {
		return this.popupFontToolBar;		
	}

	public static void main(String[] args) throws Exception {
		JPanel panel = new JPanel();
		panel.add(new SwatNewNetToolbar(SwatTabView.getInstance(), SwatTreeView.getInstance()));
		new DisplayFrame(panel, true);
	}



}
