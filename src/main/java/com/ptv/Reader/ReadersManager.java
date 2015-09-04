/**
 * 
 */
package com.ptv.Reader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Daemon.PtvConstant;
import com.ptv.Reader.NFC.NfcReaderSkeleton;

/**
 * ReadersManager used linked list to record all the readers, because the Manger provide
 * the polling way for threads to read ID from readers.
 * @author Vis.Lee
 *
 */
public class ReadersManager implements IDReadersManager {

	protected static final Logger logger = LogManager.getLogger( ReadersManager.class.getName() );
	
	private LinkedList<IDReader> readers;
	
	/*
	 * !! please add your reader in the readers array "manually" if you create one !!
	 */
	private IDReader[] readersArray = { (new NfcReaderSkeleton()) };
	
	
	
	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderManager#initReaderManager()
	 */
	public int initReadersManager() {
		
		// init 
		readers = new LinkedList<IDReader>( Arrays.asList(readersArray) );
		
		int ret = initReaders();
		
		logger.info( "The {} init done! ", this.getClass().getName() );
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderManager#releaseReaderManager()
	 */
	public void releaseReadersManager() {

		releaseReaders();
		
		logger.info( "The {} release done! ", this.getClass().getName() );
		
	}

	/* 
	 * init all the readers record in the 
	 */
	private int initReaders() {
		
		// init all readers
		ListIterator<IDReader> iter = readers.listIterator();
		
		while( iter.hasNext() ){
			
			IDReader reader = iter.next();
			
			ReaderState rs = reader.initReaderWorker();
			
			if( rs == ReaderState.WORKER_ALIVE ){
				logger.info( "The reader worker init success for reader \"{}\"", reader.getReaderName() );
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
	public LinkedList<CustomerInfo> pollingAllReaders() {
		
		ListIterator<IDReader> iter = readers.listIterator();
		LinkedList<CustomerInfo> ids = new LinkedList<CustomerInfo>();
		
		while(iter.hasNext()){
			
			IDReader reader = iter.next();
			CustomerInfo ci = reader.readID();
			
			if( ci != null ){
				
				logger.debug( ci.toString() );
				
				// XXX should we return a list of ID? kind of weird
				ids.add(ci);
			}
		}
		
		return ids;
	}


}
