
package com.ptv.Reader.NFC;

/**
 * implementation of nfc reader via JNI
 * 
 * @author Vis.Lee
 *
 */
public class NfcReaderImpl {

	static { System.loadLibrary("PtvJni2Nfc"); }
	
    public static native int openNfcReader();
    public static native void closeNfcReader();
    
    public static native String getReaderName();
    public static native String startPolling();
    public static native void stopPolling();
    
    
}
