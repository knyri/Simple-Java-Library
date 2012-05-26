package simple.sql;

import java.sql.*;
import java.util.Properties;
import java.util.Vector;

/*
DatabaseMetaData = Connection.getMetaData()
ResultSetMetaData = ResultSet.getMetaData()
*/

/**
 * This class attempts to simplify connections while keeping
 * full control of what's happening.
 * <br>Created: Sep 1, 2004
 * @author Kenneth Pierce
 */
public class Database {
	final Connection dbC;
	final Statement dbS;
	ResultSet dbRS;
	final DatabaseMetaData dbMD;
	public static final int DVROPT_URL = 0x01,
							DVROPT_SCHEMA = 0x02,
							DVROPT_CATALOG = 0x04,
							DVROPT_USERNAME = 0x08,
							DVROPT_PASSWORD = 0x10;
	public static final int CON_MYSQL = DVROPT_URL + DVROPT_SCHEMA + DVROPT_USERNAME + DVROPT_PASSWORD + 0x00,
							CON_ADVANTAGE = DVROPT_URL + DVROPT_CATALOG + DVROPT_USERNAME + DVROPT_PASSWORD + 0x20,
							CON_ORACLE = DVROPT_URL + DVROPT_CATALOG + DVROPT_USERNAME + DVROPT_PASSWORD + 0x40,
							CON_SQLSERVER = DVROPT_URL + DVROPT_CATALOG + DVROPT_USERNAME + DVROPT_PASSWORD + 0x60;
	public static final String[] CON_STR = new String[]{"", "", "jdbc:extendedsystems:advantage://%url%/%catalog%"};
	/**
	 * This constructor is tailored to be used with some kind of login screen.
	 * @param type One of CON_*
	 * @param url Usually the IP/hostname and the port
	 * @param schema DBMS dependant
	 * @param catalog DBMS dependant
	 * @param user Username
	 * @param password Password
	 * @throws ClassNotFoundException If the needed driver cannot be found.
	 * @throws SQLException If the actuall connection fails.
	 */
	public Database(int type, String url,String schema, String catalog, String user, String password) throws ClassNotFoundException, SQLException {
		switch (type) {
		case CON_MYSQL:
			Class.forName("org.gjt.mm.mysql.Driver");
			url = "jdbc:mysql://"+url+"/"+catalog;//assign DB specific URL
			//here, catalog is equivalent to the database name
			break;
		case CON_ADVANTAGE:
			Class.forName("com.extendedsystems.jdbc.advantage.ADSDriver");
			url = "jdbc:extendedsystems:advantage://"+url+"/"+catalog;//assign DB specific URL
			break;
		case CON_ORACLE:
			Class.forName("oracle.jdbc.driver.OracleDriver");
			url = "jdbc:oracle:thin:@"+url+":"+catalog;//assign DB specific URL
			break;
		case CON_SQLSERVER:
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			url = "jdbc:jtds:sqlserver//" + url + "/" + catalog;
			break;
		}
		if (url==null || url.equals("")) {
			url = "jdbc:extendedsystems:advantage://141.165.208.216:6262/osdg/mdsinc/ADSData/MDSData.add;user=odbc";
		}
		dbC = DriverManager.getConnection(url, user, password);
		dbS = dbC.createStatement();
		dbMD = dbC.getMetaData();
	}
	/**
	 * @param type One of CON_*
	 * @param driver The package.package.class location of the driver.
	 * @param url Usually IP/hostname and port number
	 * @param schema DBMS dependant
	 * @param catalog DBMS dependant
	 * @param user Username
	 * @param password Password
	 * @throws ClassNotFoundException If the driver was not found.
	 * @throws SQLException If the connection fails.
	 */
	public Database(int type, String driver, String url,String schema, String catalog, String user, String password) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		switch (type) {
		case CON_MYSQL:
			url = "jdbc:mysql://"+url+"/"+catalog;//assign DB specific URL
			//here, catalog is equivalent to the database name
			break;
		case CON_ADVANTAGE:
			url = "jdbc:extendedsystems:advantage://"+url+"/"+catalog;//assign DB specific URL
			break;
		case CON_ORACLE:
			url = "jdbc:oracle:thin:@"+url+":"+catalog;//assign DB specific URL
			break;
		case CON_SQLSERVER:
			url = "jdbc:jtds:sqlserver//" + url + "/" + catalog;
			break;
		}
		dbC = DriverManager.getConnection(url, user, password);
		dbS = dbC.createStatement();
		dbMD = dbC.getMetaData();
	}
	/**
	 * One of the two more advanced constructors.
	 * @param driver The package.package.class location of the driver.
	 * @param url Driver dependant.
	 * @param user Username
	 * @param password Password
	 * @throws ClassNotFoundException If the driver was not found.
	 * @throws SQLException If the connection fails.
	 */
	public Database(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		dbC = DriverManager.getConnection(url, user, password);
		dbS = dbC.createStatement();
		dbMD = dbC.getMetaData();
	}
	/**
	 * Second of the two more advanced constructors.
	 * @param driver The package.package.class location of the driver.
	 * @param url Location of the database. Format is driver dependant.
	 * @param props Driver dependant properties used to define the connection.
	 * @throws ClassNotFoundException If the driver was not found.
	 * @throws SQLException If the connection failed.
	 */
	public Database(String driver, String url, Properties props) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		dbC = DriverManager.getConnection(url, props);
		dbS = dbC.createStatement();
		dbMD = dbC.getMetaData();
	}
	public void close() {
		try {
			dbS.close();
			dbC.close();
		} catch (Exception e) {}
	}
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	public synchronized void executeQuery(String SQL) throws SQLException {
		dbRS = dbS.executeQuery(SQL);
	}
	public synchronized int executeUpdate(String SQL) throws SQLException {
		return dbS.executeUpdate(SQL);
	}
	@SuppressWarnings("unused")
	private void displayResultSet() throws SQLException {
		System.out.println("ResultSet:");
		while (dbRS.next()) {
			for (int i = 1;i<=dbRS.getMetaData().getColumnCount();i++) {
				System.out.print(dbRS.getString(i)+"\t");
			}
			System.out.println();
		}
	}
	public Statement createStatement() throws SQLException {return dbC.createStatement();}
	public ResultSet getDBRS() {return dbRS;}
	public DatabaseMetaData getDBMD() {return dbMD;}
	public String[] getSystemFunctions() throws SQLException {
		return dbMD.getSystemFunctions().split(",");
	}
	public String[] getStringFunctions() throws SQLException {
		return dbMD.getStringFunctions().split(",");
	}
	public String[] getTimeDateFunctions() throws SQLException {
		return dbMD.getTimeDateFunctions().split(",");
	}
	public String[] getNumericFunctions() throws SQLException {
		return dbMD.getNumericFunctions().split(",");
	}
	public synchronized Vector<String> getResultColumnNames() throws SQLException {
		ResultSetMetaData rsMD = dbRS.getMetaData();
		Vector<String> cNames = new Vector<String>();
		for (int i = 0;i<rsMD.getColumnCount();i++) {
			cNames.addElement(rsMD.getColumnName(i+1));
		}
		return cNames;
	}
	public synchronized Vector<String> getColumnNames(ResultSet rs) throws SQLException {
		ResultSetMetaData rsMD = rs.getMetaData();
		Vector<String> cNames = new Vector<String>();
		for (int i = 0;i<rsMD.getColumnCount();i++) {
			cNames.addElement(rsMD.getColumnName(i+1));
		}
		return cNames;
	}
	@SuppressWarnings("unused")
	private Vector<String> getData(String sql) throws SQLException {
		Statement stm = dbC.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		Vector<String> data = new Vector<String>();
		while(rs.next()) {
			data.addElement(rs.getString(1));
		}
		return data;
	}
	public synchronized void getColumnNames(String table) throws SQLException {
		dbRS = dbMD.getColumns(null, null, table, null);
	}
	public synchronized void getTables() throws SQLException {
		dbRS = dbMD.getTables(null, null, null, null);
	}
	public synchronized Vector<Vector<Object>> getResultValues() throws SQLException {
		Vector<Object> row;// = new Vector<Object>();
		Vector<Vector<Object>> table = new Vector<Vector<Object>>();
		while (dbRS.next()) {
			row = new Vector<Object>();
			for (int i = 0;i<dbRS.getMetaData().getColumnCount();i++) {
				row.addElement(dbRS.getObject(i+1));
			}
			table.addElement(row);
		}
		return table;
	}
	@SuppressWarnings("unused")
	private void displaySchemas() throws SQLException {
		ResultSet schema = dbC.getMetaData().getSchemas();
		ResultSetMetaData schemaMD = schema.getMetaData();
		System.out.println(dbC.getMetaData().getSchemaTerm());
		while (schema.next()) {
			for (int i = 1;i<=schemaMD.getColumnCount();i++) {
				System.out.println(schema.getString(i));
			}
		}
	}
	@SuppressWarnings("unused")
	private Vector<Vector<String>> toVector(ResultSet rs) throws SQLException {
		Vector<Vector<String>> table = new Vector<Vector<String>>();
		while(rs.next()) {
			table.add(rowToVector(rs));
		}
		return table;
	}
	private Vector<String> rowToVector(ResultSet rs) throws SQLException {
		Vector<String> row = new Vector<String>();
		for (int i = 0; i< rs.getMetaData().getColumnCount(); i++) {
			row.add(rs.getString(i));
		}
		return row;
	}
}