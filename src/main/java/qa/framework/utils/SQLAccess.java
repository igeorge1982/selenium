package main.java.qa.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.apache.ibatis.jdbc.ScriptRunner;

import main.java.qa.framework.main.TestBase;
import main.java.qa.framework.main.WebElements;

public class SQLAccess extends TestBase implements WebElements {
	
	private static String dbDriverClass;
	private static String dbUrl;
	private static String dbUserName;
	private static String dbPassWord;
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private static long lastInsertId;
	private static String createProcedure_GetTestRun;
	private static String createProcedure_JoinTestRun;
	private volatile static UUID idOne;
	
	//public static boolean genSumRep;
	
	 public volatile static boolean genSumRep;

	public synchronized static boolean getGenSumRep() {
	    boolean tmp = genSumRep;
	    if (tmp = false) {
	        tmp = genSumRep;
	        if (tmp = false) {
	          genSumRep = tmp = true;
	      }
	    }
	    return tmp; // Using tmp here instead of myField avoids an memory update
	  }
	
	public SQLAccess(String dbDriverClass, String dbUrl, String dbUserName, String dbPassWord) {

		SQLAccess.dbDriverClass = dbDriverClass;
		SQLAccess.dbUrl = dbUrl;
		SQLAccess.dbUserName = dbUserName;
		SQLAccess.dbPassWord = dbPassWord;

	}
	
	  public synchronized final static UUID uuId(){

	          if (idOne == null) {
		     SQLAccess.idOne = UUID.randomUUID();
		     Log.info("UUID One: " + idOne);
	          	}
		  return idOne;
	         
	  }
	          
	public static void SetUpDataBase() throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);			
			
			Log.info("MySql connection is " + connect.isValid(3000));

		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);
		}

	}

	public static boolean testSummaryReport(String suite, String testname, int configFailes, int testFailes, int testSkipped, int testPassed)
			throws Exception {

		if (testname == null) {
			return false;
		}
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
					 
				long time = System.currentTimeMillis();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				
			String sql = "insert into  feedback.SUITE_MethodSummaryReport values (default, ?, ?, ?, ? , ?, ?, ?, ?, ?)";
				
			preparedStatement = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, suite);
			preparedStatement.setString(2, testname);
			preparedStatement.setInt(3, testPassed);
			preparedStatement.setInt(4, testFailes);
			preparedStatement.setInt(5, testSkipped);
			preparedStatement.setInt(6, configFailes);
			preparedStatement.setTimestamp(7, timestamp);
			preparedStatement.setString(8, testname);
			preparedStatement.setLong(9, lastInsertId);
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			while (rs.next()) {
				Log.info("GenKey: "+rs.getLong(1));
			}
			preparedStatement.closeOnCompletion();
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();
			Log.info("TestRuns was inserted into the db.");

		}
		getGenSumRep();// = true;
		return true;
	}
	
	public static boolean sessionId() throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
					 
				long time = System.currentTimeMillis();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				
			String sql = "insert into  feedback.Sessions values (default, ?, ?, ?)";
				
			preparedStatement = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,SQLAccess.uuId().toString());
			preparedStatement.setLong(2, lastInsertId);
			preparedStatement.setTimestamp(3, timestamp);
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			while (rs.next()) {
				Log.info("GenKey: "+rs.getLong(1));
			}
			preparedStatement.closeOnCompletion();
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();
			Log.info("SessionId was created");

		}
		return true;
	}
	
	public static boolean insertReport() throws Exception {
		
		Path testOutput = Paths.get("test-output/html");

		if (testOutput.toFile().exists()) {
			
			System.out.println("Real path: " + testOutput.toRealPath(LinkOption.NOFOLLOW_LINKS)); 

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
			String sqltestrun = "insert into feedback.testruns (id, time_) values (default, ?)";
			
			long time = System.currentTimeMillis();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
			
			preparedStatement = connect.prepareStatement(sqltestrun);
			preparedStatement.setTimestamp(1, timestamp);
			preparedStatement.executeUpdate();
			
			PreparedStatement getLastInsertId = connect.prepareStatement("select LAST_INSERT_ID() from feedback.testruns");
			ResultSet rs = getLastInsertId.executeQuery();
			while (rs.next()) {

				lastInsertId = rs.getLong("last_insert_id()");
			}	
			
			String sql = "insert into feedback.HTML_Reports  "
					+ "(id, Report_files, testrun_id) values (default, ?, ?)";
						
			preparedStatement = connect.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			
			File[] children = testOutput.toFile().listFiles();

			for (int i = 0; i < children.length; i++) {
				if (!children[i].isHidden()) {
				InputStream inputStream = new FileInputStream(new File(children[i].toString()));
				preparedStatement.setBlob(1, inputStream);
				preparedStatement.setLong(2, lastInsertId);
				
				int row = preparedStatement.executeUpdate();
				if (row > 0) {
					Log.info(children[i]+ " was inserted into the db.");
					}
				}
			}

			close();

		} catch (SQLException ex) {

		      SQLAccess.printSQLException(ex);

		} catch (IOException ex) {
			Log.info(ex.getCause());

		}
		return true;
		
		} else {
			
			Log.info(testOutput.toFile().toString() + " directory does not exist");			
		}
		return false;
	}

	  public static void createProcedureGetTestRun() throws SQLException {

		    String queryDrop = "DROP PROCEDURE IF EXISTS GET_TEST_RUN";

		    createProcedure_GetTestRun =
		        "create procedure GET_TEST_RUN(IN id int(11)) " +
		          "begin " +
		            "select * " +
		              "from feedback.SUITE_MethodSummaryReport " +
		              "where feedback.SUITE_MethodSummaryReport.id = id; " +
		          "end";
		    
		    createProcedure_JoinTestRun = 
		    	  "create procedure JOIN_TEST_RUN(IN id int(11)) " +
		    		 "begin " +
		    		   "select testruns.id as testid, SUITE_MethodSummaryReport.SUITE_NAME as Suite, " +
		    		   	"SUITE_MethodSummaryReport.TEST_NAME as Test, testruns.TIME_ as time, HTML_reports.Report_files as files " +
		    			 "from testruns " +
	    			 	 "join HTML_reports ON testruns.id = HTML_reports.testrun_id " +
		    			 "join SUITE_MethodSummaryReport ON SUITE_MethodSummaryReport.testrun_id = testruns.id " +
    	     	 	    "where testruns.id = id; " +
    			 	"end";
		    
		    Statement stmt = null;
		    Statement stmtDrop = null;

		    try {
		      System.out.println("Calling DROP PROCEDURE");
		      stmtDrop = connect.createStatement();
		      stmtDrop.execute(queryDrop);
		    } catch (SQLException e) {
		      SQLAccess.printSQLException(e);
		    } finally {
		      if (stmtDrop != null) { stmtDrop.close(); }
		    }


		    try {
		      stmt = connect.createStatement();
		      stmt.executeUpdate(createProcedure_GetTestRun);
		      stmt.executeUpdate(createProcedure_JoinTestRun);
		    } catch (SQLException e) {
		    	SQLAccess.printSQLException(e);
		    } finally {
		      if (stmt != null) { stmt.close(); }
		    }
		  }

	public static void runSqlScript(String sqlPath) throws ClassNotFoundException, SQLException, FileNotFoundException {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);			
			connect.isValid(3000);
			
			Log.info("MySql connection is " + connect.isValid(3000));
				
		// Initialize object for ScripRunner
		ScriptRunner sr = new ScriptRunner(connect);

		// Give the input file to Reader
		Reader reader = new BufferedReader(new FileReader(sqlPath));

		// Exctute script
		sr.runScript(reader);
		Log.info("Script run: "+ sqlPath);

	} catch (SQLException ex) {
	      SQLAccess.printSQLException(ex);
			}
		}

	// You need to close the resultSet
	private static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}
			
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connect != null) {
				connect.close();

			}
		} catch (Exception e) {

		}
	}
	
	  public static void printSQLException(SQLException ex) {
		    for (Throwable e : ex) {
		      if (e instanceof SQLException) {
		        if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
		          e.printStackTrace(System.err);
		          System.err.println("SQLState: " + ((SQLException)e).getSQLState());
		          System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
		          System.err.println("Message: " + e.getMessage());
		          Throwable t = ex.getCause();
		          while (t != null) {
		            System.out.println("Cause: " + t);
		            t = t.getCause();
		          }
		        }
		      }
		    }
		  }
	  
	  public static boolean ignoreSQLException(String sqlState) {
		    if (sqlState == null) {
		      System.out.println("The SQL state is not defined!");
		      return false;
		    }
		    // X0Y32: Jar file already exists in schema
		    if (sqlState.equalsIgnoreCase("X0Y32"))
		      return true;
		    // 42Y55: Table already exists in schema
		    if (sqlState.equalsIgnoreCase("42Y55"))
		      return true;
		    return false;
		  }

}
