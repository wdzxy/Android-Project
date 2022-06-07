package com.example.db.model;

/**
 * 收藏
 */
public class Collection {

    public static final String TABLE_NAME = "collection";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_SONG_NAME = "song_name";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " TEXT PRIMARY KEY,"
            + COLUMN_SONG_NAME + " TEXT"
            + ")";

    private String songID;

    private String songName;

    public Collection(String songID, String songName) {
        this.songID = songID;
        this.songName = songName;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
