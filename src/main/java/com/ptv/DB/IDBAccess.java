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
	
	public boolean createConnection(String driver, String url, String username, String password) throws Exception;
	
	public void releaseConnection();
	
	public Connection getConnection();
	
	public CustomerInfo readCustomerInfo(UUID uid) throws Exception;
	
	public int writeCustomerInfo(CustomerInfo customerInfo) throws Exception;
	
	// TODO in the future
//	public StoreInfo readStoreInfo(UUID storeId);
//	
//	public int writeStoreInfo(StoreInfo storeInfo);
//	
//	public DisplayInfo readDisplayInfo(UUID displayId);
//	
//	public int writeDisplayInfo(DisplayInfo displayInfo);

}
