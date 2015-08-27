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
    private static final Logger logger = LogManager.getLogger(PtvDaemon.class);
    
    public static void main( String[] args )
    {
        
        int retcode = initDaemon();
        
    }

    public static int initDaemon() {
		
		// wakeup
		logger.debug(" ptv daemon start! ");
		
		// init rest
		
		return 0;
	}
	
	public static int exitDaemon() {
		
		// ALL end
		logger.info(" ptv daemon exit! ");
		
		return 0;
	}
}
