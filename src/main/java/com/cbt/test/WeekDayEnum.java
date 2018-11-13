package com.cbt.test;

public enum WeekDayEnum {
    SUN(7,"sun"),MON(1,"mon"),TUS(2,"tus"),WED(3,"wed"),THU(4,"thu"),FRI(5,"fri"),SAT(6,"sat");

    private int value;
    private String lable;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    WeekDayEnum(int value, String lable) {
        this.value = value;
        this.lable = lable;
    }
}

class TestGoEnum{
    public static void main(String[] args) {
        System.err.println(WeekDayEnum.valueOf("mon".toUpperCase()));
        for(WeekDayEnum wd : WeekDayEnum.values()){
            System.err.println(wd + ".getValue() ======>" + wd.getValue());
        }

        System.err.println("WeekDayEnum.SAT.compareTo(WeekDayEnum.FRI):" + WeekDayEnum.SAT.compareTo(WeekDayEnum.FRI));
        System.err.println("WeekDayEnum.SUN.name():" + WeekDayEnum.SUN.getLable());
    }
}