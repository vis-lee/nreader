package com.ptv.Presenter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Geolocation.Location;

import junit.framework.TestCase;

public class TestWebpagePresenter extends TestCase {

	
	private static final Logger logger = LogManager.getLogger(TestWebpagePresenter.class.getName());
	
	
	private IPresenter presenter;
	
	
	public void testWebpagePresenter(){
		
		logger.debug("test start");
		
		presenter = WebpagePresenter.getPresenter();
		
		CustomerInfo ci = CustomerInfo.genDummyCustomerInfo();
		Location loc = ci.getLocation();

		// set region to US
		loc.setRegion("US");
		showPage(ci, 1);
		
		// set region to JP, should present yahoo jp
		loc.setRegion("JP");
		showPage(ci, 2);
		
		// set region to TW, should present yahoo tw
		loc.setRegion("TW");
		showPage(ci, 3);
		
		
		// set region to FR, should present yahoo France
		loc.setRegion("FR");
		showPage(ci, 4);
		
		// set region to DE, should present yahoo Germany
		loc.setRegion("DE");
		showPage(ci, 5);
		
		// set region to ES, should present yahoo Spain
		loc.setRegion("ES");
		showPage(ci, 6);
	}
	
	private void showPage(CustomerInfo ci, int step) {
		
		try {
			presenter.showPresentation(ci);
			Thread.currentThread().sleep(5*1000);
		} catch (Exception e) {
			logger.error("step {}: ", step, e);
		}
		
	}
	
}
