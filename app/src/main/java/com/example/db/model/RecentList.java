package com.example.db.model;

public class RecentList {

    public static final String TABLE_NAME = "recent_list";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_SONG_ID = "song_id";

    public static final String COLUMN_SONG_NAME = "song_name";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " TEXT PRIMARY KEY,"
            + COLUMN_SONG_ID + " TEXT,"
            + COLUMN_SONG_NAME + " TEXT"
            + ")";

    private int id;

    private String songID;

    private String songName;

    public RecentList(String songID, String songName) {
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
