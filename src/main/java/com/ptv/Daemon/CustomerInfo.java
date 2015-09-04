/**
 * 
 */
package com.ptv.Daemon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Vis.Lee
 * customer information,
 */
public class CustomerInfo {

	private long userID;
	//private LinkedList<Long> cardID;
	private UUID cardID;
	private String region;
	private String location;
	private long lastTimeStamp;
	
	
	public CustomerInfo(UUID cardId) {
		
		this.cardID = cardId;
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
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
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
		
		Date date = new Date(lastTimeStamp * 1000);
		
		SimpleDateFormat sdate = new SimpleDateFormat();
		
		return sdate.format(date);
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return String.format( "userID = {}, cardID = {}, region = {}, location = {}, TimeStamp = {}",
				               userID, cardID, region, location, getLastTimeStampInDateString() );
		
	}
	
	
	
	
}
