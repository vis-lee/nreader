package com.ptv.DB;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;

import junit.framework.TestCase;

public class TestFileDB extends TestCase {
	
	final Logger logger = LogManager.getLogger(TestFileDB.class.getName());
	
	private CustomerInfo origCi = CustomerInfo.genDummyCustomerInfo();
	private UUID origUid = origCi.getCardID();
	
	public void testFileDB(){
		
		IDBAccess db;
		
		try {
			
			db = (IDBAccess) new FileDataBase();
			
			// try to get this ci from file
			CustomerInfo dummy = db.readCustomerInfo(origUid);
			CustomerInfo retCi = db.readCustomerInfo(origUid);
			
			logger.info("dummyCi = {} ", dummy);
			logger.info("retCi = {} ", retCi);
			
			//assertEquals(dummyCi, retCi);
			
			if( dummy.compareTo(retCi) == 0 ){
				logger.info("pass test!!");
			}
			
			testFileDBOperater();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void testFileDBOperater() {
		
		System.setProperty("enable.FileDB", "true");
		
		try {
			
			DataBaseOperations dbOperations = DataBaseOperations.getDatabaseOperations();
			CustomerInfo ci = dbOperations.readCustomerInfo(origUid);
			
			if(ci.getCardID().compareTo(origUid) == 0){
				logger.info("testFileDBOperater pass!");
			} else {
				logger.info("testFileDBOperater fail!");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
