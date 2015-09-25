/**
 * 
 */
package com.ptv.Reader;

/**
 * @author Vis.Lee
 *
 */
public class ReaderRemovedException extends Exception {
	
	
	ReaderRemovedException(String msg, Throwable e){
		
		super(msg, e);
	}
	
	ReaderRemovedException(Throwable e){
		
		super(e);
	}
	
	
	public Throwable getCause(){
		return super.getCause();
	}
	
	public Throwable initCause(Throwable cause){
		return super.initCause(cause);
	}


}
