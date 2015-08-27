/**
 * 
 */
package com.ptv.Reader.NFC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;

/**
 * @author Vis.Lee
 *
 */
public class TestNfcReaderImpl extends TestCase {

	private static final Logger logger = LogManager.getLogger();
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testNfcReader(){
		
		logger.info("start testing...");
		
		NfcReaderImpl nfc = new NfcReaderImpl();
		
		//open nfc
		int ret = nfc.openNfcReader();
		assertEquals(0, ret);
		
		//get reader's name
		String name = nfc.getReaderName();
		assertNotNull(name);
		System.out.printf("the reader's name = %s", name);
		
		//start polling
		String id = nfc.startPolling();
		assertNotNull(id);
		System.out.printf("the card ID = %s", id);
		
		//stop polling
		nfc.stopPolling();
		
		//close nfc reader
		nfc.closeNfcReader();
		
		logger.info("end testing");
		
	}
	

}
