package com.cbt.pojo;

public class JsonResults {
    private int totalpage;
    private Object Results;
    private int message;

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public Object getResults() {
        return Results;
    }

    public void setResults(Object results) {
        Results = results;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
