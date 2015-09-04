/**
 * 
 */
package com.ptv.Reader.NFC;

import java.util.UUID;

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
				
				// TODO check the NFC device error code
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
			
			setDevState( ReaderState.DEV_UP );
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
	public UUID readIDFromReader() throws Exception {
		
		UUID uid = null;
		
		// we need to flush out the buffer before we call to JNI
		System.out.flush();
		
		try{
			
			String result = NfcReaderImpl.startPolling();
			
			// parse result
			uid = parseNfcResult(result);
			
		} catch (InterruptedException e) {
			
			if( Thread.interrupted() ){
				
				// TODO check the nfc device state
				logger.error("interrupt catched!!");
				logger.error( e.getMessage() );
			}
			
			// throw new InterruptedException("NFC Device un-plugged!");
			throw e;
		}
		
		return uid;
	}

	private UUID parseNfcResult(String result) {
		/*
		 * the HAL used libnfc which return the following result:
		 *   ISO/IEC 14443A (106 kbps) target:
		 *   ATQA (SENS_RES): 00  04
		 *   UID (NFCID1): 6a  2b  cb  35
		 *   SAK (SEL_RES): 08
		 */
		logger.debug("detect card: \n {}", result.toString());

		UUID uid = null;
		String regex = new String("[:\n]");
		String[] strs = result.split(regex);
		
		// find where the UID is
		for(int i = 0; i < strs.length; i++) {
			
			if(strs[i].contains("UID")){
				
				// we want the string followed UID
				uid = UUID.fromString(strs[i+1]);
				break;
			}
			
		};
		
		return uid;
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
