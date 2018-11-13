package com.cbt.processes.thread;

import com.cbt.bean.SpiderBean;
import com.cbt.processes.service.IPreferentialServer;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.PreferentialServer;
import com.cbt.processes.service.SpiderServer;

public class GoodsAddThread extends Thread {

	private SpiderBean spiderBean;
	private int type ;
	private int number;
	private String price;
	private String totalweight;
	private String totalvalume;
	private String guid;
	private int userid;
	private String sessionId;
	private int preferentalId;
	
	public GoodsAddThread() {
	}
	
	public GoodsAddThread(SpiderBean spiderBean, int type) {
		super();
		this.spiderBean = spiderBean;
		this.type = type;
	}

	public GoodsAddThread(SpiderBean spiderBean, int type, int preferentalId) {
		super();
		this.spiderBean = spiderBean;
		this.type = type;
		this.preferentalId = preferentalId;
	}
	
	public GoodsAddThread( int type, int number,
			String price, String totalweight, String totalvalume, String guid,
			int userid, String sessionId) {
		super();
		this.type = type;
		this.number = number;
		this.price = price;
		this.totalweight = totalweight;
		this.totalvalume = totalvalume;
		this.guid = guid;
		this.userid = userid;
		this.sessionId = sessionId;
	}

	@Override
	public synchronized void run() {
		ISpiderServer iss = new SpiderServer();
		if(type == 0){
			iss.addGoogs_car(spiderBean);
		}else{
			iss.upGoogs_car(guid,0, number, userid,sessionId, price, totalvalume, totalweight);
		}
		if(preferentalId != 0){
			IPreferentialServer preferentialServer = new PreferentialServer();
			preferentialServer.upPA(preferentalId);
		}
		this.spiderBean = null;
	}
	
	
}
