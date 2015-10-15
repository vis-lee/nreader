/**
 * 
 */
package com.ptv.DB;

import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Geolocation.Location;

/**
 * @author Vis.Lee
 *
 */
// TODO need to remove the singleton pattern, db could be connect to multiple servers
public class PtvMSSqlServer implements DatabaseConstant, IDBAccess {

	private static final Logger logger = LogManager.getLogger(PtvMSSqlServer.class.getName());
	
	static private final String DEFDriver = "net.sourceforge.jtds.jdbc.Driver";
	
	protected Connection conn = null;
	
//	static private PtvDataBase dbhandle = null;
	
	// ensure singleton
	public PtvMSSqlServer() throws SQLException {
		
		__connect2DB(null, null, null, null);
		
	}
	
	public PtvMSSqlServer(String driver, String url, String username, String password) throws SQLException {
		
		__connect2DB(driver, url, username, password);
		
	}
	
	
	public boolean createConnection() throws SQLException {

		return createConnection();
		
	}
	
	@Override
	public boolean createConnection(String driver, String url, String username, String password) throws SQLException {
		
		if( conn == null ){
			
			conn = __connect2DB(driver, url, username, password);
			
		}
		
		return (conn != null) ? true: false;
		
	}
	
	synchronized private Connection __connect2DB(String driver, String url, String username, String password) throws SQLException {
		
		ResultSet rs = null;
		
		if( conn == null ){
			
			try {
				
				// check the parameters
				driver = (driver == null) ? DEFDriver:driver;
				url = (url == null) ? DBURL:url;
				username = (username == null) ? USERNAME:username;
				password = (password == null) ? PASSWORD:password;
				
				// try to connect to DB
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);
				
				logger.debug("Connected to the database!!! Getting table list...");
				
				if(logger.isDebugEnabled()){
					DatabaseMetaData dbm = conn.getMetaData();
		            rs = dbm.getTables(null, null, "%", new String[] { "TABLE" });
		            while (rs.next()) { logger.debug(rs.getString("TABLE_NAME")); }
				}
	            
				
			} catch (ClassNotFoundException e) {
				
				logger.error("jtds class not be found! name = " + DEFDriver, e);
				
			} catch (SQLException e) {
				
				logger.error("__connect2DB, connection failed!", e);
				
			} finally {
				
				if( (rs != null) && (!rs.isClosed()) ){
					rs.close();
				}
	        }
		}
		
		return conn;
		
	}
	
	public void releaseConnection(){
		
		if( conn != null ){
			__releaseConnection(conn);
		}
		
	}
	
	public Connection getConnection(){
		
		return conn;
		
	}
	
	synchronized private void __releaseConnection(Connection conn){
		
        try {
        	
        	if( conn != null ){
        		
        		if(!conn.isClosed()){
    				conn.close();
    				conn = null;
    			}
        	}
			
		} catch (SQLException e) {
			
			logger.error("releaseConnection got exception!", e);
		}
		
	}
	
	synchronized public CustomerInfo readCustomerInfo(UUID uid) throws SQLException {
		
		CustomerInfo ci = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			
			//String sql = "SELECT * FROM [NFC_TEST].[dbo].[nfc_poc_testing] where cardID=?";
			String sql = "SELECT * FROM " + NFC_DEFTABLE + " where cardID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid.toString());
			
			rs = pstmt.executeQuery();
			
			rs.getFetchSize();
			
			if( rs.next() ){
				
				// create CustomerInfo
				ci = new CustomerInfo(rs.getLong(1));
				ci.setCardID(UUID.fromString(rs.getString(2)));
				ci.setLocation(new Location(rs.getString(3), rs.getDouble(4), rs.getDouble(5)));
				ci.setLastTimeStamp(rs.getLong(6));
				
				logger.debug("found one ci: {}", ci);
				
			} else {
				
				// XXX insert customer info? or do nothing.
				// FIXME this is temporary sentence for PoC, need to be removed later
				ci = CustomerInfo.genDummyCustomerInfo(uid);
				writeCustomerInfo( ci );
				logger.warn("we didn't find out the related card ID {}, so we insert a dummy ci as \n {}", uid, ci);
			}
			
		} catch (SQLException e) {
			
			logger.error("readCustomerInfo got exception!", e);
			
		} finally {
			
			if(!pstmt.isClosed()){
				pstmt.close();
			}
			if(!rs.isClosed()){
				rs.close();
			}
			
		}
		
		return ci;
	}

	/*
	 * @RETURN retcode should return 1 which indicates 1 row been wrote.
	 * (non-Javadoc)
	 * @see com.ptv.DB.IDBAccess#writeCustomerInfo(com.ptv.Daemon.CustomerInfo)
	 */
	synchronized public int writeCustomerInfo(CustomerInfo customerInfo) throws SQLException {

		/*
		 *  the table schema:
		 *    userID bigint, 
		 *    cardID uniqueidentifier, 
		 *    region nvarchar(16), 
		 *    longitude float, 
		 *    latitude float, 
		 *    lastTimeStamp bigint 
		 */
		
		Random rn = new Random();
		
		// sql cmd
		String sql = "INSERT INTO " + NFC_DEFTABLE + 
				     " (userID, cardID, region, longitude, latitude, lastTimeStamp) VALUES (?,?,?,?,?,?)";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		// FIXME userID should bind with CRM or have some kind of rules to generate rather via random
		pstmt.setLong(1, rn.nextLong());
		pstmt.setString(2, customerInfo.getCardID().toString());
		pstmt.setString(3, customerInfo.getLocation().getRegion());
		pstmt.setDouble(4, customerInfo.getLocation().getLongitude());
		pstmt.setDouble(5, customerInfo.getLocation().getLatitude());
		pstmt.setLong(6, customerInfo.getLastTimeStamp());
		
		int retCode = pstmt.executeUpdate();
		
		return retCode;
	}



}
