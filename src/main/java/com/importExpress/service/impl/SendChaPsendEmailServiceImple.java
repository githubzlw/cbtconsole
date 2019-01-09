package com.importExpress.service.impl;

import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.processes.dao.IUserDao;
import com.cbt.util.Utility;
import com.cbt.website.util.JsonResult;
import com.importExpress.mail.TemplateType;
import com.importExpress.service.SendChaPsendEmailService;
import com.importExpress.utli.FreightUtlity;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * *****************************************************************************************
 *
 * @ClassName sendChaPsendEmailServiceImple
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2018/12/29 10:34:34
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       10:34:342018/12/29     cjc                       初版
 * ******************************************************************************************
 */
@Service
public class SendChaPsendEmailServiceImple implements SendChaPsendEmailService {
    private static final Logger logger = LoggerFactory.getLogger(SendChaPsendEmailServiceImple.class);
    @Autowired
    private FreightUtlity freightUtlity;
    @Autowired
    private IPictureComparisonService ips;
    @Override
    public JsonResult sendChaPsendEmail(String emailInfo, String email, String copyEmail, String orderNo, String userId, String title , String reason3){
        JsonResult result = new JsonResult();
        try {
            //step v1. @author: cjc @date：2018/12/29 10:25:45  TODO 获取参数
            ips.updateCgdEmailFlag(orderNo);
            //线上插入changegooddata数据
            int i = ips.insertOnlineChange(orderNo);
            int res = 0;
            if(Utility.getStringIsNull(emailInfo)) {
               /* String sendemail = null;
                String pwd = null;
                if (Utility.getStringIsNull(copyEmail)) {
                    String[] adminEmail = userDao.getAdminUser(0, copyEmail, 0);
                    if (adminEmail != null) {
                        sendemail = adminEmail[0];
                        pwd = adminEmail[1];
                    }
                }*/
                //Added <V1.0.1> Start： cjc   17:41:57 TODO 使用新版
                Map<String,Object> map = new HashedMap();
                //href="http://www.import-express.com/orderInfo/getChangeProduct?flag=1&orderNo=QC26660789904597_1"
                String href = "";
                map.put("clickHereForDetails","http://www.import-express.com/orderInfo/getChangeProduct?flag=1&orderNo="+orderNo);
                //标题
                map.put("title",title);
                //客户邮件
                map.put("email",email);
                //邮件信息
                map.put("emailInfo",emailInfo);
                //抄送人
                map.put("copyEmail",copyEmail);
                //用户id
                map.put("userId",userId);
                //订单
                map.put("orderNo",orderNo);
                //备注
                map.put("reason3",reason3);

                freightUtlity.sendMailNew(email, copyEmail, title, map, TemplateType.REPLACEGOODS);
                result.setOk(true);
                result.setMessage("success！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送替换邮件失败e:[{}]",e.getMessage());
            result.setMessage("failure！");
            result.setOk(false);
        }
        return result;
    }
}
