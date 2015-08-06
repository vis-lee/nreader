package com.ptv.Reader;

public class NoReader extends Exception {
	
	public NoReader() {
		
		// can't find any reader devices
		new Throwable("can't find any reader devices!");
		
	}
}
