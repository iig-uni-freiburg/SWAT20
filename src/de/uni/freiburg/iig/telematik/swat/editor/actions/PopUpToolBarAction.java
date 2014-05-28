package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.menu.FontToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.PopupToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBarDialog;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PopUpToolBarAction extends AbstractPNEditorAction {

	private JDialog popupDialog;
	private JToggleButton button;
	private JToolBar fontToolbarContent;
	private JButton newDialogButton;
	private PopupToolBar popupFontToolBar;
	protected JDialog dialog;

	public PopUpToolBarAction(PNEditor editor,String name, String iconName, JToolBar toolbar ) throws ParameterException, PropertyException, IOException {
		super(editor, name, IconFactory.getIcon(iconName));
		popupFontToolBar = new PopupToolBar();
		fontToolbarContent = toolbar;
		newDialogButton = new JButton(IconFactory.getIcon("maximize"));
		newDialogButton.setBorderPainted(false);
		newWindowButton(popupFontToolBar, fontToolbarContent, newDialogButton);
		   
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(getPopupFrame() == null){	
			popupFontToolBar.setButton(getButton(), false);
			
			popupFontToolBar.add(fontToolbarContent);
			 popupFontToolBar.add(newDialogButton);

		    int size = 0;
			try {
				size = SwatProperties.getInstance().getIconSize().getSize();
			} catch (PropertyException e1) {
			} catch (IOException e1) {
			}
			
			popupFontToolBar.show(getButton(), 0, size + size/2);
			
				}
		
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

	public void setButton(JToggleButton fontButton) {
		this.button = fontButton;
		
	}
	
	

	private void newWindowButton(final PopupToolBar popupFontToolBar, final JToolBar fontToolbarContent2, JButton newDialogButton) {
		newDialogButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					popupFontToolBar.setButton(getButton(), true);
					Window window = SwingUtilities.getWindowAncestor(getButton());
					JDialog dialog = new ToolBarDialog(window, fontToolbarContent2.getName(), false);
					dialog.setTitle("Font");
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

	
	

}
