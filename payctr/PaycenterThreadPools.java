package com.sdhoo.pdloan.payctr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用户支付中心相关线程池
 * @author SDPC_LIU
 *
 */
public class PaycenterThreadPools {

	/**
	 * 用户支付监听相关的线程池 .
	 * 同时处理300个宝付线程 ; 
	 */
	public static ExecutorService userpayMntThreadPool ; 
	
	/**
	 * 代付监听相关的线程池 .
	 * 同时处理300个宝付线程 ; 
	 */
	public static ExecutorService syspayMntThreadPool ; 
	
	/**
	 * 新浪代扣监听相关的线程池 .
	 * 同时处理300个宝付线程 ; 
	 */
	public static ExecutorService sinaDkMntThreadPool ; 
	
	/**
	 * 系统对外通知线程池
	 */
	public static ExecutorService outNotifyThreadPool ; 


	static {
	    ThreadFactory baofuDkThreadFactory = Executors.defaultThreadFactory();
	    userpayMntThreadPool = new ThreadPoolExecutor(2, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10240), baofuDkThreadFactory, new ThreadPoolExecutor.AbortPolicy());
	    
	    ThreadFactory baofuDfThreadFactory = Executors.defaultThreadFactory();
	    syspayMntThreadPool = new ThreadPoolExecutor(2, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10240), baofuDfThreadFactory, new ThreadPoolExecutor.AbortPolicy());
	    
	    ThreadFactory sinaDkThreadFactory = Executors.defaultThreadFactory();
	    sinaDkMntThreadPool = new ThreadPoolExecutor(2, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10240), sinaDkThreadFactory, new ThreadPoolExecutor.AbortPolicy());
	   
	    ThreadFactory outNotifyThreadFactory = Executors.defaultThreadFactory();
	    outNotifyThreadPool = new ThreadPoolExecutor(2, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10240), outNotifyThreadFactory, new ThreadPoolExecutor.AbortPolicy());
	}
	

}
