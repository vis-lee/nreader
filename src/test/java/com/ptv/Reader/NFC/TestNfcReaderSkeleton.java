package com.ptv.Reader.NFC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;

public class TestNfcReaderSkeleton extends TestCase {

	protected static final Logger logger = LogManager.getLogger( TestNfcReaderSkeleton.class.getName() );
	public void testNfcReaderSkeleton(){
		
		NfcReaderSkeleton nfc = new NfcReaderSkeleton();
		
		try {
			
			nfc.initReader();
			
			String id = nfc.readIDFromReader();
			
			logger.info("ci = {}", (id == null) ? "NULL" : id.toString() );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
}
