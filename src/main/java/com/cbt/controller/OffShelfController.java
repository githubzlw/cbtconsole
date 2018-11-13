package com.cbt.controller;

import com.cbt.service.OffShelfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ly
 * @ClassName OffShelfController
 * @Description 软下架 硬下架相关
 * @date 2018/08/10 10:05
 */
@Controller
@RequestMapping(value = "/offshelf")
public class OffShelfController {

    private static final Log LOG = LogFactory.getLog(OffShelfController.class);

    @Autowired
    private OffShelfService offShelfService;

    /**
     * 	接口传入值
			pid					商品pid
			unsellableReason	需要更新的值（取值范围>0 ）
		处理逻辑
			更新unsellableReason
			按照文档 根据同款数量更新valid为0或2...
		具体处理
			unsellableReason 更新成对应值
			valid
				unsellableReason  更新值说明
					1			1688货源下架
						valid = 同款>0?=2:0;
					2			不满足库存条件
						valid = 2;
					3			销量无变化(低库存)
						不更新 valid
					4			页面404
						valid = 同款>0?=2:0;
					5			重复验证合格
						
					6			IP问题或运营直接下架
						valid = 0;
					7			店铺整体禁掉
						valid = 2;  						 可能问题 原本是0 更新成2
					8			采样不合格
						valid = 同款>2?=2:0;   				 
					9			有质量问题
						valid = 同款>2?=2:0;  				
					10			商品侵权
						valid = 0;
					11			店铺侵权
						valid = 0;
					12			难看
						
					13			中文
     *
     *	 2018/08/13 9:25 新增逻辑 如果商品有库存则不下架
     *		select count(id) as n from inventory where goods_pid= ?
     *
     *	 2018/08/10 15:57 ly create
     * @param pid 需要更新的商品pid
     * @param unsellableReason 原因
     * @return
     */
    @RequestMapping(value = "/updatebypid.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> updateByPid(@RequestParam("pid") String pid, 
    		@RequestParam("unsellableReason") Integer unsellableReason) {
    	HashMap<String, String> resultMap = new HashMap<String, String>();
        try {
            if (null == pid || !pid.matches("\\d{3,30}")
            		|| null == unsellableReason || unsellableReason <= 0 
            		|| unsellableReason > 13) {
                // 参数错误，响应400
            	resultMap.put("state", "error");
            	resultMap.put("message", "参数错误");
                return resultMap;
            }
            //下架逻辑
            Integer res = offShelfService.updateByPid(pid, unsellableReason);
            // 返回0 更新失败，返回1更新成功，返回2 unsellableReason值问题
            if (res == 1) {
            	resultMap.put("state", "success");
            	resultMap.put("message", "更新成功");
			} else if (res == 0) {
				resultMap.put("state", "error");
				resultMap.put("message", "更新失败(无此数据或内部错误)");
			} else if (res == 11) {
				resultMap.put("state", "success");
				resultMap.put("message", "更新成功(有库存,不更新valid)");
			} else if (res == 10) {
				resultMap.put("state", "error");
				resultMap.put("message", "更新失败(有库存)");
			} else {
				resultMap.put("state", "error");
            	resultMap.put("message", "参数错误");
			}
            return resultMap;
        } catch (Exception e) {
            LOG.error("OffShelfController.updateByPid error " + e);
        }
        // 响应500
        resultMap.put("state", "error");
    	resultMap.put("message", "内部错误");
        return resultMap;
    }
    
    
    /**
	 * ly 2018/09/03 13:59
	 * http://127.0.0.1:8086/cbtconsole/offshelf/goodssoftoffshelf.do
	 * 商品软下架转硬下架 
	 * 通过url触发 定时 
	 **/
	@RequestMapping("/goodssoftoffshelf")	
	@ResponseBody
	public Map<String, String> goodsSoftOffShelf() {
		HashMap<String, String> resultMap = new HashMap<String, String>();
//        try {
        	offShelfService.goodsSoftOffShelf();
//        	resultMap.put("state", "success");
//        } catch(Exception e){
//        	resultMap.put("state", "error");
//        	resultMap.put("message", "内部错误");
//        }
		return resultMap;
	}

}
