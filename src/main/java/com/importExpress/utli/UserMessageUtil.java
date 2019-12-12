package com.importExpress.utli;

import com.importExpress.pojo.UserMessage;
import com.importExpress.service.UserOtherInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.utli
 * @date:2019/12/12
 */
@Component
public class UserMessageUtil {

    @Autowired
    private UserOtherInfoService userOtherInfoService;

    /**
     * 发送客户通知信息 本地备份一次
     *
     * @param userId
     * @param orderNo
     * @param type
     * @return
     */
    public boolean sendMessage(int userId, String orderNo, int type, String content, String jumpUrl,String question,String answer) {

        if (userId == 0 && StringUtils.isBlank(orderNo)) {
            return false;
        }
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(userId);
        userMessage.setOrderNo(orderNo);
        userMessage.setType(type);
        if (StringUtils.isNotBlank(content)) {
            if (content.length() > 50) {
                userMessage.setContent(content.substring(0, 50) + "***");
            } else {
                userMessage.setContent(content);
            }
        }
        userMessage.setJumpUrl(jumpUrl);
        userMessage.setQuestion(question);
        userMessage.setAnswer(answer);
        paraseType(userMessage);

        sendByMq(userMessage);
        userOtherInfoService.insertIntoUserMessage(userMessage);
        return false;
    }


    private void paraseType(UserMessage userMessage) {
        /**
         * 1.用户使用问题反馈
         * 2.产品单页用户留言
         */
        switch (userMessage.getType()) {
            case 1:
                if (StringUtils.isBlank(userMessage.getJumpUrl())) {
                    userMessage.setJumpUrl("");
                }

                if (StringUtils.isBlank(userMessage.getContent())) {
                    userMessage.setContent("Your question " + userMessage.getOrderNo() + " has a new reply, please click here to check >>");
                }
                break;
            case 2:
                if (StringUtils.isBlank(userMessage.getJumpUrl())) {
                    userMessage.setJumpUrl("/goodsinfo/Our-Website-Goods-1" + userMessage.getOrderNo() + ".html");
                }
                if (StringUtils.isBlank(userMessage.getContent())) {
                    userMessage.setContent("There is a new reply to your question about the " + userMessage.getOrderNo()
                            + " product, please click here to check >>");
                }
                break;
            default:
                userMessage.setJumpUrl("");
                if (StringUtils.isBlank(userMessage.getContent())) {
                    userMessage.setContent("");
                }
        }
    }

    /**
     * 通过MQ发送客户通知信息
     *
     * @param userMessage
     */
    private void sendByMq(UserMessage userMessage) {
        String content;
        if (StringUtils.isNotBlank(userMessage.getContent())) {
            content = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(userMessage.getContent());
        } else {
            content = "";
        }
        String jumpUrl;
        if (StringUtils.isNotBlank(userMessage.getJumpUrl())) {
            jumpUrl = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(userMessage.getJumpUrl());
        } else {
            jumpUrl = "";
        }
        String question ;
        if (StringUtils.isNotBlank(userMessage.getQuestion())) {
            question = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(userMessage.getQuestion());
        } else {
            question = "";
        }
        String answer ;
        if (StringUtils.isNotBlank(userMessage.getAnswer())) {
            answer = GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(userMessage.getAnswer());
        } else {
            answer = "";
        }
        StringBuffer sb = new StringBuffer("insert into user_message(user_id,order_no,content,type,jump_url,question,answer) values(");
        sb.append(userMessage.getUserId() + ",")
                .append("\'" + userMessage.getOrderNo() + "\',")
                .append("\'" + content + "\',")
                .append(+userMessage.getType() + ",")
                .append("\'" + jumpUrl + "\')")
                .append("\'" + question + "\')")
                .append("\'" + answer + "\')");
        NotifyToCustomerUtil.sendSqlByMq(sb.toString());
    }
}
