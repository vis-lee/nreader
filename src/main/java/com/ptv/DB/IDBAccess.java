/**
 * 
 */
package com.ptv.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Daemon.DisplayInfo;
import com.ptv.Daemon.StoreInfo;

/**
 * interface defined database access APIs
 * @author Vis.Lee
 *
 */
public interface IDBAccess {
	
	
//	public DatabaseHandle getDatabaseHandle();
	
	public Connection connectToDatabase(String url) throws SQLException;
	
	public void releaseConnection(Connection conn);
	
	public CustomerInfo readCustomerInfo(Connection conn, UUID uid) throws SQLException;
	
	public int writeCustomerInfo(Connection conn, CustomerInfo customerInfo) throws SQLException;
	
	// TODO in the future
//	public StoreInfo readStoreInfo(UUID storeId);
//	
//	public int writeStoreInfo(StoreInfo storeInfo);
//	
//	public DisplayInfo readDisplayInfo(UUID displayId);
//	
//	public int writeDisplayInfo(DisplayInfo displayInfo);

}
