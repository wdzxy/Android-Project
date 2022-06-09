package com.example.bean;

import java.util.Objects;

public class SingleSongBean {

    private String id;//歌曲id

    private String song;//歌曲名

    private String singer;//歌手名

    private String duration;//时长

    private String path;//存储位置

    private String album;//专辑名

    private String ID;//在数据库中的id

    private String albumArt;//封面id

    public SingleSongBean() {
    }

    public SingleSongBean(String id, String song, String singer, String duration, String path, String album, String ID) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.duration = duration;
        this.path = path;
        this.album = album;
        this.ID = ID;
    }

    public SingleSongBean(String id, String song, String singer, String duration, String path, String album, String ID, String albumArt) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.duration = duration;
        this.path = path;
        this.album = album;
        this.ID = ID;
        this.albumArt = albumArt;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setImage(String albumArt) {
        this.albumArt = albumArt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleSongBean that = (SingleSongBean) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(song, that.song) &&
                Objects.equals(singer, that.singer) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, song, singer, duration, path);
    }
}
