package com.ptv.Daemon;


import org.apache.logging.log4j.Logger;

import com.ptv.Reader.ReadersManager;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

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
    private static final Logger logger = LogManager.getLogger(PtvDaemon.class.getName());
    
    protected volatile boolean ptvDaemonState = false;
    
    protected PtvDaemon ptvDaemon;
    private PtvDaemonOperations operations;
    
    Thread shutdownhook = new PtvShutdownHook();
    
    /**
	 * @return the ptvDaemonState
	 */
	public boolean getPtvDaemonState() {
		return ptvDaemonState;
	}

	
	public int doDaemonRoutines(){
		
		int retcode = PtvConstant.SUCCESS;
		
		operations.goOperations();
		
		return retcode;
	}
	

	public int initDaemon(PtvDaemon ptvDaemon) {
		
		int retcode = 0;
		
		// wakeup
		logger.debug(" ptv daemon start! ");
		
		this.ptvDaemon = ptvDaemon;
		
		// init
		operations = new PtvDaemonOperations(ptvDaemon);
		
		if( (retcode = operations.initOperations()) < 0){
			return retcode;
		}
		
		// register shutdown hook
		Runtime.getRuntime().addShutdownHook( shutdownhook );
		
		// go!
		ptvDaemonState = true;
		
		return retcode;
	}
	
	public int exitDaemon() {
	
		// set the state to DOWN
		ptvDaemonState = false;
		
		// run the shutdown hooks
		// 1. shutdown the reader manager
		operations.exitOperations();
		
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
	
	
	/*
	 * Daemon main loop
	 */
    synchronized public static void main( String[] args )
    {
    	// create the instance of ptv Daemon
    	PtvDaemon ptvDaemon = new PtvDaemon();
    	
    	ptvDaemon.initDaemon( ptvDaemon );
    	
    	while(ptvDaemon.getPtvDaemonState()){
    		
    		//TODO main loop routines
    		ptvDaemon.doDaemonRoutines();
    		
    		try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	}
        
    }

	
	
}
