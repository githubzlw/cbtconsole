package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.ZoneMapper;
import com.cbt.warehouse.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ZoneInfoServiceImpl")
public class ZoneInfoServiceImpl implements IZoneInfoService {

	@Autowired
	private ZoneMapper zoneMapper;
	
	@Override
	public List<TransitCheckfree> getCheckFree() {
		return zoneMapper.getCheckFree();
	}

	@Override
	public List<TransitPricecost> getPriceCost() {
		// TODO Auto-generated method stub
		return zoneMapper.getPriceCost();
	}
	@Override
	public List<TransitPricecost> getPriceCost1() {
		// TODO Auto-generated method stub
		return zoneMapper.getPriceCost1();
	}

	@Override
	public List<TransitType> getTransitType(int type) {
		// TODO Auto-generated method stub
		return zoneMapper.getTransitType(type);
	}
	
	@Override
	public List<DeliveryDate> getDeliveryDate() {
		// TODO Auto-generated method stub
		return zoneMapper.getDeliveryDate();
	}

	@Override
	public List<CountryEpacketjcexBean> getCountryEpacket() {
		// TODO Auto-generated method stub
		return zoneMapper.getCountryEpacket();
	}

}
