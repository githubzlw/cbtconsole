package com.cbt.parse.pool;

public class PThread extends Thread {
	//线程池
	private ThreadPool pool;
	//任务
	private Runnable target;
	private boolean isShutDown = false;
	private boolean isIdle = false;
	
	
	public PThread() {
		
	}
	public PThread(Runnable target,String name,ThreadPool pool){
		super(name);
		this.pool = pool;
		this.target = target;
	}
	
	public Runnable getTarget(){
		return target;
	}
	
	public boolean isIdle(){
		return isIdle;
	}
	
	@Override
	public void run() {
		super.run();
		while(!isShutDown){
			isIdle = false;
			if(target!=null){
				target.run();
			}
			isIdle = true;
			try {
				pool.repool(this);
				synchronized (this) {
					wait();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			isIdle = false;
		}
	}
	
	public synchronized void setTarget(Runnable newTarget){
		target  = newTarget;
		notifyAll();
		
	}
	
	public synchronized void shutDown(){
		isShutDown = true;
		notifyAll();
	}
	
}
