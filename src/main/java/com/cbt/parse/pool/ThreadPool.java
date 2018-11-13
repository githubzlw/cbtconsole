package com.cbt.parse.pool;

import java.util.List;
import java.util.Vector;

public class ThreadPool {
	private static ThreadPool instance = null;
	//空闲的线程队列
	private List<PThread> pThreads;
	//已有的线程总数
	private int threadCounter;
	private boolean isShutDown = false;
	
	private ThreadPool(){
		this.pThreads  = new Vector<PThread>(20);
		threadCounter = 0;
	}
	
	public int getCreatedThreadsCount(){
		return threadCounter;
	}
	
	//取得线程池实例
	public synchronized static ThreadPool getInstance(){
		if(instance == null){
			instance = new ThreadPool();
		}
		return instance;
	}
	
	//将已经执行完的线程放入线程池
	protected synchronized void repool(PThread repooling) {
		if(!isShutDown){
			pThreads.add(repooling);
		}else{
			repooling.shutDown();
		}
	}
	
	//停止线程池所有线程
	public synchronized void shutDown(){
		isShutDown = true;
		for (int th = 0; th < pThreads.size(); th++) {
			PThread idleThread = pThreads.get(th);
			idleThread.shutDown();
		}
	}
		
	//执行任务
	public synchronized void start(Runnable target){
		PThread thread = null;
//		System.out.println("threadCounter:"+threadCounter);
		//如果有空闲线程，则直接使用
		if(pThreads.size()>0){
			int lastIndex = pThreads.size()-1;
			thread = pThreads.get(lastIndex);
			pThreads.remove(lastIndex);
			//立即执行
			thread.setTarget(target);
		}else{
			//没有空闲线程  则创建线程
			threadCounter++;
			thread = new PThread(target, "PThread #"+threadCounter, this);
			
			//启动线程
			thread.start();
			
		}
		
	}
}
	
