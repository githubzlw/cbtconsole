package com.importExpress.utli;

/**
 * 更新redis的json Bean
 */
public class RedisUpModel {

    private String userId;
    private String type;
    private String json;

    public RedisUpModel(String userId, String type) {
        this.userId = userId;
        this.type = type;
    }

    public RedisUpModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userId\":\"")
                .append(userId).append('\"');
        sb.append(",\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"json\":\"")
                .append(json).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
