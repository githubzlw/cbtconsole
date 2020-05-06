package com.importExpress.pojo;

import lombok.Data;

/**
 * *****************************************************************************************
 *
 * @ClassName CustomerQuestionsAndReplayBean
 * @Author: cjc
 * @Descripeion 客户提问和客服回复列表
 * @Date： 2020/4/22 3:48 下午
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       3:48 下午2020/4/22     cjc                       初版
 * ******************************************************************************************
 */
@Data
public class CustomerQuestionsAndReplayBean {
    private int id;
    private int type = 0;
    private String creatime;
    private Object msg;
    private int flag = 0;
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(id).append('\"');
        sb.append(",\"type\":\"").append(type).append('\"');
        sb.append(",\"creatime\":\"").append(creatime).append('\"');
        sb.append(",\"msg\":\"").append(msg).append('\"');
        sb.append(",\"flag\":\"").append(flag).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
