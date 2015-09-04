/**
 * 
 */
package com.ptv.Reader.NFC;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Reader.IDReader;
import com.ptv.Reader.ReaderState;

import junit.framework.TestCase;

/**
 * @author Vis.Lee
 *
 */
public class TestIDReader extends TestCase {

	private static final Logger logger = LogManager.getLogger();
	
	public void testIDReaderFunc(){
		
		// init nfc reader
		IDReader reader = new NfcReaderSkeleton();
		
		ReaderState rs = reader.initReaderWorker();
		
		if( rs == ReaderState.WORKER_ALIVE ){
			logger.info("the worker init success!");
		} else {
			logger.info("the worker init fail!");
			return;
		}

		CustomerInfo ci = null;
		
		while( ci == null ){
			
			ci = reader.readID();
			
			if(ci == null){
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.info(ci.toString());
			}
		}
		
		String deviceName = ((NfcReaderSkeleton) reader).getDeviceName();
		
		logger.info("the device name is {}", deviceName == null ? deviceName : "no device" );
		
		reader.releaseReaderWorker();
		
		logger.info("test finished!");
		
	}
	
}
