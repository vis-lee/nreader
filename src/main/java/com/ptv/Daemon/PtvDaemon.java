package com.ptv.Daemon;


import org.apache.logging.log4j.Logger;

import com.ptv.Reader.ReadersManager;

import org.apache.logging.log4j.LogManager;

/**
 * Hello!
 *
 * @author Vis.Lee
 */
public class PtvDaemon 
{
	
    // Define a static logger variable so that it references the
    // Logger instance named "PtvDaemon".
    private static final Logger logger = LogManager.getLogger(PtvDaemon.class);
    
    protected volatile boolean ptvDaemonState = false;
    
    protected PtvDaemon ptvDaemon;
    
    protected ReadersManager readersManager;
    
    Thread shutdownhook = new PtvShutdownHook();
    
    public int initDaemon(PtvDaemon ptvDaemon) {
		
		// wakeup
		logger.debug(" ptv daemon start! ");
		
		this.ptvDaemon = ptvDaemon;
		ptvDaemonState = true;
		
		// init rest
		
		
		// init reader manager
		readersManager = ReadersManager.getReadersManager();
		
		
		// register shutdown hook
		Runtime.getRuntime().addShutdownHook( shutdownhook );
		
		return 0;
	}
	
	public int exitDaemon() {
	
		// set the state to DOWN
		ptvDaemonState = false;
		
		// run the shutdown hooks
		// 1. shutdown the reader manager
		ReadersManager.terminateReadersManager();
		
		// ALL end
		logger.info(" ptv daemon exit! ");
		
		return 0;
	}
	

	/*
	 * shutdown hook thread
	 */
	public class PtvShutdownHook extends Thread {

		public void run() {
			
			// run through the shutdown hooks
			exitDaemon();
			
		}
		
	}
	
	
    synchronized public static void main( String[] args )
    {
    	// create the instance of ptv Daemon
    	PtvDaemon ptvDaemon = new PtvDaemon();
    	
    	ptvDaemon.initDaemon( ptvDaemon );
        
    }

	
	
}
