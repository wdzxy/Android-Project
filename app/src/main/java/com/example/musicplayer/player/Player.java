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

    //底部播放器属性
    private static String song = "";

    private static String singer = "";

    private static boolean playing = false;//表示当前播放器是否处于播放状态 true:正在播放 false:当前未播放或暂停

    private static Context context;

    //记录需要更新的控件
    private static List<TextView> songTvList = new ArrayList<>(3);//初始长度根据activity个数确定

    private static List<TextView> singerTvList = new ArrayList<>(3);

    private static List<ImageView> playIvList = new ArrayList<>(3);

    public static void setContext(Context context) {
        Player.context = context;
    }

    /**
     * 设置播放的音乐的路径
     * @param path
     */
    public static void setPath(String path){
        musicPath = path;
    }

    /**
     * 开始播放
     */
    public static void start(){
        if (mediaPlayer != null && !status){
            update();
           mediaPlayer.reset();
           mediaPlayer = MediaPlayer.create(context,Uri.parse(musicPath));
           mediaPlayer.start();
        }
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

        if (!song.equals("")) {
            songTV.setText(song);
        }
        if (!singer.equals("")) {
            singerTV.setText(singer);
        }
        playIV.setImageResource(playing ? R.drawable.local_music : R.drawable.local_music);//需要将图标替换,true:播放中 false:未播放或暂停
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
                songTvList.get(i).setText(song);
                singerTvList.get(i).setText(singer);
                playIvList.get(i).setImageResource(playing ? R.drawable.local_music : R.drawable.local_music);//需要将图标替换,true:播放中 false:未播放或暂停
            }
        }
    }

    public static String getSong() {
        return song;
    }

    public static void setSong(String song) {
        Player.song = song;
    }

    public static String getSinger() {
        return singer;
    }

    public static void setSinger(String singer) {
        Player.singer = singer;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        Player.playing = playing;
    }
}
