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
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.menu.PopupToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBarDialog;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatNewNetToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchPopupToolBar;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PopUpToolBarAction extends AbstractAction {

	private JDialog popupDialog;
	private JButton button;
	private SwatNewNetToolbar fontToolbarContent;
	private JButton newDialogButton;
	private WorkbenchPopupToolBar popupFontToolBar;
	protected JDialog dialog;

	public PopUpToolBarAction(String name, String iconName, SwatNewNetToolbar toolbar ) throws ParameterException, PropertyException, IOException {
		super(name, IconFactory.getIcon(iconName));
		popupFontToolBar = new WorkbenchPopupToolBar();
		popupFontToolBar.disposeAllWindows();
		fontToolbarContent = toolbar;
		fontToolbarContent.setToolbar(popupFontToolBar);
		newDialogButton = new JButton(IconFactory.getIcon("maximize"));
		newDialogButton.setBorderPainted(false);
		newWindowButton(popupFontToolBar, fontToolbarContent, newDialogButton);
		   
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(getPopupFrame() == null){	
			popupFontToolBar.setButton(getButton(), false, true);
			
			popupFontToolBar.add(fontToolbarContent);
			 popupFontToolBar.add(newDialogButton);
			

		    int size = 0;
			try {
				size = SwatProperties.getInstance().getIconSize().getSize();
			} catch (PropertyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			popupFontToolBar.show(getButton(), 0, size*3+size/5);
			
				}
		
	}
	
	protected JDialog getPopupFrame() {
		return popupDialog;
	}
	
	public void setPopupFrame(JDialog dialog) {
		this.popupDialog = dialog;
	}

	protected JButton getButton() {
		// TODO Auto-generated method stub
		return button;
	}

	public void setButton(JButton swatToolbarButton) {
		this.button = swatToolbarButton;
		
	}
	
	

	private void newWindowButton(final WorkbenchPopupToolBar popupFontToolBar2, final JToolBar fontToolbarContent2, JButton newDialogButton) {
		newDialogButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					popupFontToolBar2.setButton(getButton(), true, false);
					Window window = SwingUtilities.getWindowAncestor(getButton());
					JDialog dialog = new ToolBarDialog(window, fontToolbarContent2.getName(), false);
					dialog.setTitle("Font");
//					dialog.setSize(new Dimension(fontToolbarContent2.getSize().width, fontToolbarContent2.getSize().height + 20));
					dialog.setLocationRelativeTo(window);
					dialog.add(fontToolbarContent2);
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
	
	
	public void disposeAllWindows() {
		popupFontToolBar.disposeAllWindows();
	}
	
	

}
