package com.ptv.Reader;

/*
 * read info from readers
 */
public interface IDReader {

	
	/*
	 * init reader devices
	 */
	public IDReader initReader() throws Exception;
	public int releaseReader() throws Exception;
	
	public IDReader getReader();
	public long readID();
	
}
