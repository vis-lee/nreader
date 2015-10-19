/**
 * 
 */
package com.ptv.Daemon;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.ptv.Geolocation.Location;

/**
 * @author Vis.Lee
 * customer information,
 */
public class CustomerInfo implements Serializable, Comparable<Object> {

	private static final long serialVersionUID = 480515743781027706L;

	private long userID = 0;
	
	//private LinkedList<Long> cardID;
	private UUID cardID;

	private Location location;
	
	// time stamp for last update time
	private long lastTimeStamp = 0;
	
	
	
	
	public CustomerInfo(long userID) {
		this.userID = userID;
	}

	public CustomerInfo(UUID cardId) {
		
		this.cardID = cardId;
		setLastTimeStamp(System.currentTimeMillis());
	}
	
	
	/**
	 * @return the cardID
	 */
	public UUID getCardID() {
		return cardID;
	}
	
	
	/**
	 * @param cardID the cardID to set
	 */
	public void setCardID(UUID cardID) {
		this.cardID = cardID;
		setLastTimeStamp(System.currentTimeMillis());
	}
	
	
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
		setLastTimeStamp(System.currentTimeMillis());
	}
	
	
	
	/**
	 * @return the lastTimeStamp
	 */
	public long getLastTimeStamp() {
		return lastTimeStamp;
	}
	
	
	/**
	 * @param lastTimeStamp the lastTimeStamp to set
	 */
	public void setLastTimeStamp(long lastTimeStamp) {
		this.lastTimeStamp = lastTimeStamp;
	}
	
	
	public String getLastTimeStampInDateString(){
		
		Date date = new Date(lastTimeStamp);
		
		SimpleDateFormat sdate = new SimpleDateFormat();
		
		return sdate.format(date);
		
	}
	
	
	static public CustomerInfo genDummyCustomerInfo(){
		
		Random rn = new Random();
		CustomerInfo ci = new CustomerInfo( rn.nextLong() );
		ci.setCardID(new UUID( (1L << 64), rn.nextLong()));
		ci.setLocation(Location.getDummyLocation());
		
		return ci;
		
	}
	
	static public CustomerInfo genDummyCustomerInfo(UUID uid){
		
		Random rn = new Random();
		CustomerInfo ci = new CustomerInfo( rn.nextLong() );
		ci.setCardID( uid );
		ci.setLocation(Location.getDummyLocation());
		
		return ci;
		
	}
	
	/*
	 * FIXME need to complete it
	 */
	public int update(CustomerInfo ci){
		
		int retcode = PtvConstant.SUCCESS;
		
		setLastTimeStamp(System.currentTimeMillis());
		
		return retcode;
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return String.format( "userID = %d, cardID = %s, location = %s, TimeStamp = %d, in Date = %s",
				               userID, (cardID != null) ? cardID.toString() : "no cardID", 
				            	(location != null) ? location : "unknow", 
				            	getLastTimeStamp(),
				            	getLastTimeStampInDateString() );
		
	}

	@Override
	public int compareTo(Object o) throws ClassCastException {
		
		if ( !(o instanceof CustomerInfo) ){
			throw new ClassCastException();
		}
		
		CustomerInfo cci = (CustomerInfo) o;
		
		if( this.userID == cci.userID && 
			this.cardID.compareTo( cci.cardID ) == 0 ){
			
			return 0;
		}
		
		return (this.userID > cci.userID) ? 1 : -1;
	}
	
	
}
