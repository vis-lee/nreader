package com.ptv.Reader;

import java.util.LinkedList;

import com.ptv.Daemon.CustomerInfo;

/*
 * read info from readers
 */
public interface IDReadersManager {
	
	/*
	 * initial function and release function
	 */
	// public int initReadersManager();
	// public void releaseReadersManager();
	public IDReadersManager getReadersManager();
	
	/*
	 * get the reference of Java IDReader instance by name
	 */
	public IDReader getReader(String readerName);
	
	/*
	 * polling all the readers that registered in this manager
	 */
	public LinkedList<CustomerInfo> pollingAllReaders();
	
	
}
