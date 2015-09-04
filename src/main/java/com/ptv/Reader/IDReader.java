package com.ptv.Reader;

import com.ptv.Daemon.CustomerInfo;

/**
 * The IDReader is a middle layer for unify the APIs for the all kinds of the readers,
 * which starts a worker thread to polling the readers and offer the result in a sync-
 * queue.
 * @author Vis.Lee
 *
 */
public interface IDReader {
	
	/*
	 * init the worker thread of reader
	 */
	public ReaderState initReaderWorker();
	
	/*
	 * release the worker thread of reader
	 */
	public void releaseReaderWorker();
	
	/*
	 * read ID from sync-queue
	 */
	public CustomerInfo readID();
	
	/*
	 * @return ReaderName
	 */
	public String getReaderName();
	
}
