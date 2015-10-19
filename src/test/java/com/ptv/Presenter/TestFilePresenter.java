package com.ptv.Presenter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.DB.FileDataBase;
import com.ptv.Daemon.CustomerInfo;
import junit.framework.TestCase;

public class TestFilePresenter extends TestCase {

	private static final Logger logger = LogManager.getLogger(TestFilePresenter.class.getName());
	
	public void testGetCommands(){

		FilePresenter fp;
		
		try {
			fp = new FilePresenter("./conf");
			CustomerInfo ci = CustomerInfo.genDummyCustomerInfo();
			String cmd = fp.genCommands(ci);
			
			logger.info("ci = {}\n", ci);
			logger.info("cmd = {}", cmd);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
