package com.ptv.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;

public class TestDBBase extends TestCase implements DatabaseConstant{

	protected final Logger logger = LogManager.getLogger(TestDBBase.class.getName());
	
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}


	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {

		// release connection
		if( !conn.isClosed() ){
			
			conn.close();
		}
		
		super.tearDown();
	}


	Connection conn = null;
	
	
	public Connection connect2DB() throws ClassNotFoundException, SQLException{
		
		Class.forName(DBDRIVER_NAME);
		conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
		
		DatabaseMetaData dbm = conn.getMetaData();
        
		ResultSet rs = dbm.getTables(null, null, "%", new String[] { "TABLE" });
        
        while (rs.next()) {
        	System.out.println(rs.getString("TABLE_NAME")); 
        }
        
		return conn;
        
	}
	
}
