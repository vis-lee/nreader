/**
 * 
 */
package com.ptv.Reader.NFC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Reader.AbstractReader;
import com.ptv.Reader.ReaderRemovedException;
import com.ptv.Reader.ReaderState;

/**
 * The NFC reader skeleton layer for implementing basic nfc operations
 * @author Vis.Lee
 *
 */
public class NfcReaderSkeleton extends AbstractReader {

	private static final Logger logger = LogManager.getLogger( AbstractReader.class.getName() );
	
	protected ReaderState devState = ReaderState.DEV_DOWN;
	
	// default reader name, nfc.
	private String readerName = new String("nfc");
	
	// default hardware name
	private String deviceName = null;
	
	
	/**
	 * @return the devState
	 */
	synchronized public ReaderState getDevState() {
		return devState;
	}

	/**
	 * @param devState the devState to set
	 */
	synchronized public void setDevState(ReaderState devState) {
		this.devState = devState;
	}

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReader#initReader()
	 * Singleton init function.
	 */
	@Override
	public ReaderState initReader() throws Exception {
		
		logger.debug("try to init reader device...");
		
		// we need to flush out the buffer before we call to JNI
		System.out.flush();
		
		ReaderState rs = getDevState();
		
		if( rs == ReaderState.DEV_DOWN ){
			
			rs = __initReader();
		}
		
		return rs;
	}
	
	synchronized private ReaderState __initReader() throws Exception {

		ReaderState rs = getDevState();
		
		if( rs == ReaderState.DEV_DOWN ){

			//open nfc
			int ret = NfcReaderImpl.openNfcDevice();
			
			if( ret == 0 ){
				
				rs = ReaderState.DEV_UP;
				setDevState(rs);
				
				//setup device name
				deviceName = NfcReaderImpl.getDeviceName();
				
				logger.info("init device success, device name = {}", deviceName);
				
			} else {
				
				// check the NFC device error code
				logger.error("openNfcDevice returned non-0 code, code = {}", ret);
			}
		}
		
		return rs;

	}

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReader#releaseReader()
	 */
	@Override
	synchronized public void releaseReader() throws Exception {
		
		// we need to flush out the buffer before we call to JNI
		System.out.flush();
		
		ReaderState rs = getDevState();
		
		if( rs == ReaderState.DEV_UP ){
			
			logger.debug("ReaderState is DEV_UP, release it...");
			__releaseReader();
		}
		
	}
	
	synchronized private void __releaseReader() throws Exception {

		ReaderState rs = getDevState();
		
		if( rs == ReaderState.DEV_UP ) {
			logger.debug("call to libnfc to release reader...");
			// call libnfc close
			NfcReaderImpl.closeNfcDevice();
			logger.debug("reader closed!");
			setDevState( ReaderState.DEV_DOWN );
		}
		
	}

//	/* (non-Javadoc)
////	 * @see com.ptv.Reader.IDReader#getReader()
//	 */
//	public IDReader getReader() {
//		
//		return nfc;
//	}

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReader#readID()
	 */
	@Override
	public String readIDFromReader() throws Exception {
		
		String id = null;
		
		// we need to flush out the buffer before we call to JNI
		System.out.flush();
		
		try{
			
			String result = NfcReaderImpl.startPolling();
			
			// parse the result
			id = parseNfcResult(result);
			
		} catch (InterruptedException e) {
			
			logger.error("interrupt catched!!", e);
			
			throw e;
			
		} catch ( ReaderRemovedException e ) {
			
			// set DEV_DOWN
			setDevState(ReaderState.DEV_DOWN);
			logger.error("NFC Device un-plugged!", e);
			throw e;
		}
		
		return id;
	}
	
	public void abortFromReader(){
		
		NfcReaderImpl.stopPolling();
	}

	/*
	 * the HAL used libnfc which return the following result:
	 *   ISO/IEC 14443A (106 kbps) target:
	 *   ATQA (SENS_RES): 00  04
	 *   UID (NFCID1): 6a  2b  cb  35
	 *   SAK (SEL_RES): 08
	 */
	private String parseNfcResult(String result) {
		
		String extract = null;
		
		if (result != null){

			String regex = new String("[:\n]");
			String[] strs = result.split(regex);
			
			logger.debug("detect card: \n {}", result.toString());
			// find where the UID is
			for(int i = 0; i < strs.length; i++) {
				
				if(strs[i].contains("UID")){
					
					// we want the string followed UID
					extract = strs[i+1];
					
					// replace the whitespace and \n 
					extract = extract.replaceAll("\\s+", "");
					
					break;
				}
			}
		}
		
		return extract;
	}
	

	@Override
	public ReaderState getReaderState() {
		
		// return the device state
		return devState;
	}

	@Override
	public String getReaderName() {

		return readerName;
	}
	
	public String getDeviceName() {
		
		return deviceName;
	}

}
