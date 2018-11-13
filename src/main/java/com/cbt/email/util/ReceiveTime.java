package com.cbt.email.util;

import com.cbt.email.TimerListener.InitListener;
import com.cbt.email.email.ReciveOneMail1;
import com.cbt.email.entity.EmailUser;

import java.util.TimerTask;


public class ReceiveTime extends TimerTask {

	private EmailUser user;
	
	public EmailUser getUser() {
		return user;
	}

	public void setUser(EmailUser user) {
		this.user = user;
	}

	public ReceiveTime(EmailUser user) {
		this.user = user;
	}

	public ReceiveTime() {
		super();
	}

	@Override
	public void run() {
		// 每二十分钟执行一次
		if(InitListener.list.size() > 0) {
			for (EmailUser e : InitListener.list) {
				//ReciveOneMail.imap(path);
				//接收邮件的方法
				ReciveOneMail1.pop(e);
			}
		}
	}

}
