package com.ptv.Daemon;


import org.apache.logging.log4j.Logger;

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
    private String PtvDaemonName = PtvDaemon.class.getSimpleName();
    
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
		
		Thread.currentThread().setName(PtvDaemonName);
		
		// wakeup
		logger.debug("### {} is starting!! ###", PtvDaemonName);
		

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
		logger.info("### {} terminated completely!! ###", Thread.currentThread().getName());
		
		return 0;
	}
	

	/*
	 * shutdown hook thread
	 */
	public class PtvShutdownHook extends Thread {

		private String name = "PtvShutdownWorker";
		
		public void run() {
			
			Thread.currentThread().setName(name);
			logger.debug("### {} start to execute shutdown hook...", Thread.currentThread().getName());
			
			// run through the shutdown hooks
			exitDaemon();
			
			logger.info("### {} shutdown completed!! ###", Thread.currentThread().getName());
			
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
    		
    		// main loop routines
    		ptvDaemon.doDaemonRoutines();
    		
    		
    	}
    	
    }

	
	
}
