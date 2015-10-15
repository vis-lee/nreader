package com.ptv.Daemon;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.DB.DataBaseOperations;
import com.ptv.DB.PtvMSSqlServer;
import com.ptv.Presenter.IPresenter;
import com.ptv.Presenter.WebpagePresenter;
import com.ptv.Reader.ReadersManager;

public class PtvDaemonOperations implements PtvConstant{
	
	private static final Logger logger = LogManager.getLogger(PtvDaemon.class.getName());
	
	protected ReadersManager readersManager;
	protected DataBaseOperations dbOperations;
    
	private PtvDaemon ptvDaemon = null;
	private IPresenter presenter = null;
	
	
	public PtvDaemonOperations(PtvDaemon ptvDaemon) {
		
		this.ptvDaemon = ptvDaemon;
		this.presenter = WebpagePresenter.getPresenter();
	}


	public int goOperations() {
		
		int retcode = PtvConstant.SUCCESS;
		
		// id operations
		LinkedList<UUID> ids = readersManager.pollingAllReaders();
		
		// DB operations
		CustomerInfo ci = null;
		
		try {
			
			for( Iterator<UUID> iter = ids.iterator(); iter.hasNext(); ){
				
				UUID uid = iter.next();
				
				ci = dbOperations.readCustomerInfo(uid);
				
				// TODO perform operations by operationEnum
				presenter.showPresentation(ci);
				
			}
			
		} catch (Exception e) {
			
			logger.error("getCustomerInfoFromDataBase exception!");
		}
		
		return retcode;
	}
	

	public int initOperations() {
		
		// init all components
		try {
			dbOperations = DataBaseOperations.getDatabaseOperations();
		} catch (Exception e) {
			logger.error("initOperations : ERR_INIT_DB", e);
			return ERR_INIT_DB;
		} 
		
		
		logger.info("initOperations : SQL ready");
		
		// get presenter TODO should implement a operator class to management all kinds of the presenters
		presenter = WebpagePresenter.getPresenter();
		
		if(presenter == null){
			logger.error("initOperations : ERR_GET_PRESENTER");
			return ERR_GET_PRESENTER;
		} else {
			logger.info("initOperations : PRESENTER ready");
		}
		
		// init reader manager
		readersManager = ReadersManager.getReadersManager();
		
		if( readersManager == null ){
			logger.error("initOperations : ERR_INIT_READERS ");
			return ERR_INIT_REDERS;
		} else {
			logger.info("initOperations : READERS ready");
		}
		
		return 0;
	}
	
	public int exitOperations() {
	
		// run the shutdown hooks
		// 1. shutdown the reader manager
		logger.info(" terminating the readers ");
		ReadersManager.terminateReadersManager();
		
		// 2. terminate the presenter
		logger.info(" terminating the webpager ");
		presenter.terminate();
		
		// 3. close the db manager
		logger.info(" terminating the db ");
		dbOperations.releaseOperations();
		
		// ALL end
		logger.info(" ptv operations exit! ");
		
		return 0;
	}
	
	
	
	
	//******************************************************************************************//
	//																							//
	//									operations definition									//
	//																							//
	//******************************************************************************************//
	
	public enum OperationsEnum {
		
		WriteInfo,
		ShowPage,
		PlayVideo
		
	}
	
	
}
