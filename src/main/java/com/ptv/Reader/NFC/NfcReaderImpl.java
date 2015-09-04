
package com.ptv.Reader.NFC;

/**
 * implementation of nfc reader via JNI
 * 
 * @author Vis.Lee
 *
 */
public class NfcReaderImpl {

	static { System.loadLibrary("PtvJni2Nfc"); }
	
	/*
	 * c interface, call to the libnfc
	 */
    public static native int openNfcDevice();
    public static native void closeNfcDevice();
    
    public static native String getDeviceName();
    public static native String startPolling() throws Exception;
    public static native void stopPolling();
    
    
}
