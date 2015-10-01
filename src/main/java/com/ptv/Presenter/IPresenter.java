/**
 * 
 */
package com.ptv.Presenter;

import com.ptv.Daemon.CustomerInfo;

/**
 * Presenter interface
 * @author Vis.Lee
 *
 */
public interface IPresenter {

	/*
	 * terminate / dispose this presenter
	 */
	public void terminate();
	
	/*
	 * start to show presentations
	 */
	public int showPresentation(CustomerInfo ci) throws Exception;
	
	/*
	 * stop the presentation of the customer
	 */
	public int stopPresentation(CustomerInfo ci) throws Exception;
	
}
