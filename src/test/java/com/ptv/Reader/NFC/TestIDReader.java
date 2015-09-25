/**
 * 
 */
package com.ptv.Reader.NFC;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Reader.IDReader;
import com.ptv.Reader.ReaderState;

import junit.framework.TestCase;

/**
 * @author Vis.Lee
 *
 */
public class TestIDReader extends TestCase {

	private static final Logger logger = LogManager.getLogger(TestIDReader.class.getName());
	
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

		UUID uid = null;
		
		while( (uid == null) || true ){
			
			uid = reader.readID();
			
			if(uid == null){
				try {
					TimeUnit.SECONDS.sleep(1);
					
				} catch (InterruptedException e) {
					
					System.out.println( ((NfcReaderSkeleton) reader).getReaderState().toString() );
					
				}
			} else {
				logger.info(uid.toString());
			}
		}
		
		String deviceName = ((NfcReaderSkeleton) reader).getDeviceName();
		
		logger.info("the device name is {}", deviceName == null ? deviceName : "no device" );
		
		reader.releaseReaderWorker();
		
		logger.info("test finished!");
		
	}
	
}
