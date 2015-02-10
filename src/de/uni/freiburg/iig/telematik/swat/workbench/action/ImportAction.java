package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPopupMenu;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class ImportAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = -6755567361911699870L;

	public ImportAction(String name) {
		super(name);
		setTooltip("Import from filesystem");
		try {
			setIcon(IconFactory.getIcon("import"));
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}
	}

	public ImportAction() {
		this("Import");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		JButton source = (JButton) e.getSource();
		final JPopupMenu menu = new JPopupMenu("Menu");
		menu.add(new PTImportAction());
		menu.add(new LogImportAction());
		menu.add(new AFtemplateImportAction());
		menu.add(new AFSqlLogImportAction());
		menu.setInvoker((Component) e.getSource());
		menu.setLocation((int) source.getLocationOnScreen().getX(), (int) source.getLocationOnScreen().getY());
		menu.setVisible(true);
	}

}
