package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.ProblemFeedBackDao;
import com.cbt.warehouse.pojo.ProblemFeedBackBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取注册页面、购物车页面、支付页面反馈弹框数据;
 * @author tb
 * @time 2017.3.9
 */
@Service
public class ProblemFeedBackServiceImpl implements ProblemFeedBackService {

	@Autowired
	private ProblemFeedBackDao problemFeedBackDao;
	
	@Override
	public List<ProblemFeedBackBean> queryForList(int type, String beginDate,
			String endDate,int adminId, int pageNo,String is_report) {
		return problemFeedBackDao.queryForList(type, beginDate, endDate,adminId, pageNo,is_report);
	}

	@Override
	public Long queryCount(int type, String beginDate, String endDate,int adminId,String is_report) {
		return problemFeedBackDao.queryCount(type, beginDate, endDate,adminId,is_report);
	}

	@Override
	public List<ProblemFeedBackBean> queryForAllList() {
		return problemFeedBackDao.queryForAllList();
	}
	
	@Override
	public List<ProblemFeedBackBean> getReportProblem(String report_id) {
		
		return problemFeedBackDao.getReportProblem(report_id);
	}

    @Override
    public Map<String, String> queryWarningNum() {
        List<Map<String, String>> list = problemFeedBackDao.queryWarningNum();
        if (list != null && list.size() > 0){
            Map<String, String> result = new HashMap<String, String>();
            for (Map<String, String> bean : list) {
                result.put("type" + String.valueOf(bean.get("type")), String.valueOf(bean.get("total")));
            }
            return result;
        }
        return null;
    }
}
