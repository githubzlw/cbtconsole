package com.winit.api.service;

import org.junit.Test;

import com.winit.api.model.RequestMsg;

public class QueryWarehouse extends QueryBase{
	
	@Test
	public void toDo() {
		doAction();
	}

	@Override
	protected void setdBusinessData(RequestMsg requestMsg) {
		requestMsg.setData("");
		
	}

	@Override
	protected void setRequestAction(RequestMsg requestMsg) {
		requestMsg.setAction("queryWarehouse");
		
	}

	@Override
	protected void parseRequestResult(String result) {
		System.out.println(result);
		
	}

}
