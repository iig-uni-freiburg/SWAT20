package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBarDialog;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.PopupToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.TokenlabelToolBar;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PopUpToolBarAction extends AbstractPNEditorAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3097560664208606500L;
	private JDialog popupDialog;
	private JToggleButton button;
	private JToolBar toolbarContent;
	private JButton newDialogButton;
	private PopupToolBar popupToolBar;
	protected JDialog dialog;

	public PopUpToolBarAction(PNEditor editor, String name, String iconName, JToolBar toolbar) throws ParameterException, PropertyException, IOException {
		super(editor, name, IconFactory.getIcon(iconName));
		popupToolBar = new PopupToolBar();
		toolbarContent = toolbar;
		newDialogButton = new JButton(IconFactory.getIcon("maximize"));
		newDialogButton.setBorderPainted(false);
		newWindowButton(popupToolBar, toolbarContent, newDialogButton);
	}

	protected JDialog getPopupFrame() {
		return popupDialog;
	}

	public void setPopupFrame(JDialog dialog) {
		this.popupDialog = dialog;
	}

	protected JToggleButton getButton() {
		// TODO Auto-generated method stub
		return button;
	}

	public void setButton(JToggleButton addedButton) {
		this.button = addedButton;

	}

	private void newWindowButton(final PopupToolBar popupToolBar, final JToolBar toolbarContent, final JButton newDialogButton) {
		newDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupToolBar.setButton(getButton(), true);
				Window window = SwingUtilities.getWindowAncestor(getButton());
				JDialog dialog = new ToolBarDialog(window, toolbarContent.getName(), false);
				if (newDialogButton.getName() != null)
					dialog.setTitle(newDialogButton.getName());
				dialog.setLocationRelativeTo(window);
				dialog.add(toolbarContent);
				dialog.setModal(false);
				dialog.setResizable(false);
				getButton().addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						getButton().setSelected(true);
						super.mouseClicked(e);
					}
				});

				dialog.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						getButton().setSelected(false);
						setPopupFrame(null);

					}
				});
				setPopupFrame(dialog);
				dialog.pack();
				dialog.setVisible(true);
				setDialog(dialog);
			}

		});
	}

	protected void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public JDialog getDialog() {
		return dialog;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (getPopupFrame() == null) {
			popupToolBar.setButton(getButton(), false);

			popupToolBar.add(toolbarContent);
			popupToolBar.add(newDialogButton);

			int size = 0;
			size = SwatProperties.getInstance().getIconSize().getSize();
			popupToolBar.show(getButton(), 0, size + size / 2);
		}
	}

}
