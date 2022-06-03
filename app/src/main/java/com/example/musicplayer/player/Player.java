package com.example.musicplayer.player;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.AlbumBean;
import com.example.bean.SingleSongBean;
import com.example.musicplayer.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Player {

    private static Player player;

    private MediaPlayer mediaPlayer;

    private boolean status = false;//表示当前是否有正在播放或暂停的音乐 true:有 false:无

    private String musicPath = "";

    private boolean playing = false;//表示当前播放器是否处于播放状态 true:正在播放 false:当前暂停

    private Context context;

    //当前播放位置
    private int current = -1;

    //播放列表
    private List<SingleSongBean> list;
    
    //本地专辑列表
    private Map<String ,List<SingleSongBean>> albumMap;

    //当前播放的
    private SingleSongBean currentSong;

    //记录需要更新的控件
    private List<TextView> songTvList;

    private List<TextView> singerTvList;

    private List<ImageView> playIvList;

    private Player(Context context){
        this.context = context;
        mediaPlayer = new MediaPlayer();
        list = new ArrayList<>();
        albumMap = new HashMap<>();
        songTvList = new ArrayList<>(3);//初始长度根据activity个数确定
        singerTvList = new ArrayList<>(3);
        playIvList = new ArrayList<>(3);
        loadData();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<SingleSongBean> getSingleSongList() {
        return list;
    }

    public void setList(List<SingleSongBean> list) {
        this.list = list;
    }

    public List<AlbumBean> getAlbumList(){
        ArrayList<AlbumBean> albumBeans = new ArrayList<>(albumMap.size());
        //遍历专辑集合
        Iterator<String> iterator = albumMap.keySet().iterator();
        while (iterator.hasNext()){
            String albumName = iterator.next();
            int count = albumMap.get(albumName).size();
            AlbumBean albumBean = new AlbumBean(
                    albumName,
                    count
            );
            albumBeans.add(albumBean);
        }
        return albumBeans;
    }

    /**
     * 加载本地音乐，并根据专辑分组
     */
    public void loadData(){
        //获取ContentResolver
        ContentResolver contentResolver = context.getContentResolver();
        //获取本地音乐存储的地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        //查询地址
        Cursor cursor = contentResolver.query(uri, null, selection, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //遍历cursor
        int id = 0;
        while (cursor.moveToNext()){
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            String time = simpleDateFormat.format(new Date(duration));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

            //将一行数据封装到对象中
            SingleSongBean singleSongBean = new SingleSongBean(
                    sid,
                    song,
                    singer,
                    time,
                    path,
                    album
            );
            list.add(singleSongBean);

            //判断此单曲的专辑是否已经在map中
            if (albumMap.containsKey(album)) {
                //已经创建过这个专辑，直接获取专辑并将单曲添加进去
                List<SingleSongBean> singleSongBeans = albumMap.get(album);
                singleSongBeans.add(singleSongBean);
            }else {
                //当前单曲的专辑没有创建过，创建新的专辑
                ArrayList<SingleSongBean> singleSongBeans = new ArrayList<>();
                singleSongBeans.add(singleSongBean);
                albumMap.put(album,singleSongBeans);
            }
        }
    }

    /**
     * 设置播放的音乐的路径
     * @param path
     */
    private void setPath(String path){
        musicPath = path;
    }

    /**
     * 开始播放
     */
    public void start(SingleSongBean bean){
        if (bean == null){
            return;
        }
        currentSong = bean;
        setPath(bean.getPath());
        if (mediaPlayer != null && !status){
            mediaPlayer = MediaPlayer.create(context,Uri.parse(musicPath));
            mediaPlayer.start();
            status = true;
        }else{
            mediaPlayer.seekTo(current);
            mediaPlayer.start();
        }
        playing = true;
        update();
    }

    /**
     * 用于底部播放器的播放按钮
     */
    public void play(){
        if (mediaPlayer.isPlaying()){
            pause();
        }else {
            start(currentSong);
        }
    }

    /**
     * 暂停
     */
    public void pause(){
        mediaPlayer.pause();
        current = mediaPlayer.getCurrentPosition();
        playing = false;
        update();
    }

    /**
     * 停止当前的音乐
     */
    public void stop(){
        if (mediaPlayer != null){
            if (status){
                mediaPlayer.pause();//暂停
                mediaPlayer.seekTo(0);//进度条退回到0
                mediaPlayer.stop();//停止
                mediaPlayer.release();
            }
            playing = false;
            status = false;
        }
    }

    /**
     * 播放上一首，存在上一首时返回true，没有上一首时返回false
     */
    public boolean playLast(){
        int index = list.indexOf(currentSong);
        if (index == 0){
            return false;
        }else {
            currentSong = list.get(index-1);
            stop();
            start(currentSong);
            return true;
        }
    }

    /**
     * 播放下一首
     */
    public boolean playNext(){
        int index = list.indexOf(currentSong);
        if (index == list.size()-1){
            return false;
        }else {
            currentSong = list.get(index+1);
            stop();
            start(currentSong);
            return true;
        }
    }

    /**
     * 在activity创建时将底部播放器界面的歌曲名，歌手名，播放状态的控件加入队列
     * @param songTV
     * @param singerTV
     * @param playIV
     */
    public void addView(TextView songTV,TextView singerTV,ImageView playIV){
        songTvList.add(songTV);
        singerTvList.add(singerTV);
        playIvList.add(playIV);
        String song = "";
        String singer = "";

        if (currentSong != null) {
            song = currentSong.getSong();
            singer = currentSong.getSinger();
        }

        if (!song.equals("")) {
            songTV.setText(song);
        }
        if (!singer.equals("")) {
            singerTV.setText(singer);
        }
        playIV.setImageResource(playing ? R.drawable.pause : R.drawable.play);//需要将图标替换,true:播放中 false:未播放或暂停
    }

    public void removeView(TextView songTV,TextView singerTV,ImageView playIV){
        songTvList.remove(songTV);
        singerTvList.remove(singerTV);
        playIvList.remove(playIV);
    }

    /**
     * 当播放的歌曲发生变化时，调用此方法更新注册的控件
     */
    private void update(){
        if (songTvList.size() == singerTvList.size() && songTvList.size() == playIvList.size()){
            for (int i = 0; i < songTvList.size(); i++) {
                songTvList.get(i).setText(currentSong.getSong());
                singerTvList.get(i).setText(currentSong.getSinger());
                playIvList.get(i).setImageResource(playing ? R.drawable.pause : R.drawable.play);//需要将图标替换,true:播放中 false:未播放或暂停
            }
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public static Player getPlayer(Context context){
        if (player == null){
            synchronized (Player.class){
                if (player == null){
                    player = new Player(context);
                }
            }
        }
        return player;
    }
}
