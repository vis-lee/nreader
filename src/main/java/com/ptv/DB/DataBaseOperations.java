/**
 * 
 */
package com.ptv.DB;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;

/**
 * @author Vis.Lee
 *
 */
public class DataBaseOperations {

	private static final Logger logger = LogManager.getLogger(PtvMSSqlServer.class.getName());
	
	static private DataBaseOperations dbOperations;
	
	private IDBAccess db;
	
	public boolean isFileDBEnable = false;
	
	
	private DataBaseOperations() throws Exception {
		
		isFileDBEnable = Boolean.getBoolean("enable.FileDB");
		
		if(isFileDBEnable){
			
			db = (IDBAccess) new FileDataBase();
			
		} else {
			
			db = (IDBAccess) new PtvMSSqlServer();
		}
		
	}
	
	
	
	// connect to DB
	static public DataBaseOperations getDatabaseOperations() throws Exception {
		
		if( dbOperations == null){
			return __getDatabaseOperations();
		}
		
		return dbOperations;
	}
	

	synchronized static private DataBaseOperations __getDatabaseOperations() throws Exception{
		
		if(dbOperations == null){
			
			dbOperations = new DataBaseOperations();

		}
		
		return dbOperations;
	}
	
	
	public CustomerInfo readCustomerInfo(UUID uid) throws Exception{
		
		return db.readCustomerInfo(uid);
		
	}
	
	
	public int writeCustomerInfo(CustomerInfo customerInfo) throws Exception {
		
		return db.writeCustomerInfo(customerInfo);
		
	}
	
	
	public void releaseOperations() {
		
		db.releaseConnection();
		
	}
	
}
