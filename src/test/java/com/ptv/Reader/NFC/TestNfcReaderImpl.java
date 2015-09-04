/**
 * 
 */
package com.ptv.Reader.NFC;

import java.io.PrintStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.PtvDaemon;
import junit.framework.TestCase;
import thirdparty.LoggingOutputStream;

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
		
		//redirect the STDOUT to our stream
		//System.setErr( new PrintStream( new LoggingOutputStream( LogManager.getLogger(), Level.ERROR ), true) );
		System.setOut( new PrintStream( new LoggingOutputStream( LogManager.getLogger(), Level.INFO ), true) );
		
		//open nfc
		int ret = nfc.openNfcDevice();
		assertEquals(0, ret);
		
		//get reader's name
		String name = nfc.getDeviceName();
		assertNotNull(name);
		System.out.printf("the reader's name = %s", name);
		
		//start polling
		String id = null;
		try {
			id = nfc.startPolling();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(id);
		System.out.printf("the card ID = %s", id);
		
		//stop polling
		nfc.stopPolling();
		
		//close nfc reader
		nfc.closeNfcDevice();
		
		logger.info("end testing");
		
	}
	

}
