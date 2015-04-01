package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Icon;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.misc.errorhandling.ErrorStorage;

public class SendExceptionsAsEmail extends AbstractWorkbenchAction {

	public SendExceptionsAsEmail() {
		this("Send Bug Report");
	}

	public SendExceptionsAsEmail(String name, Icon icon) {
		super(name, icon);
	}

	public SendExceptionsAsEmail(String name) {
		super(name);
		try {
			setIcon(IconFactory.getIcon("error"));
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		ErrorStorage.getInstance().sendAsMail();

	}

}
