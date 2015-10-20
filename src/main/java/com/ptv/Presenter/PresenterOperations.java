/**
 * 
 */
package com.ptv.Presenter;

import com.ptv.Daemon.CustomerInfo;

/**
 * @author Vis.Lee
 *
 */
public class PresenterOperations {
	

	private static PresenterOperations prstOperations;
	
	private IPresenter ipresenter;
	
	
	private PresenterOperations() throws Exception {
		
		String FPCMD = System.getProperty("fpexecmd");
		
		if(FPCMD == null){
			
			ipresenter = (IPresenter)WebpagePresenter.getPresenter();
			
		} else {
			
			FPCMD = System.getProperty("fpexecmd");
			ipresenter = (IPresenter) new FilePresenter(FPCMD);
			
		}
		
	}
	
	
	// get presenter operations
	static public PresenterOperations getPresentOperations() throws Exception {
		
		if( prstOperations == null){
			return __getPresentOperations();
		}
		
		return prstOperations;
	}
	

	synchronized static private PresenterOperations __getPresentOperations() throws Exception{
		
		if(prstOperations == null){
			
			prstOperations = new PresenterOperations();

		}
		
		return prstOperations;
	}



	public void showPresentation(CustomerInfo ci) throws Exception {
		
		ipresenter.showPresentation(ci);
		
	}



	public void terminate() {

		ipresenter.terminate();
		
	}
	
	

}
