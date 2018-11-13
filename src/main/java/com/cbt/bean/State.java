package com.cbt.bean;

public class State {
	private String stateAbb;
	private String stateName;
	private String zipCode;
	private String destinationPort;
	public State(){}
	public State(String stateAbb,String stateName){
		this.stateAbb=stateAbb;
		this.stateName=stateName;
	}
	public String getStateAbb() {
		return stateAbb;
	}
	public void setStateAbb(String stateAbb) {
		this.stateAbb = stateAbb;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}
}
