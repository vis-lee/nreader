package com.ptv.Reader;

/**
 * This is the device control APIs layer for a ID reader. Need to implement the code for device 
 * HAL control.
 * @author Vis.Lee
 *
 */
public interface IDReaderDevice {

	//******************************************************************************************//
	//																							//
	//								constants and error code									//
	//																							//
	//******************************************************************************************//
		
		
	final static int NO_DEVICE = -4; // map to linux error code, NOSUCHDEV
	
	/* 
	 * init device and return the reference of Java IDReader instance
	 */
	public ReaderState initReader() throws Exception;
	
	/*
	 * release the context and device
	 */
	public void releaseReader() throws Exception;
	
	/*
	 * read ID from device
	 */
	public String readIDFromReader() throws Exception;
	
}
