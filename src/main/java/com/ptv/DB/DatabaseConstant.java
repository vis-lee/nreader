package com.ptv.DB;

public interface DatabaseConstant {
	
	public static final String DBDRIVER_NAME	= "net.sourceforge.jtds.jdbc.Driver";
	public static final String SQLSERVER_URI 	= "172.16.1.4:1433";
	public static final String NFC_DATABASE  	= "NFC_TEST";
	public static final String NFC_DEFTABLE  	= "nfc_poc_testing";
	
	public static final String DBURL 			= "jdbc:jtds:sqlserver://"+ SQLSERVER_URI +"/"+ NFC_DATABASE +";instance=SQLEXPRESS";
	
	public static final String USERNAME			= "ptv";
	public static final String PASSWORD			= "pilot!67tv";

}
