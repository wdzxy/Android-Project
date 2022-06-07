package com.example.bean;

public class SongListBean {

    private int id;

    private String name;

    private String createTime;

    private int count;

    public SongListBean(int id, String name, String createTime, int count) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }
}
