/**
 * 
 */
package com.ptv.Reader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.PtvConstant;
import com.ptv.Reader.NFC.NfcReaderSkeleton;

/**
 * ReadersManager used linked list to record all the readers, because the Manger provide
 * the polling way for threads to read ID from readers.
 * @author Vis.Lee
 *
 */
public class ReadersManager {

	protected static final Logger logger = LogManager.getLogger( ReadersManager.class.getName() );
	
	private static ReadersManager readersManager;
	
	private LinkedList<IDReader> readers;
	
	/*
	 * !! please add your reader in the readers array "manually" if you create one !!
	 */
	private IDReader[] readersArray = { (new NfcReaderSkeleton()) };
	
	
	// disable the initialization of the class
	private ReadersManager(){

	}
	
	
	/*
	 * singleton, only one instance of ReadersManager
	 */
	static public ReadersManager getReadersManager(){
		
		if(readersManager == null){
			
			__initReadersManager();
		}
		
		return readersManager;
		
	}
	
	synchronized private static ReadersManager __initReadersManager(){
		
		if( readersManager == null){
			
			readersManager = new ReadersManager();
			readersManager.initReadersManager();
		}
		
		return readersManager;
	}
	
	static public void terminateReadersManager(){
		
		if(readersManager != null){
			
			__terminateReadersManager();
		}
	}
	
	synchronized private static void __terminateReadersManager() {

		if(readersManager != null){
			
			readersManager.releaseReadersManager();
			readersManager = null;
		}
		
	}

	
	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderManager#initReaderManager()
	 */
	private int initReadersManager() {
		
		// init 
		readers = new LinkedList<IDReader>( Arrays.asList(readersArray) );
		
		int ret = initReaders(readers);
		
		logger.info( "The {} init done!", this.getClass().getSimpleName() );
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderManager#releaseReaderManager()
	 */
	private void releaseReadersManager() {

		releaseReaders();
		
		logger.info( "The {} release done! ", this.getClass().getName() );
		
	}

	/* 
	 * init all the readers record in the 
	 */
	private int initReaders( LinkedList<IDReader> readers) {
		
		// init all readers
		ListIterator<IDReader> iter = readers.listIterator();
		
		while( iter.hasNext() ){
			
			IDReader reader = iter.next();
			
			ReaderState rs = reader.initReaderWorker();
			
			if( rs == ReaderState.WORKER_ALIVE ){
				logger.info( "The reader worker init success for reader \"{}\"", reader.getReaderName() );
			} else {
				logger.error( "The reader worker init failed for reader \"{}\"", reader.getReaderName() );
			}
			
		}
		
		return PtvConstant.SUCCESS;
	}
	
	/*
	 * release all readers
	 */
	private void releaseReaders(){
		
		ListIterator<IDReader> iter = readers.listIterator();
		
		// go thru all readers
		while( iter.hasNext() ){
			
			IDReader reader = iter.next();
			
			// release reader worker
			reader.releaseReaderWorker();
			
		}
		
	}
	
	

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderManager#getReader(java.lang.String)
	 */
	public IDReader getReader(String readerName) {
		
		ListIterator<IDReader> iter = readers.listIterator();
		
		while(iter.hasNext()){
			
			IDReader reader = iter.next();
			
			if( (reader.getReaderName() == readerName) ){
				
				logger.debug("found the readerName {} which matched \"{}\" from caller", reader.getReaderName(), readerName);
				return reader;
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderManager#pollingAllReaders()
	 */
	public LinkedList<UUID> pollingAllReaders() {
		
		ListIterator<IDReader> iter = readers.listIterator();
		LinkedList<UUID> ids = new LinkedList<UUID>();
		
		while(iter.hasNext()){
			
			IDReader reader = iter.next();
			UUID uid = reader.readID();
			
			if( uid != null ){
				
				logger.debug( uid.toString() );
				
				// XXX should we return a list of ID? 
				ids.add(uid);
			}
		}
		
		return ids;
	}


}
