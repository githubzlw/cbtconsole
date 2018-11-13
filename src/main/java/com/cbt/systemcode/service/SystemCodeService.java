package com.cbt.systemcode.service;

import com.cbt.pojo.SystemCode;
import com.cbt.pojo.SystemCodeExample;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SystemCodeService {
	List<SystemCode> selectByExample(SystemCodeExample example);
    
}