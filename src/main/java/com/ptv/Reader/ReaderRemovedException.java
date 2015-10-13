/**
 * 
 */
package com.ptv.Reader;

/**
 * @author Vis.Lee
 *
 */
public class ReaderRemovedException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6922280503292759404L;

	public ReaderRemovedException(String msg, Throwable e){
		
		super(msg, e);
	}
	
	public ReaderRemovedException(Throwable e){
		
		super(e);
	}
	
	public ReaderRemovedException(String s){
		
		super(s);
	}
	
	public Throwable getCause(){
		return super.getCause();
	}
	
	public Throwable initCause(Throwable cause){
		return super.initCause(cause);
	}


}
