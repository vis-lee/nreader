package com.ptv.Presenter;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Daemon.PtvConstant;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class WebpagePresenter implements IPresenter {

	private final static WebpagePresenter presenter = new WebpagePresenter();
	BrowserLauncher launcher = null;
	
	private WebpagePresenter() {
		
	}
	
	synchronized private boolean isBrowserLauncher() 
			throws BrowserLaunchingInitializingException, 
			       UnsupportedOperatingSystemException  {
		
		if( launcher == null ){
			launcher = new BrowserLauncher();
		}
		
		if( launcher != null){
			return true;
		} else {
			return false;
		}
	}
	

	static public WebpagePresenter getPresenter() {
		return presenter;
	}

	public int showPresentation(CustomerInfo ci) throws Exception {
		
		int retcode = PtvConstant.SUCCESS;
		String urlString = "";
		
		if( isBrowserLauncher() ){
			
			launcher.setNewWindowPolicy(false);
			
			urlString = regionToURL( ci.getLocation().getRegion() );
			
			launcher.openURLinBrowser(urlString);
			
		} else {
			
			retcode = -1;
		}
		
		return retcode;
	}

	public void terminate() {
		// TODO Auto-generated method stub
	}
	
	
	private String regionToURL(String region){
		
		String str = null;
		
		switch( region ) {
		case "TW":
			str = "http://tw.yahoo.com/";
			break;
		case "JP":
			str = "http://www.yahoo.co.jp/";
			break;
		case "US":
		case "UK":
			str = "http://www.msn.com/en-us";
			break;
		case "FR":
			str = "https://fr.yahoo.com/";
			break;
		case "DE":
			str = "https://de.yahoo.com/";
			break;
		case "ES":
			str = "https://es.yahoo.com/";
			break;
			default:
				str = "http://www.msn.com/zh-tw";
		}
		
		return str;
		
	}

	@Override
	public int stopPresentation(CustomerInfo ci) throws Exception {
		// FIXME not support now
		return 0;
	}

}
