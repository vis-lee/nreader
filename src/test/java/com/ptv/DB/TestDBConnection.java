package com.ptv.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;

public class TestDBConnection extends TestDBBase {

	
	public void testDBConnection() {
		
		final Logger logger = LogManager.getLogger(TestDBConnection.class.getName());
		
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			
			conn = connect2DB();
			
            Statement stmt = conn.createStatement();
            
            stmt.execute("CREATE TABLE #TESTTBS1 " + " (userID int)");
            
            int row = 1;
            for (row = 1; row <= 10; row++) {
                assertEquals(1, stmt.executeUpdate("INSERT INTO #TESTTBS1" +
                        " VALUES(" + row + ")"));
            }
            
            /*
             * test query
             */
            String qstr = "SELECT * FROM #TESTTBS1";
            
            rs = stmt.executeQuery(qstr);
            
            logger.info("total fetch size = {} ", rs.getFetchSize());
            
            row = 1;
            while (rs.next()) {
            	assertEquals(String.valueOf(row), rs.getString("userID"));
            	System.out.println(rs.getString("userID"));
            	row++;
            }
            
            rs.close();
            stmt.close();
            conn.close();

            
		} catch (ClassNotFoundException e) {
			logger.error("jtds class not be found!", e);
		} catch (SQLException e) {
			logger.error("SQL connection failed!", e);
		}
		
	}

}
