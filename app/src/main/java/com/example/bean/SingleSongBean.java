package com.example.bean;

public class SingleSongBean {

    private String id;//歌曲id

    private String song;//歌曲名

    private String singer;//歌手名

    private String duration;//时长

    private String path;//存储位置

    public SingleSongBean() {
    }

    public SingleSongBean(String id, String song, String singer, String duration, String path) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.duration = duration;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
