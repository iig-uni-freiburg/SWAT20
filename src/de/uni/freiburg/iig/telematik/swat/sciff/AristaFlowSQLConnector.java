package de.uni.freiburg.iig.telematik.swat.sciff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLog;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/** Reads AristaFlow database and converts to csv file **/
public class AristaFlowSQLConnector {
	private final String QUERY = "select eh.TIMESTAMP as Start, ehEnd.TIMESTAMP as End, eh.NODEID, eh.ITERATION, ag.FIRSTNAME, ag.LASTNAME, op.NAME, eh.NODENAME, eh.INSTANCENAME, eh.INSTANCELOGID, eh.TEMPLATENAME from EXECUTIONHISTORY eh join EXECUTIONHISTORY ehEnd on eh.NODEID = ehEnd.NODEID and eh.ITERATION = ehEnd.ITERATION and eh.STATECHANGE = 14 and ehEnd.STATECHANGE = 18 and eh.INSTANCELOGID = ehEnd.INSTANCELOGID join AGENT ag on eh.AGENTID = ag.ID join ORGPOSITION op on eh.AGENTORGPOSITIONID = op.ID where eh.TEMPLATENAME like 'Eingangsrechnung' order by eh.TIMESTAMP";
	private Connection con;
	private Statement st;
	private FileWriter fileWriter;
	private final String[] HEADER = { "START", "END", "NODEID", "ITERATION", "FIRSTNAME", "LASTNAME", "NAME", "NODENAME", "INSTANCENAME",
			"INSTANCELOGID", "TEMPLATENAME" };
	private final File tempFile = new File(System.getProperty("java.io.tmpdir") + "/aristaFlowExport.csv");
	private LogModel model;

	public static void main(String[] args) throws IOException {

		String url = SwatProperties.getInstance().getAristaFlowURL();
		System.out.println(changeDatabase(url, "neueDB"));

		try {
			SwatProperties prop = SwatProperties.getInstance();
			prop.setAristaFlowURL("jdbc:postgresql://127.0.0.1:5432/InvoiceLocal");
			prop.setAristaFlowUser("ADEPT2");
			prop.setAristaFlowPass("ADEPT2DB");
			//prop.store();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AristaFlowSQLConnector con;
		try {
			//con = new AristaFlowSQLConnector("127.0.0.1", "ADEPT2", "ADEPT2DB");
			con = new AristaFlowSQLConnector();
			AristaFlowParser result = con.parse();
			con.getDatabases();
			System.out.println(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public AristaFlowSQLConnector(String ip, String user, String password) throws SQLException, ClassNotFoundException, IOException {
		//Create PostreSQL connection
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection("jdbc:postgresql://" + ip + "/InvoiceLocal", user, password);
		//con = DriverManager.getConnection(server + "/InvoiceLocal", user, password);
		st = con.createStatement();
		fileWriter = new FileWriter(tempFile);
	}

	public String[] getDatabases() throws SQLException, ClassNotFoundException {
		String dbQuery = "SELECT d.datname as \"Name\", pg_catalog.pg_get_userbyid(d.datdba) as \"Owner\",pg_catalog.pg_encoding_to_char(d.encoding) as \"Encoding\", d.datcollate as \"Collate\", d.datctype as \"Ctype\",pg_catalog.array_to_string(d.datacl, E'\n') AS \"Access privileges\" FROM pg_catalog.pg_database d ORDER BY 1;";

		ResultSet resultSet = con.createStatement().executeQuery(dbQuery);
		List<String> entries = new LinkedList<String>();
		while (resultSet.next()) {
			String result=resultSet.getString(1);
			if (!result.equals("postgres") && !result.equals("template0") && !result.equals("template1")) {
				entries.add(result);
			}
		}
		String[] results = { "" };
		return entries.toArray(results);
	}

	/**
	 * Reads Credentials from Properties
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 **/
	public AristaFlowSQLConnector() throws ClassNotFoundException, IOException, SQLException {
		//Create PostreSQL connection
		Class.forName("org.postgresql.Driver");
		String url = SwatProperties.getInstance().getAristaFlowURL();
		String user = SwatProperties.getInstance().getAristaFlowUser();
		String pass = SwatProperties.getInstance().getAristaFlowPass();
		con = DriverManager.getConnection(url, user, pass);
		//con = DriverManager.getConnection(server + "/InvoiceLocal", user, password);
		st = con.createStatement();
		fileWriter = new FileWriter(tempFile);
	}

	public void connectToDatabase(String database) throws SQLException, IOException {
		String url = changeDatabase(SwatProperties.getInstance().getAristaFlowURL(), database);
		String user = SwatProperties.getInstance().getAristaFlowUser();
		String pass = SwatProperties.getInstance().getAristaFlowPass();
		con = DriverManager.getConnection(url, user, pass);
	}

	private static String changeDatabase(String originalUrl, String newDatabase) {
		int i = originalUrl.lastIndexOf("/");
		return originalUrl.substring(0, i) + "/" + newDatabase;
	}

	public AristaFlowParser parse() throws Exception {
		createFirstLine(fileWriter);
		ResultSet rs = st.executeQuery(QUERY);
		while (rs.next()) {
			writeLine(rs, fileWriter);
		}
		fileWriter.close();
		AristaFlowParser parser = new AristaFlowParser(tempFile);
		parser.parse(whichTimestamp.BOTH);

		return parser;
	}

	public File getTempFile() {
		return tempFile;
	}

	public LogModel getModel() throws Exception {
		if (model == null) {
			//File file = new File(SwatProperties.getInstance().getLogWorkingDirectory(), "AristaFlowImport " + getCurrentTime() + ".csv");
			File file = File.createTempFile("AristaFlowImport", "csv");
			FileWriter fileWriter = new FileWriter(file);
			createFirstLine(fileWriter);
			ResultSet rs = st.executeQuery(QUERY);
			while (rs.next()) {
				writeLine(rs, fileWriter);
			}
			//LogFileViewer logFile = new LogFileViewer(file);
			model = new LogModel(file, SwatLog.Aristaflow);
			//SwatComponents.getInstance().putCsvIntoSwatComponent(log, log.getName());
		}

		return model;
	}


	public String getCurrentTime() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-" + Calendar.getInstance().get(Calendar.MINUTE);
	}


	private void writeLine(ResultSet rs, FileWriter fileWriter) throws SQLException, IOException {
		StringBuilder sb = new StringBuilder();
		boolean firstElement = true;
		for (int i = 0; i < HEADER.length; i++) {
			if (!firstElement)
				sb.append(";");
			firstElement = false;
			sb.append(rs.getString(HEADER[i]));
		}
		sb.append("\r\n");
		fileWriter.write(sb.toString());

	}

	private void createFirstLine(FileWriter fileWriter) throws IOException {
		fileWriter.write("START;END;NODEID;ITERATION;FIRSTNAME;LASTNAME;NAME;NODENAME;INSTANCENAME;INSTANCELOGID;TEMPLATENAME");
		fileWriter.write("\r\n");
	}

}
