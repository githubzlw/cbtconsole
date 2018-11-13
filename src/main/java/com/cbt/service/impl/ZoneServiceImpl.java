package com.cbt.service.impl;

import com.cbt.dao.ZoneDao;
import com.cbt.dao.impl.ZoneDaoImpl;
import com.cbt.service.ZoneService;

public class ZoneServiceImpl implements ZoneService {
	private ZoneDao zoneDao = new ZoneDaoImpl();

	@Override
	public String getCountryById(String id,String userid) {

		return zoneDao.getCountryById(id,userid);
	}

}
