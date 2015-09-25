package com.ptv.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Geolocation.Location;

import junit.framework.TestCase;

public class TestNfcDBConnectionSelectByUID extends TestDBBase {

	
	public void testDBSelectByUID() {
		
		final Logger logger = LogManager.getLogger(TestNfcDBConnectionSelectByUID.class.getName());
		
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			
			conn = connect2DB();
			
            // get the last one record
           UUID uid = getLastRecordUID(conn);
            
            // try to get the last one
            CustomerInfo ci = null;
            String sql = "SELECT * FROM " + NFC_DEFTABLE + " where cardID=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid.toString());
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ){
				
				// create CustomerInfo
				ci = new CustomerInfo(rs.getLong(1));
				ci.setCardID(UUID.fromString(rs.getString(2)));
				ci.setLocation(new Location(rs.getString(3), rs.getDouble(4), rs.getDouble(5)));
				ci.setLastTimeStamp(rs.getLong(6));
				
			} else {
				
				// XXX insert customer info? or do nothing.
			}
			
			logger.info("test result = " + ci);
            
            rs.close();
            pstmt.close();
            conn.close();

            
		} catch (ClassNotFoundException e) {
			logger.error("jtds class not be found!", e);
		} catch (SQLException e) {
			logger.error("SQL connection failed!", e);
		}
		
	}

	private UUID getLastRecordUID(Connection conn) {
		
		UUID uid = null;
		
		try {
			
			String qstr = "SELECT * FROM " + NFC_DEFTABLE;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(qstr);
			
			while (rs.next()) {
				System.out.println(rs.getString("cardID"));
				uid = UUID.fromString(rs.getString(2));
			}

			stmt.close();
			rs.close();
			
			if(uid == null){
				logger.error("no record for testing");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return uid;
	}

}
