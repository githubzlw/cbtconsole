package com.importExpress.service.impl;

import com.importExpress.mapper.EmailStatisticsLogMapper;
import com.importExpress.pojo.EmailStatisticsLog;
import com.importExpress.service.EmailStatisticsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮件发送统计
 *
 * @author chenlun
 */
@Service("emailStatisticsLogServiceImpl")
public class EmailStatisticsLogServiceImpl implements EmailStatisticsLogService {

    @Autowired
    private EmailStatisticsLogMapper emailStatisticsLogMapper;

    @Override
    public void addEmailStatisticsLog(EmailStatisticsLog emailStatisticsLog) {
        emailStatisticsLogMapper.addEmailStatisticsLog(emailStatisticsLog);
    }

    @Override
    public void updateEmailStatisticsLog(EmailStatisticsLog emailStatisticsLog) {
        emailStatisticsLogMapper.updateEmailStatisticsLog(emailStatisticsLog);
    }

}
