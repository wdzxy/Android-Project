package com.example.db.model;

public class Song {

    public static final String TABLE_NAME = "song";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_SONG_ID = "song_id";

    public static final String COLUMN_LIST_ID = "list_id";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " TEXT PRIMARY KEY,"
            + COLUMN_SONG_ID + " TEXT,"
            + COLUMN_LIST_ID + " INTEGER"
            + ")";

    private int id;//歌曲在数据库中的id

    private String songId;

    private int listId;

    public Song(int id, String songId, int listId) {
        this.id = id;
        this.songId = songId;
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
