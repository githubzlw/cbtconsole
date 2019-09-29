package com.importExpress.utli;

public class RunSqlModel {
    private String type = "2";

    private String sql;

    public String getType() {
        return type;
    }
//    public void setType(String type) {
//        this.type = type;
//    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    public RunSqlModel(String sql) {
        super();
        this.sql = sql;
    }
    public RunSqlModel() {
        super();
    }
    @Override
    public String toString() {
        return "RunSqlModel [type=" + type + ", sql=" + sql + "]";
    }
}
