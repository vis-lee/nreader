/**
 * 
 */
package com.ptv.Presenter;

/**
 * Presenter interface
 * @author Vis.Lee
 *
 */
public interface IPresenter {

	/*
	 * presenter device operation funcs
	 */
	public IPresenter initPresenter() throws Exception;
	public int releasePresenter() throws Exception;
	
	public int getPresenter() throws Exception;
	public int show() throws Exception;
	
	
}
