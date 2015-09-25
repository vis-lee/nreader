package com.ptv.Daemon;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.DB.DatabaseHandle;
import com.ptv.Reader.ReadersManager;

public class PtvDaemonOperations {
	
	private static final Logger logger = LogManager.getLogger(PtvDaemon.class.getName());
	
	protected ReadersManager readersManager;
	protected DatabaseHandle dbhandle;
    
	private PtvDaemon ptvDaemon = null;
	
	
	public PtvDaemonOperations(PtvDaemon ptvDaemon) {
		
		this.ptvDaemon = ptvDaemon;
	}


	/*public boolean checkIDReaders(){
		
		boolean ret = false;
		
		// id operations
		LinkedList<UUID> ids = readersManager.pollingAllReaders();
		
		// TODO DB operations
		
		// TODO presentations
		
		return true;
	}*/
	
	public int goOperations() {
		
		int retcode = PtvConstant.SUCCESS;
		
		// id operations
		LinkedList<UUID> ids = readersManager.pollingAllReaders();
		
		// TODO DB operations
		CustomerInfo ci = null;
		
		try {
			
			for( Iterator<UUID> iter = ids.iterator(); iter.hasNext(); ){
				
				UUID uid = iter.next();
				
				ci = dbhandle.readCustomerInfo(uid);
				
				// TODO perform operations by operationEnum
				Presentations.ShowPage(ci);
				
			}
			
		} catch (SQLException e) {
			
			logger.error("getCustomerInfoFromDataBase exception!");
		}
		
		return retcode;
	}
	
//	private CustomerInfo getCustomerInfoFromDatabase(LinkedList<UUID> ids) {
//
//		CustomerInfo ci = null;
//		
//		try {
//			
//			for( Iterator<UUID> iter = ids.iterator(); iter.hasNext(); ){
//				
//				UUID uid = iter.next();
//				
//				ci = dbhandle.readCustomerInfo(uid);
//				
//			}
//			
//		} catch (SQLException e) {
//			
//			logger.error("getCustomerInfoFromDataBase exception!");
//		}
//		
//		return ci;
//	}


	public int initOperations() {
		
		// TODO init rest
		try {
			dbhandle = DatabaseHandle.getDatabaseHandle();
		} catch (SQLException e) {
			logger.error("can't get init Database connection fail!", e);
		}
		
		// init reader manager
		readersManager = ReadersManager.getReadersManager();
		
		return 0;
	}
	
	public int exitOperations() {
	
		// run the shutdown hooks
		// 1. shutdown the reader manager
		ReadersManager.terminateReadersManager();
		
		// TODO 2. close the db manager
		dbhandle.releaseConnection();
		// TODO 3. terminate the presenter
		
		// ALL end
		logger.info(" ptv operations exit! ");
		
		return 0;
	}
	
	
	
	
	//******************************************************************************************//
	//																							//
	//									operations definition									//
	//																							//
	//******************************************************************************************//
	
	public enum OperationsEnum {
		
		WriteInfo,
		ShowPage,
		PlayVideo
		
	}
	
	
}
