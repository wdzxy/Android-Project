package com.example.bean;

import java.util.Objects;

public class AlbumBean {

    private String albumName;

    private int count;

    public AlbumBean(String albumName, int count) {
        this.albumName = albumName;
        this.count = count;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumBean albumBean = (AlbumBean) o;
        return count == albumBean.count &&
                albumName.equals(albumBean.albumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumName, count);
    }
}
