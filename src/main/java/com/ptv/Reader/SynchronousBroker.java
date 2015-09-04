/**
 * 
 */
package com.ptv.Reader;

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
	
	
	protected static final Logger logger = LogManager.getLogger( AbstractReader.class.getName() );
	
	/*
	 * We use SynchronousQueue to restrict only one ID at once.
	 * The producer would wait until the consumer took the request from it.
	 * So, SynchronousQueue doesn't have any capacity, it's a hand over class. 
	 */
	public SynchronousQueue<CustomerInfo> queue;

	
	public SynchronousBroker(){
		this.queue = new SynchronousQueue<CustomerInfo>();
	}
	
	
	public boolean offer(CustomerInfo ci){
		
		logger.debug( "thread = {}, offer ID = {} to broker", Thread.currentThread().getName(), ci.getCardID() );
		return this.queue.offer(ci);
		
	}
	
	public CustomerInfo poll(){
		
		logger.debug( "thread = {}, poll from broker ", Thread.currentThread().getName() );
		return this.queue.poll();
		
	}


	public CustomerInfo poll(long consumerPollTime, TimeUnit seconds) {

		try {
			
			logger.debug( "thread = {}, poll from broker with timer set to {} seconds ", Thread.currentThread().getName(), consumerPollTime );
			return this.queue.poll(consumerPollTime, seconds);
			
		} catch (InterruptedException e) {
			
			return null;
			
		}
		
	}
	
	
}
