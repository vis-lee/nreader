/**
 * 
 */
package com.ptv.Presenter;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.DB.FileDataBase;
import com.ptv.Daemon.CustomerInfo;
import com.ptv.Daemon.PtvConstant;

/**
 * @author Vis.Lee
 *
 */
public class FilePresenter implements IPresenter {

	private static final Logger logger = LogManager.getLogger(FileDataBase.class.getName());
	
	private String presenterFilesDir;
	
	public FilePresenter() throws Exception {
		this(PtvConstant.PTV_FPRS_DIR);
	}
	
	public FilePresenter(String presenterFilesDir) throws Exception {
		super();
		__init(presenterFilesDir);
	}
	
	public int __init(String presenterFilesDir) throws Exception{
		
		int retcode = PtvConstant.SUCCESS;
		
		this.presenterFilesDir = presenterFilesDir;
		File prstFileDir = new File(this.presenterFilesDir);
		
		logger.debug("presenter file dir located in = {}", presenterFilesDir);
		
		if(!prstFileDir.exists()){
			
			// create file
			prstFileDir.mkdirs();
		}
		
		return retcode;
	}
	
	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int showPresentation(CustomerInfo ci) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int stopPresentation(CustomerInfo ci) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
