package com.importExpress.pojo;

public class SimpleCarDataBean {
    /**
     * 产品id
     */
    private String id;
    /**
     * 获取添加购物车的各种规格id 规格价格 规格数量list
     */
    private String tp;
    /**
     * 数量
     */
    private int num;
    /**
     * 店铺下有多少商品
     */
    private int sct;
    /**
     * 部分check
     */
    private int st;
    /**
     * 产品remark
     */
    private String rm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSct() {
        return sct;
    }

    public void setSct(int sct) {
        this.sct = sct;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"tp\":\"")
                .append(tp).append('\"');
        sb.append(",\"num\":")
                .append(num);
        sb.append(",\"sct\":")
                .append(sct);
        sb.append(",\"st\":")
                .append(st);
        sb.append(",\"rm\":\"")
                .append(rm).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
