/**
 * 
 */
package com.ptv.Daemon;

import java.util.ArrayList;
import java.util.UUID;

import com.ptv.Geolocation.Location;

/**
 * @author Vis.Lee
 *
 */
public class StoreInfo {
	
	// read from conf file
	private UUID id;
	private int countryCode;
	private String CountryName;
	private Location location;
	private String address;
	private String phoneNumber;
	
	private ArrayList<DisplayInfo> displays;
	
}
