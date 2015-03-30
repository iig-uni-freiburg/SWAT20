package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatNewNetToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchPopupToolBar;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.menu.ToolBarDialog;

public class PopUpToolBarAction extends AbstractAction {

	private static final long serialVersionUID = 7214726307740810351L;
	private JDialog popupDialog;
	private JButton button;
	private SwatNewNetToolbar toolbarContent;
	private JButton newDialogButton;
	private WorkbenchPopupToolBar popupToolBar;
	protected JDialog dialog;

	public PopUpToolBarAction(String name, String iconName, SwatNewNetToolbar toolbar ) throws ParameterException, PropertyException, IOException {
		super(name, IconFactory.getIcon(iconName));
		popupToolBar = new WorkbenchPopupToolBar();
		popupToolBar.disposeAllWindows();
		toolbarContent = toolbar;
		toolbarContent.setToolbar(popupToolBar);
		newDialogButton = new JButton(IconFactory.getIcon("maximize"));
		newDialogButton.setBorderPainted(false);
		newWindowButton(popupToolBar, toolbarContent, newDialogButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(getPopupFrame() == null){	
			popupToolBar.setButton(getButton(), false);			
			popupToolBar.add(toolbarContent);
			 popupToolBar.add(newDialogButton);

		    int size = 0;
			try {
				size = SwatProperties.getInstance().getIconSize().getSize();
			} catch (PropertyException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			popupToolBar.show(getButton(), 0, size + size/2);
				}
	}
	
	private void newWindowButton(final WorkbenchPopupToolBar popupToolBar, final JToolBar toolbarContent, final JButton newDialogButton) {
		newDialogButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					popupToolBar.setButton(getButton(), true);
					Window window = SwingUtilities.getWindowAncestor(getButton());
					JDialog dialog = new ToolBarDialog(window, toolbarContent.getName(), false);
					if(newDialogButton.getName() != null)
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

	
	protected JDialog getPopupFrame() {
		return popupDialog;
	}
	
	public void setPopupFrame(JDialog dialog) {
		this.popupDialog = dialog;
	}

	protected JButton getButton() {
		return button;
	}

	public void setButton(JButton swatToolbarButton) {
		this.button = swatToolbarButton;		
	}
	

	protected void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public JDialog getDialog() {
		return dialog;
	}
	
	public void disposeAllWindows() {
		popupToolBar.disposeAllWindows();
	}
	
	

}
