package com.example.bean;

public class MainSongSheetBean {

    private String drawAble;

    private String name;

    private int count;

    public String getDrawAble() {
        return drawAble;
    }

    public void setDrawAble(String drawAble) {
        this.drawAble = drawAble;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(){
        this.count++;
    }

    public void addCount(int num){
        this.count += num;
    }

    public void mulCount(){
        this.count--;
    }

    public void mulCount(int num){
        this.count -= num;
    }
}
