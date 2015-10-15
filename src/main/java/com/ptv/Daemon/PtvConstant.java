/**
 * 
 */
package com.ptv.Daemon;

/**
 * collect all the constant setting here
 * @author Vis.Lee
 *
 */
public interface PtvConstant {

	static public int SUCCESS = 0;
	static public int PTV_ERR_BASE 			= 1000;
	static public int ERR_SQL_CONN 			= -( PTV_ERR_BASE + 1 );
	static public int ERR_GET_PRESENTER 	= -( PTV_ERR_BASE + 2 );
	static public int ERR_INIT_REDERS	 	= -( PTV_ERR_BASE + 3 );
	static public int ERR_INIT_OPERATOR	 	= -( PTV_ERR_BASE + 4 );
	static public int ERR_INIT_DB			= -( PTV_ERR_BASE + 5 );
	
	static public final int UUID_LENGTH = 32;
	
	static public final String PTV_ROOT_DIR 	= System.getProperty("user.dir", ".");
	static public final String PTV_FDB_DIR 		= PTV_ROOT_DIR + "/fdb";
	static public final String PTV_FPRS_DIR		= PTV_ROOT_DIR + "/fprs";

}
