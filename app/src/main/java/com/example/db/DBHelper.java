package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.bean.SingleSongBean;
import com.example.bean.SongListBean;
import com.example.db.model.Collection;
import com.example.db.model.RecentList;
import com.example.db.model.Song;
import com.example.db.model.SongList;
import com.example.musicplayer.player.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "song_list_db";

    private static final int DATABSE_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库表结构，使用sql创建表，检测到存在数据库文件时不会调用onCreate方法
        db.execSQL(SongList.CREATE_TABLE);
        db.execSQL(Song.CREATE_TABLE);
        db.execSQL(Collection.CREATE_TABLE);
        db.execSQL(RecentList.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库更新
    }

    /**
     * 创建一个新的歌单
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long insertList(String name){
        String createTime = LocalDateTime.now().toString();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SongList.COLUMN_NAME,name);
        values.put(SongList.COLUMN_CREATE_TIME,createTime);

        long id = db.insert(SongList.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    /**
     * 查询所有的歌单，显示歌单名和歌曲数量
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<SongListBean> getSongList(){
        String[] columns = new String[]{
                SongList.COLUMN_ID,
                SongList.COLUMN_NAME,
                SongList.COLUMN_CREATE_TIME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                SongList.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<SongListBean> songListBeans = new ArrayList<>(cursor.getCount());
        while (cursor != null && cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SongList.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(SongList.COLUMN_NAME));
            String createTime = cursor.getString(cursor.getColumnIndex(SongList.COLUMN_CREATE_TIME));

            Cursor songCursor = db.query(
                    Song.TABLE_NAME,
                    new String[]{Song.COLUMN_LIST_ID},
                    Song.COLUMN_LIST_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null
            );

            SongListBean songListBean = new SongListBean(
                    id,
                    name,
                    createTime.substring(0,10),
                    songCursor.getCount()
            );

            songListBeans.add(songListBean);
        }
        return songListBeans;
    }

    /**
     * 根据歌单id获取歌曲列表
     * @param id
     * @return
     */
    public List<SingleSongBean> getSongListByListId(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        //查询歌曲id
        Cursor cursor = db.query(
                Song.TABLE_NAME,
                new String[]{Song.COLUMN_SONG_ID},
                Song.COLUMN_LIST_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        ArrayList<String> songIDs = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()){
            String songID = cursor.getString(cursor.getColumnIndex(Song.COLUMN_SONG_ID));
            songIDs.add(songID);
        }

        Player player = Player.getPlayer(null);
        List<SingleSongBean> listBySongId = player.getListBySongId(songIDs);
        return listBySongId;
    }

    /**
     * 将单曲保存到歌单中
     * @param songID
     * @param songListID
     * @return
     */
    public long insertSong(String songID,int songListID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Song.COLUMN_ID,songID);
        values.put(Song.COLUMN_LIST_ID,songListID);

        long id = db.insert(Song.TABLE_NAME, null, values);
        return id;
    }

    /**
     * 收藏歌曲
     * @param songID
     * @param songName
     * @return
     */
    public long collect(String songID,String songName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Collection.COLUMN_ID,songID);
        values.put(Collection.COLUMN_SONG_NAME,songName);

        long id = db.insert(Collection.TABLE_NAME, null, values);
        return id;
    }

    /**
     * 取消收藏
     * @param songID
     * @return
     */
    public int unCollect(String songID){
        SQLiteDatabase db = this.getWritableDatabase();

        int id = db.delete(Collection.TABLE_NAME, Collection.COLUMN_ID + "=?", new String[]{songID});
        return id;
    }

    /**
     * 获取收藏的单曲
     * @return
     */
    public List<SingleSongBean> getCollections(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Collection.TABLE_NAME,
                new String[]{Collection.COLUMN_ID, Collection.COLUMN_SONG_NAME},
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<String> ids = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(Collection.COLUMN_ID));
            ids.add(id);
        }

        Player player = Player.getPlayer(null);
        List<SingleSongBean> listBySongId = player.getListBySongId(ids);
        return listBySongId;
    }

    /**
     * 判断单曲是否被收藏
     * @param songId
     * @return
     */
    public boolean isCollected(String songId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Collection.TABLE_NAME,
                null,
                Collection.COLUMN_ID+"=?",
                new String[]{songId},
                null,
                null,
                null
        );

        return cursor.getCount() > 0;
    }

    /**
     * 将单曲添加到最近播放
     * @param songId
     * @param songName
     * @return
     */
    public long insertRecentPlay(String songId,String songName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RecentList.COLUMN_SONG_ID,songId);
        values.put(RecentList.COLUMN_SONG_NAME,songName);

        long insert = db.insert(
                RecentList.TABLE_NAME,
                null,
                values
        );
        return insert;
    }

    /**
     * 获取最近播放列表
     * @return
     */
    public List<SingleSongBean> getRecentList(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                RecentList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RecentList.COLUMN_ID + " desc"
        );

        ArrayList<String> ids = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()){
            String songId = cursor.getString(cursor.getColumnIndex(RecentList.COLUMN_SONG_ID));
            ids.add(songId);
        }

        Player player = Player.getPlayer(null);
        List<SingleSongBean> listBySongId = player.getRecentPlayBySongId(ids);

        return listBySongId;
    }
}
