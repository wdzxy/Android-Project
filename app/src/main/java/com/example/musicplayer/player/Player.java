package com.example.musicplayer.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.SingleSongBean;
import com.example.musicplayer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private static MediaPlayer mediaPlayer  =new MediaPlayer();

    private static boolean status = false;//表示当前是否有正在播放或暂停的音乐 true:有 false:无

    private static String musicPath = "";

    private static boolean playing = false;//表示当前播放器是否处于播放状态 true:正在播放 false:当前暂停

    private static Context context;

    //当前播放位置
    private static int current = -1;

    //播放列表
    private static List<SingleSongBean> list;

    //当前播放的
    private static SingleSongBean currentSong;

    //记录需要更新的控件
    private static List<TextView> songTvList = new ArrayList<>(3);//初始长度根据activity个数确定

    private static List<TextView> singerTvList = new ArrayList<>(3);

    private static List<ImageView> playIvList = new ArrayList<>(3);

    public static void setContext(Context context) {
        Player.context = context;
    }

    public static List<SingleSongBean> getList() {
        return list;
    }

    public static void setList(List<SingleSongBean> list) {
        Player.list = list;
    }

    /**
     * 设置播放的音乐的路径
     * @param path
     */
    private static void setPath(String path){
        musicPath = path;
    }

    /**
     * 开始播放
     */
    public static void start(SingleSongBean bean){
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

    public static void play(){
        if (mediaPlayer.isPlaying()){
            pause();
        }else {
            start(currentSong);
        }
    }

    /**
     * 暂停
     */
    public static void pause(){
        mediaPlayer.pause();
        current = mediaPlayer.getCurrentPosition();
        playing = false;
        update();
    }

    /**
     * 停止当前的音乐
     */
    public static void stop(){
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
    public static boolean playLast(){
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
    public static boolean playNext(){
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
    public static void addView(TextView songTV,TextView singerTV,ImageView playIV){
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

    public static void removeView(TextView songTV,TextView singerTV,ImageView playIV){
        songTvList.remove(songTV);
        singerTvList.remove(singerTV);
        playIvList.remove(playIV);
    }

    /**
     * 当播放的歌曲发生变化时，调用此方法更新注册的控件
     */
    private static void update(){
        if (songTvList.size() == singerTvList.size() && songTvList.size() == playIvList.size()){
            for (int i = 0; i < songTvList.size(); i++) {
                songTvList.get(i).setText(currentSong.getSong());
                singerTvList.get(i).setText(currentSong.getSinger());
                playIvList.get(i).setImageResource(playing ? R.drawable.pause : R.drawable.play);//需要将图标替换,true:播放中 false:未播放或暂停
            }
        }
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        Player.playing = playing;
    }
}
