package com.example.bean;

public class SingerBean {

    private String singer;

    private int count;

    public SingerBean(String singer, int count) {
        this.singer = singer;
        this.count = count;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
