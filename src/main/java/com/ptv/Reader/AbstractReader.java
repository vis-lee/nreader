/**
 * 
 */
package com.ptv.Reader;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;

/**
 * @author Vis.Lee
 *
 * All kinds of the readers implementation needs to inherent this Abstract class. A middle layer
 * of Synchronous Broker used to sync the behavior between ptv daemon and polling worker thread.
 * So, the ptv daemon just need to init the reader and readID from it's point of view, no matter 
 * which kind of readers. 
 * 
 */
public abstract class AbstractReader extends Thread implements IDReader, IDReaderDevice{

	
	protected static final Logger logger = LogManager.getLogger( AbstractReader.class.getName() );
	
	public final static long CONSUMER_POLL_TIME = 1;  // second
	
	public final static long WAIT_TIMER = 3; //seconds
	
//	protected volatile IDReader reader;

	// a flag to indicate worker to keep working or not
	protected ReaderState alive = ReaderState.WORKER_DOWN;
	
	public String name;
	
	/*
	 * 1. create a broker
	 */
	protected SynchronousBroker broker;
	
	/*
	 * 2. create a thread to poll the reader
	 */
	protected ReaderWorker readerWorker;
	
	


	//******************************************************************************************//
	//																							//
	//								implements of PollingWorker									//
	//																							//
	//******************************************************************************************//
	
	/**
	 * This thread is responsible for polling the reader and offer the grabbed ID to ptv daemon
	 * @author Vis.Lee
	 *
	 */
	public class ReaderWorker extends Thread{
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			
			logger.info("the {} is starting", AbstractReader.class.getSimpleName());
			
			while( alive == ReaderState.WORKER_ALIVE ){
				
				if( getReaderState() == ReaderState.DEV_UP ){
					
					try {
						
						// 2. polling
						CustomerInfo ci = new CustomerInfo( readIDFromReader() );
						
						// 3. return the cardID to broker
						broker.offer(ci);
						
					} catch (Exception e) {
						
						// if the interrupt caused by the device been detached accidentally or jvm dispose
						if( Thread.interrupted() ){
							
							if( getReaderState() == ReaderState.DEV_DOWN ){
							
								// TODO if the device isn't up, clear my state
								logger.warn("received interrupt and DEV_DOWN. Trying to init device again!");
							}
							
							// interrupted by dispose function
							if( alive == ReaderState.WORKER_DOWN ){
								
								logger.debug("received interrupt and WORKER_DOWN, shoule be called from dispose function!");
							}
						}
					}
					
				} else { // if( getReaderState() == ReaderState.DEV_UP )
					
					// 1. init device
					__initReader();
				}
				
			} // end of while( alive)
			
			logger.info("the {} is exiting", AbstractReader.class.getSimpleName());
			
			super.run();
		}


		
	}
	
	/* 
	 * the inherited classes needs to implement the initialize device function
	 * Does this function need singleton here? not really.
	 */
	private ReaderState __initReader(){
		
		ReaderState rs = null;
		
		// init device, singleton
		while( getReaderState() == ReaderState.DEV_DOWN ){
			
			try {
				
				// try to init device
				rs = initReader();
				
				if( rs == ReaderState.DEV_DOWN ){
					
					// time out interrupt
					logger.info("No Reader detected");
					
					// sleep WAIT_TIMER
					TimeUnit.SECONDS.sleep(WAIT_TIMER);
				}

				
			} catch (Exception e) {
				
				if(logger.isDebugEnabled()){
					
					logger.debug( e.getStackTrace() );
				}
				
				if (Thread.interrupted()){
					
					/*
					 *  1. if it's timer fired, do nothing
					 *  2. what if the device be detached accidentally? no care here.
					 */
					
				}
			}
		}
		
		return rs;
		
	}


	/* 
	 * the inherited classes needs to implement the release device function
	 */
	private void __releaseReader() throws Exception {
		
		if( getReaderState() != ReaderState.DEV_DOWN ){
			
			// close device 
			releaseReader();
		}
		
	}


	//******************************************************************************************//
	//																							//
	//									implements of IDReader									//
	//																							//
	//******************************************************************************************//
	
	
	/*
	 * 3. read function for the caller
	 */
	public CustomerInfo readID() {
		
		// read ID from broker
		// return broker.poll(CONSUMER_POLL_TIME, TimeUnit.SECONDS);
		return broker.poll();
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReader#initReaderWorker()
	 * 
	 * init the reader worker
	 * 
	 */
	public ReaderState initReaderWorker() {
		
		this.broker = new SynchronousBroker();
		
		this.readerWorker = new ReaderWorker();
		
		if( readerWorker != null ){
			
			this.alive = ReaderState.WORKER_ALIVE;
			
			this.readerWorker.start();
		}
		
		// return the reader instance, no matter the device is up or not.
		return this.alive;
	}


	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReader#releaseReaderWorker()
	 * 
	 * release the reader worker and the device.
	 * 
	 */
	public void releaseReaderWorker() {

		try {
			
			// terminate the worker first
			__releaseWorker();
			
			// release the reader device
			__releaseReader();
			
		} catch (Exception e) {
			
			logger.debug( e.getMessage() );
		}
		
	}


	/**
	 * release the reader worker by setting the alive flag to WORKER_DOWN, 
	 * also sending the interrupt signal to the worker and join(wait) for finish.
	 * 
	 * @throws InterruptedException
	 */
	private void __releaseWorker() throws InterruptedException {
		
		if( readerWorker != null ){

			// stop the worker
			alive = ReaderState.WORKER_DOWN;
			
			if( readerWorker.getState() == Thread.State.WAITING ){
				
				// interrupt the thread to go to exit
				readerWorker.interrupt();
			}
			
			// wait worker to finish
			readerWorker.join();
			
			readerWorker = null;
			
			logger.info( " the {} has been terminated! ", Thread.currentThread().getName() );
			
		}
		
		
	}
	

	
	
	//******************************************************************************************//
	//																							//
	//									Abstract functions										//
	//																							//
	//******************************************************************************************//

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderDevice#initReader()
	 * 
	 * !! need to implement singleton for init !!
	 * Because the polling worker will keep trying to init 
	 * the device until the device is presented and up.
	 */
	abstract public ReaderState initReader() throws Exception;

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderDevice#releaseReader()
	 * !! need to implement singleton for release !!
	 */
	abstract public void releaseReader() throws Exception;

	/* (non-Javadoc)
	 * @see com.ptv.Reader.IDReaderDevice#readIDFromReader()
	 */
	abstract public UUID readIDFromReader() throws Exception;

	// TODO REMOVE THIS
//	/*
//	 * get reader reference for operations. The difference 
//	 * between initReader and getReader is that getReader 
//	 * return the reader's reference without thinking the 
//	 * state of device is up or not.
//	 */
//	//abstract public IDReader getReader();
	
	/*
	 * @return ReaderName
	 */
	abstract public String getReaderName();
	
	/*
	 * get the device state
	 */
	abstract public ReaderState getReaderState();
	
}
