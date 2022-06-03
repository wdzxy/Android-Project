package com.example.bean;

public class PathBean {

    private String path;

    private int count;

    public PathBean(String path, int count) {
        this.path = path;
        this.count = count;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
