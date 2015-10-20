/**
 * 
 */
package com.ptv.DB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Daemon.PtvConstant;

/**
 * store the customer info in a file while network is unavailable.
 * !! this interface only for poc, it is not very conscientious code. need to be improved if we want to use it as commercial purpose
 * @author Vis.Lee
 *
 */
public class FileDataBase implements IDBAccess {

	private static final Logger logger = LogManager.getLogger(FileDataBase.class.getName());
	
	private String dbFilesDir;
	private File dbfile;
	
	
	public FileDataBase() throws Exception {
		this(PtvConstant.PTV_FDB_DIR);
	}
	
	public FileDataBase(String dbFilesDir) throws Exception {
		super();
		__init(dbFilesDir);
	}
	
	public int __init(String dbFilesDir) throws Exception{
		
		int retcode = PtvConstant.SUCCESS;
		this.dbFilesDir = dbFilesDir;
		
		createConnection(null, null, null, null);
		
		return retcode;
	}

	/* (non-Javadoc)
	 * @see com.ptv.DB.IDBAccess#createConnection(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createConnection(String driver, String url, String username, String password) throws Exception {
		
		boolean ret = false;
		
		logger.debug("file database dir = {}", dbFilesDir);
		
		dbfile = new File(dbFilesDir);
		
		if(!dbfile.exists()){
			
			// create file
			dbfile.mkdirs();
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.ptv.DB.IDBAccess#releaseConnection()
	 */
	@Override
	public void releaseConnection() {

	}

	/* (non-Javadoc)
	 * @see com.ptv.DB.IDBAccess#getConnection()
	 */
	@Override
	public Connection getConnection() {
		// not supported
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ptv.DB.IDBAccess#readCustomerInfo(java.util.UUID)
	 */
	@Override
	public CustomerInfo readCustomerInfo(UUID uid) throws Exception {
		
		CustomerInfo ci = null;
		
		try {
			
			FileInputStream fis = new FileInputStream( getFilePath(uid) );
			
	        ObjectInputStream ois = new ObjectInputStream(fis);
	        
	        ci = (CustomerInfo)ois.readObject();
	        
	        ois.close();
	        
		} catch (FileNotFoundException e) {
			
			// swallow
			logger.error("can't find the file {} ", uid);
			
			// FIXME write to file, in PoC mode
			ci = CustomerInfo.genDummyCustomerInfo(uid);
			writeCustomerInfo(ci);
		}
		
        return ci;
	}

	/* 
	 * XXX N.B. the files among couldn't exceed 1024 files under 1 dir. we aware this
	 * and ignor it in PoC case.
	 * @see com.ptv.DB.IDBAccess#writeCustomerInfo(com.ptv.Daemon.CustomerInfo)
	 */
	@Override
	public int writeCustomerInfo(CustomerInfo customerInfo) throws Exception {
		
		// create output stream
		FileOutputStream fos = new FileOutputStream( getFilePath(customerInfo.getCardID()) );
		
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
        oos.writeObject(customerInfo);
        
        // flush
        fos.flush();
        fos.close();
        
		return 0;
	}
	
	private String getFilePath(UUID uid){
		
		return dbFilesDir + "/" + uid.toString();
		
	}

}
