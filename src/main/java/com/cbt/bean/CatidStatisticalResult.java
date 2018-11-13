package com.cbt.bean;

public class CatidStatisticalResult extends CatidInputWeight {
	private int tooLightNum;
	private int tooHeavyNum;
	private int weightZoneNum;

	public int getTooLightNum() {
		return tooLightNum;
	}

	public void setTooLightNum(int tooLightNum) {
		this.tooLightNum = tooLightNum;
	}

	public int getTooHeavyNum() {
		return tooHeavyNum;
	}

	public void setTooHeavyNum(int tooHeavyNum) {
		this.tooHeavyNum = tooHeavyNum;
	}

	public int getWeightZoneNum() {
		return weightZoneNum;
	}

	public void setWeightZoneNum(int weightZoneNum) {
		this.weightZoneNum = weightZoneNum;
	}

	@Override
	public String toString() {
		return "{\"tooLightNum\":\"" + tooLightNum + "\", \"tooHeavyNum\":\"" + tooHeavyNum + "\", \"weightZoneNum\":\""
				+ weightZoneNum + "\", \"toString()\":\"" + super.toString() + "\"}";
	}

}
