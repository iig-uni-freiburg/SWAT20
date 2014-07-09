package de.uni.freiburg.iig.telematik.swat.sciff;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import de.invation.code.toval.validate.ParameterException;

public class DatabaseChooser {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		DatabaseChooser.DatabaseChooser();

	}

	public static AristaFlowSQLConnector DatabaseChooser() throws SQLException, IOException, ClassNotFoundException {
		AristaFlowSQLConnector connector = new AristaFlowSQLConnector();
		String[] possibilities = connector.getDatabases();
		String s = (String) JOptionPane.showInputDialog(null, "Which Arista Flow datbase to use", "Choose Database",
				JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), possibilities, "ham");

		//If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			connector.connectToDatabase(s);
			return connector;
		}

		//If you're here, the return value was null/empty.
		throw new ParameterException("no database chossen");
	}


}
