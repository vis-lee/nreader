/**
 * 
 */
package com.ptv.Presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ptv.Daemon.CustomerInfo;
import com.ptv.Daemon.PtvConstant;
import com.ptv.Geolocation.Location;

/**
 * @author Vis.Lee
 *
 */
public class FilePresenter implements IPresenter {

	private static final Logger logger = LogManager.getLogger(FilePresenter.class.getName());
	
	private String presenterFilesDir;
	
	private List<Location> regions;
	
	private Process currentNFCCmd;
	
	private String fpcmd;
	
	public FilePresenter(String fpcmd) throws Exception {
		this(fpcmd, PtvConstant.PTV_FPRS_DIR);
	}
	
	public FilePresenter(String fpcmd, String presenterFilesDir) throws Exception {
		super();
		__init(fpcmd, presenterFilesDir);
	}
	
	private int __init(String fpcmd, String presenterFilesDir) throws Exception{
		
		int retcode = PtvConstant.SUCCESS;
		
		this.presenterFilesDir = presenterFilesDir;
		File prstFileDir = new File(this.presenterFilesDir);
		
		logger.debug("presenter file dir located in = {}", presenterFilesDir);
		
		if(!prstFileDir.exists()){
			
			// create file
			prstFileDir.mkdirs();
		}
		
		this.fpcmd = fpcmd;
		
		File cmdfile = new File(PtvConstant.PTV_ROOT_DIR + "/" + this.fpcmd);
		
		if(!cmdfile.exists()){
			
			logger.error("!! you defined the fpexecmd, but we can't find the exe file!!");
			throw new FileNotFoundException();
		}
		
		this.regions = Arrays.asList(Location.dummyLocations);
		
		logger.info("File presenter init finished!");
		logger.info(regions.toString());
		
		return retcode;
	}
	
	@Override
	public void terminate() {
		
		cancelPresentation();
		
	}

	private void cancelPresentation() {
		
		if(currentNFCCmd != null && currentNFCCmd.isAlive()){
			
			// destroy
			currentNFCCmd.destroy();
			
			logger.debug("destroy previouse process: {}", currentNFCCmd.toString());
		}
		
	}

	@Override
	public int showPresentation(CustomerInfo ci) throws Exception {
		
		String cmd = genCommands(ci);

		cancelPresentation();

		logger.debug("ci ={}, \n\texecute the command: {}", ci, cmd);
		
		// call to the NFCCommand.exe
		currentNFCCmd = Runtime.getRuntime().exec(cmd);
		
		return 0;
	}

	protected String genCommands(CustomerInfo ci) {
		
		String cmd = new String(fpcmd + " /c");
		
		int index = regions.indexOf(ci.getLocation());
		
		if(index >= 0){
			
			cmd = cmd + index;
			logger.debug("NFCCmd = {}", cmd);
			
		} else {
			
			// default value
			cmd = cmd + "0";
			logger.error("can't found the index, location = {}", ci.getLocation() );
		}
		
		return cmd;
	}

	@Override
	public int stopPresentation(CustomerInfo ci) throws Exception {
		
		return 0;
	}

}
