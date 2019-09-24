package com.importExpress.utli;

import java.util.ArrayList;
import java.util.List;

//{"type":"3","sqls":["insert into test values(1)","insert into test values(2)"]}
public class RunBatchSqlModel {

    private String type = "3";

    private List<String> sqls = new ArrayList<String>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getSqls() {
        return sqls;
    }

    public void setSqls(List<String> sqls) {
        this.sqls = sqls;
    }

    //添加一个sql
    public void addSql(String sql) {
        sqls.add(sql);
    }

    public RunBatchSqlModel() {
        super();
    }

    @Override
    public String toString() {
        return "RunSqlModel [type=" + type + ", sqls=" + sqls + "]";
    }

}
