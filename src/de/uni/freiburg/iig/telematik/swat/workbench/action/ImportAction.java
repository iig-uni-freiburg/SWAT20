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

	public ImportAction(String name) {
		super(name);
		try {
			setIcon(IconFactory.getIcon("import"));
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		//menu.show(source, 1, 2);
		menu.setLocation((int) source.getLocationOnScreen().getX(), (int) source.getLocationOnScreen().getY());
		menu.setVisible(true);
	}

}
