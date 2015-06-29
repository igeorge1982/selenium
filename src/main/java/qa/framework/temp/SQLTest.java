package main.java.qa.framework.temp;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import main.java.qa.framework.main.TestBase;
import main.java.qa.framework.main.WebElements;
import main.java.qa.framework.utils.SQLAccess;

public class SQLTest extends TestBase implements WebElements {

	public static Logger Log = Logger.getLogger(Logger.class.getName());

	public static void main(String[] args) throws Exception {

		DOMConfigurator.configure(log4jxml);
		
		
		SQLAccess.SetUpDataBase();
		SQLAccess.runSqlScript(create_db_sql);
		SQLAccess.createProcedureGetTestRun();
		SQLAccess.sessionId();
		SQLAccess.sessionId();

		if (SQLAccess.insertReport()) {
			
			if (!SQLAccess.genSumRep) {
				Log.info(SQLAccess.genSumRep);	
					SQLAccess.testSummaryReport("suite", "hello", 0, 0, 0, 0);
					SQLAccess.genSumRep=true;
					Log.info(SQLAccess.genSumRep);	
			//Log.info(SQLAccess.getGenSumRep());	
			
			if (!SQLAccess.genSumRep) {
				
				SQLAccess.testSummaryReport("suite", "hello", 0, 0, 0, 0);
		Log.info(SQLAccess.getGenSumRep());	
		}
			
			}
			
		}
		
//		SQLAccess.insertReport2();
		TestBase.memory();

	}

}
