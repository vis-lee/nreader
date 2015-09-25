/**
 * 
 */
package com.ptv.Geolocation;

import java.io.Serializable;

/**
 * @author Vis.Lee
 *
 */
public class Location implements Serializable {
	
	private static final long serialVersionUID = -5619172989718687595L;
	
	// TODO used the array map { (886, "TW") }...
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
		
		return new Location(PTV_REGION, PTV_LNG, PTV_LAT);
		
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
	

}
