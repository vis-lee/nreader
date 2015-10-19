/**
 * 
 */
package com.ptv.Geolocation;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Vis.Lee
 *
 */
public class Location implements Serializable, Comparable<Location> {
	
	private static final long serialVersionUID = -5619172989718687595L;
	
	// TODO we should load this kind of common information from Database 
	public static final Location[] dummyLocations = { 
			new Location("TW", 25.079141, 121.5670869),		// Taiwan
			new Location("US", 37.4028036, -122.0410981),	// United States
			new Location("JP", 35.0061, 135.76095),			// Japan
			new Location("TH", 13.7246005, 100.6331108),	// Thailand
			new Location("DE", 51.1719674, 10.4541194),		// Germen
			new Location("FR", 46.2157467, 2.2088258),		// France
			new Location("ES", 40.2085, -3.713),			// Spain
			new Location("PH", 14.5980689, 120.9446316),	// Philippines
	};
	
	
	// TODO implement the array map { (886, "TW") } later...
	static public final String PTV_REGION = "TW";
	static public final double PTV_LAT = 25.079141;
	static public final double PTV_LNG = 121.5670869;
	
	// province or city
	private String region;
	
	private Double longitude;
	private Double latitude;
	
	
	public Location(String region, Double longitude, Double latitude) {
		super();
		this.region = region;
		this.longitude = longitude;
		this.latitude = latitude;
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
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = Double.valueOf(longitude);
	}
	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = Double.valueOf(latitude);
	}
	
	static public Location getDummyLocation(){
		
		Random rn = new Random(System.currentTimeMillis());
		Location loc = dummyLocations[ (rn.nextInt(Integer.MAX_VALUE) % dummyLocations.length) ];
		return new Location(loc.getRegion(), loc.getLongitude(), loc.getLatitude());
		
	}
	
//	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
//		out.defaultWriteObject( );
//	}
// 
//	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
//		in.defaultReadObject( );
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Location [region=" + region + ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	
	
	@Override
	public boolean equals(Object obj) {

		int retcode;
		
		if( obj instanceof Location){
			
			retcode = compareTo((Location) obj);
			
			return (retcode == 0) ? true : false;
		}
		
		return super.equals(obj);
	}
	
	
	@Override
	public int compareTo(Location o) {
	
		// we only compare the region
		if( this.getRegion().compareTo(o.getRegion()) == 0 ){
			return 0;
		}
		
		// FIXME the longitude and latitude should have some other comparison method.
		
		return (this.getLatitude() > o.getLatitude()) ? 1:-1;
	}
	

}
