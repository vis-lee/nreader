/**
 * 
 */
package com.ptv.Reader;

import java.util.UUID;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;

/**
 * @author Vis.Lee
 *
 */
public class SynchronousBroker {
	
	
	protected static final Logger logger = LogManager.getLogger( SynchronousBroker.class.getName() );
	
	/*
	 * We use SynchronousQueue to restrict only one ID at once.
	 * The producer would wait until the consumer took the request from it.
	 * So, SynchronousQueue doesn't have any capacity, it's a hand over class. 
	 */
	public SynchronousQueue<UUID> queue;

	
	public SynchronousBroker(){
		this.queue = new SynchronousQueue<UUID>();
	}
	
	
	public boolean offer(UUID uid){
		
		logger.debug( "thread = {}, offer ID = {} to broker", Thread.currentThread().getName(), uid );
		return this.queue.offer(uid);
		
	}
	
	public boolean offer(UUID uid, long producerWaitTime, TimeUnit tu) throws InterruptedException{
		
		logger.debug( "thread = {}, offer ID = {} to broker with timer set to {} {} ", Thread.currentThread().getName(), uid, producerWaitTime, tu.toString() );
		return this.queue.offer(uid, producerWaitTime, tu);
		
	}
	
	public UUID poll(){
		
		logger.debug( "thread = {}, poll from broker ", Thread.currentThread().getName() );
		return this.queue.poll();
		
	}


	public UUID poll(long consumerPollTime, TimeUnit tu) {

		try {
			
			if(logger.isDebugEnabled()){
				// modify the time if debug mode enabled
				consumerPollTime = 1;
				tu = TimeUnit.SECONDS;
			}
			
			logger.debug( "thread = {}, poll from broker with timer set to {} {} ", Thread.currentThread().getName(), consumerPollTime, tu.toString() );
			return this.queue.poll(consumerPollTime, tu);
			
		} catch (InterruptedException e) {
			
			// swallow...
			return null;
			
		}
		
	}
	
	
}
