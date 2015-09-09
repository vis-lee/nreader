/**
 * 
 */
package com.ptv.Reader.NFC;

import com.ptv.Reader.AbstractReader;
import com.ptv.Reader.ReaderState;

/**
 * The NFC reader skeleton layer for implementing basic nfc operations
 * @author Vis.Lee
 *
 */
public class NfcReaderSkeleton extends AbstractReader {

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
			
			__releaseReader();
		}
		
	}
	
	synchronized private void __releaseReader() throws Exception {

		ReaderState rs = getDevState();
		
		if( rs == ReaderState.DEV_UP ) {
			
			// call libnfc close
			NfcReaderImpl.closeNfcDevice();
			
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
			
			if( Thread.interrupted() ){
				
				// TODO check the nfc device state
				logger.error("interrupt catched!!");
				logger.error( e.getMessage() );
			}
			
			// throw new InterruptedException("NFC Device un-plugged!");
			throw e;
		}
		
		return id;
	}

	private String parseNfcResult(String result) {
		/*
		 * the HAL used libnfc which return the following result:
		 *   ISO/IEC 14443A (106 kbps) target:
		 *   ATQA (SENS_RES): 00  04
		 *   UID (NFCID1): 6a  2b  cb  35
		 *   SAK (SEL_RES): 08
		 */
		logger.debug("detect card: \n {}", result.toString());

		String regex = new String("[:\n]");
		String[] strs = result.split(regex);
		String extract = null;
		
		// find where the UID is
		for(int i = 0; i < strs.length; i++) {
			
			if(strs[i].contains("UID")){
				
				// we want the string followed UID
				extract = strs[i+1];
				
				// replace the whitespace and \n 
				extract = extract.replaceAll("\\s+", "");
				
				break;
			}
			
		};
		
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
