package com.cbt.bean;

/**
 * @author Administrator
 * ����˷�
 */
public class DeliveryFee {
	private int id;
	private String deliverymode;
	private String expecteddays;
	private float weight;
	private float deliveryfee;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDeliverymode() {
		return deliverymode;
	}
	public void setDeliverymode(String deliverymode) {
		this.deliverymode = deliverymode;
	}
	public String getExpecteddays() {
		return expecteddays;
	}
	public void setExpecteddays(String expecteddays) {
		this.expecteddays = expecteddays;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public float getDeliveryfee() {
		return deliveryfee;
	}
	public void setDeliveryfee(float deliveryfee) {
		this.deliveryfee = deliveryfee;
	}
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"deliverymode\":\"%s\", \"expecteddays\":\"%s\", \"weight\":\"%s\", \"deliveryfee\":\"%s\"}",
						id, deliverymode, expecteddays, weight, deliveryfee);
	}
	
}
