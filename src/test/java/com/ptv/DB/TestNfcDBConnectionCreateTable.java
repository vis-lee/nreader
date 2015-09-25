package com.ptv.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestNfcDBConnectionCreateTable extends TestDBBase {

	
	public void testDBConnection() {
		
		final Logger logger = LogManager.getLogger(TestNfcDBConnectionCreateTable.class.getName());
		
		Connection conn = null;
		
		try {
			
			conn = connect2DB();
			
            Statement stmt = conn.createStatement();
            
            //
            // Create a test table
            //
            int ret = stmt.executeUpdate("CREATE TABLE #TESTTBS1 " + " (userID bigint, cardID uniqueidentifier, region nvarchar(16), longitude float, latitude float, lastTimeStamp bigint)");
            
            if(ret == 0){
            	System.out.println("create table success!");
            	stmt.close();
            } else {
            	return;
            }
            
            // insert data
            ret = insertData(conn);
            
            ret = validateData(conn);
            
            conn.close();
            
		} catch (ClassNotFoundException e) {
			logger.error("jtds class not be found!", e);
		} catch (SQLException e) {
			logger.error("SQL connection failed!", e);
			
		}
		
	}

	private int validateData(Connection conn) throws SQLException {
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			String qstr = "SELECT * FROM #TESTTBS1";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(qstr);
			
			int row = 1;
	        
	        while (rs.next()) {
	        	assertEquals(String.valueOf(row), rs.getString("userID"));
	        	System.out.println(rs.getString("userID"));
	        	row++;
	        }
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally{
			
			if(!stmt.isClosed()){
				stmt.close();
			}
			if(!rs.isClosed()){
				rs.close();
			}
		}
		
		return 0;
	}

	private int insertData(Connection conn) {
		
		int row = 1;
		Random rn = new Random();
		
        for (row = 1; row <= 10; row++) {
        	
        	UUID id = new UUID( (row + 100L), rn.nextLong());
        	
        	// a piece of code for transfer between bytes and UUID
        	/*System.out.println("orig UUID = " + id);
        	
        	byte[] bytes = PtvUtils.asBytes(id);
        	UUID testid = PtvUtils.asUuid(bytes);
            System.out.println("uuid to byte array 1: " + new String(bytes));
            System.out.println("uuid to byte array 2: " + testid);*/
            
        	
        	// another workable statement.
            // assertEquals(1, stmt.executeUpdate("INSERT INTO " + NFC_DEFTABLE +
            //       " VALUES(" + (new Long(row)) + ", '" + id + "', " + "'TW', " + rn.nextDouble() + ", " + rn.nextDouble() + ", " + (new Long(System.currentTimeMillis())) + ")"));
        	
    		// sql cmd
    		String sql = "INSERT INTO #TESTTBS1 (userID, cardID, region, longitude, latitude, lastTimeStamp) VALUES (?,?,?,?,?,?)";
    		
    		PreparedStatement pstmt;
    		
			try {
				pstmt = conn.prepareStatement(sql);
	    		pstmt.setLong(1, (row));
	    		pstmt.setString(2, id.toString());
	    		pstmt.setString(3, com.ptv.Geolocation.Location.PTV_REGION);
	    		pstmt.setDouble(4, rn.nextDouble());
	    		pstmt.setDouble(5, rn.nextDouble());
	    		pstmt.setLong(6, System.currentTimeMillis());
	    		
	    		int returncode = pstmt.executeUpdate();
	    		System.out.println("the return code = " + returncode);
	    		assertEquals(1, returncode);
	    		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
            
        }

		return 0;
	}

}
