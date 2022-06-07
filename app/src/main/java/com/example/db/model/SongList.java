package com.example.db.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.litepal.crud.LitePalSupport;

import java.time.LocalDateTime;

/**
 * 自定义歌单
 * 创建默认的“我的收藏”歌单
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class SongList {

    public static final String TABLE_NAME = "song_list";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_CREATE_TIME = "create_time";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_CREATE_TIME + " TEXT"
            + ")";

    public static final String INSERT_COLLECTION = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_NAME + "," + COLUMN_CREATE_TIME + ") VALUES ("
            + "收藏,"
            + LocalDateTime.now().toString() + ")";

    private int id;//歌单的主键

    private String name;//歌单名

    private String createTime;//创建时间

    public SongList(int id, String name, String createTime) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
