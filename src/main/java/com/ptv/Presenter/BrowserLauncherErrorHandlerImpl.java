package com.ptv.Presenter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.PtvDaemon;

import edu.stanford.ejalbert.exceptionhandler.BrowserLauncherErrorHandler;

public class BrowserLauncherErrorHandlerImpl implements BrowserLauncherErrorHandler {

	
	private static final Logger logger = LogManager.getLogger(PtvDaemon.class.getName());
	
	@Override
	public void handleException(Exception ex) {

		logger.error(ex);

	}

}
