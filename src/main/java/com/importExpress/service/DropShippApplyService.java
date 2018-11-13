package com.importExpress.service;

import com.importExpress.pojo.DShippUser;

import java.util.List;

public interface DropShippApplyService {

	List<DShippUser> findAllDropShip(String userCategory, int start, int end);

    int dropShiptotal(String userCategory, int start, int end);
}
