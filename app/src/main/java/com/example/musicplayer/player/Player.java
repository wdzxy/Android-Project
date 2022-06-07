package com.example.musicplayer.player;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.AlbumBean;
import com.example.bean.PathBean;
import com.example.bean.SingerBean;
import com.example.bean.SingleSongBean;
import com.example.db.DBHelper;
import com.example.listener.OnCompletionListener;
import com.example.musicplayer.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Player {

    private static Player player;

    private MediaPlayer mediaPlayer;

    private boolean status = false;//表示当前是否有正在播放或暂停的音乐 true:有 false:无

    private String musicPath = "";//当前播放的音乐的路径

    private boolean playing = false;//表示当前播放器是否处于播放状态 true:正在播放 false:当前暂停

    private Context context;

    //当前播放位置
    private int current = -1;

    //播放列表
    private List<SingleSongBean> list;
    
    //本地专辑列表
    private Map<String ,List<SingleSongBean>> albumMap;

    //按歌手分组
    private Map<String,List<SingleSongBean>> singerMap;

    //按目录分组
    private Map<String,List<SingleSongBean>> pathMap;

    //当前播放的
    private SingleSongBean currentSong;

    //记录需要更新的控件
    private List<TextView> songTvList;

    private List<TextView> singerTvList;

    private List<Button> playIvList;

    private OnCompletionListener listener;

    private DBHelper dbHelper;

    private Player(Context context){
        this.context = context;
        mediaPlayer = new MediaPlayer();
        list = new ArrayList<>();
        albumMap = new HashMap<>();
        singerMap = new HashMap<>();
        pathMap = new HashMap<>();
        songTvList = new ArrayList<>(3);//初始长度根据activity个数确定
        singerTvList = new ArrayList<>(3);
        playIvList = new ArrayList<>(3);
        listener = new OnCompletionListener(this);
        dbHelper = new DBHelper(context);
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

    public OnCompletionListener getListener() {
        return listener;
    }

    /**
     * 整合专辑信息
     * @return
     */
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
     * 整合歌手信息
     * @return
     */
    public List<SingerBean> getSingerList(){
        ArrayList<SingerBean> singerBeans = new ArrayList<>();
        Iterator<String> iterator = singerMap.keySet().iterator();
        while (iterator.hasNext()){
            String singer = iterator.next();
            int count = singerMap.get(singer).size();
            SingerBean singerBean = new SingerBean(
                    singer,
                    count
            );
            singerBeans.add(singerBean);
        }
        return singerBeans;
    }

    public List<PathBean> getPathList(){
        ArrayList<PathBean> pathBeans = new ArrayList<>();
        Iterator<String> iterator = pathMap.keySet().iterator();
        while (iterator.hasNext()){
            String path = iterator.next();
            int count = pathMap.get(path).size();
            PathBean pathBean = new PathBean(
                    path,
                    count
            );
            pathBeans.add(pathBean);
        }
        return pathBeans;
    }

    /**
     * 根据专辑名获取歌曲列表
     * @param album
     * @return
     */
    public List<SingleSongBean> getListByAlbum(String album){
        return albumMap.get(album);
    }

    /**
     * 根据歌手获取歌曲列表
     * @param singer
     * @return
     */
    public List<SingleSongBean> getListBySinger(String singer){
        return singerMap.get(singer);
    }

    /**
     * 根据文件夹获取歌曲列表
     * @param path
     * @return
     */
    public List<SingleSongBean> getListByPath(String path){
        return pathMap.get(path);
    }

    /**
     * 根据歌曲的id获取歌曲列表，id为数据库中存储的id
     * @param ids
     * @return
     */
    public List<SingleSongBean> getListBySongId(List<String> ids){
        ArrayList<SingleSongBean> singleSongBeans = new ArrayList<>();

        for (SingleSongBean bean :
                list) {
            if (ids.contains(bean.getID())){
                singleSongBeans.add(bean);
            }
        }

        return singleSongBeans;
    }

    /**
     * 加载本地音乐，并根据专辑、歌手、文件夹分组
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
            String ID = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

            //将一行数据封装到对象中
            SingleSongBean singleSongBean = new SingleSongBean(
                    sid,
                    song,
                    singer,
                    time,
                    path,
                    album,
                    ID
            );
            list.add(singleSongBean);

            //按专辑分组
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

            //按歌手分组
            if (singerMap.containsKey(singer)){
                List<SingleSongBean> singleSongBeans = singerMap.get(singer);
                singleSongBeans.add(singleSongBean);
            }else {
                ArrayList<SingleSongBean> singleSongBeans = new ArrayList<>();
                singleSongBeans.add(singleSongBean);
                singerMap.put(singer,singleSongBeans);
            }

            //按文件夹分组
            //去掉path中的歌名
            String[] split = path.split("\\/");
            String pathWithoutSongName = "";
            for (int i=0;i<split.length-1;i++) {
                pathWithoutSongName += split[i]+"/";
            }
            if (pathMap.containsKey(pathWithoutSongName)){
                List<SingleSongBean> singleSongBeans = pathMap.get(pathWithoutSongName);
                singleSongBeans.add(singleSongBean);
            }else {
                ArrayList<SingleSongBean> singleSongBeans = new ArrayList<>();
                singleSongBeans.add(singleSongBean);
                pathMap.put(pathWithoutSongName,singleSongBeans);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //将当前单曲添加到最近播放，使用其他线程防止主线程卡死
                    dbHelper.insertRecentPlay(currentSong.getID(),currentSong.getSong());
                }
            }).start();
            mediaPlayer = MediaPlayer.create(context,Uri.parse(musicPath));
            mediaPlayer.setOnCompletionListener(listener);
            mediaPlayer.start();
            status = true;
        }else{
            mediaPlayer.start();
        }
        playing = true;
        update();
    }

    /**
     * 从头开始播放
     */
    public void replay(){
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
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
                update();
            }
            playing = false;
            status = false;
        }
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public int getCurrent(){
        current = mediaPlayer.getCurrentPosition();
        return current;
    }

    public void seekTo(int position){
        mediaPlayer.pause();
        mediaPlayer.seekTo(position);
        current = position;
        mediaPlayer.start();
    }

    public int getAudioSessionId(){
        return mediaPlayer.getAudioSessionId();
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
     * 播放列表中的第一首
     */
    public void playFirst(){
        if (list != null && list.size() > 1){
            currentSong = list.get(0);
            stop();
            start(currentSong);
        }
    }

    /**
     * 随机播放
     */
    public void randomPlay(){
        if (list != null && list.size() > 1){
            Random random = new Random();
            int nextInt = random.nextInt(list.size());
            currentSong = list.get(nextInt);
            stop();
            start(currentSong);
        }
    }

    /**
     * 在activity创建时将底部播放器界面的歌曲名，歌手名，播放状态的控件加入队列
     * @param songTV
     * @param singerTV
     * @param playIV
     */
    public void addView(TextView songTV,TextView singerTV,Button playIV){
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

        playIV.setBackgroundResource(playing ? R.drawable.ic_pause : R.drawable.ic_play);//需要将图标替换,true:播放中 false:未播放或暂停
    }

    public void addSongTV(TextView view){
        songTvList.add(view);

        String song = "";

        if (currentSong != null) {
            song = currentSong.getSong();
        }

        if (!song.equals("")) {
            view.setText(song);
        }
    }

    public void addSingerTV(TextView view){
        singerTvList.add(view);

        String singer = "";

        if (currentSong != null) {
            singer = currentSong.getSong();
        }

        if (!singer.equals("")) {
            view.setText(singer);
        }
    }

    public void addPlayBtn(Button button){
        playIvList.add(button);

        button.setBackgroundResource(playing ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    public void removeView(TextView songTV,TextView singerTV,ImageView playIV){
        songTvList.remove(songTV);
        singerTvList.remove(singerTV);
        playIvList.remove(playIV);
    }

    /**
     * 当播放的歌曲发生变化时，调用此方法更新注册的控件
     */
    public void update(){

        int songs = songTvList.size();
        int singers = singerTvList.size();
        int plays = playIvList.size();

        for (int i = 0; i < songs || i < singers || i < plays; i++) {
            if (i < songs){
                songTvList.get(i).setText(currentSong.getSong());
            }
            if (i < singers){
                singerTvList.get(i).setText(currentSong.getSinger());
            }
            if (i < plays){
                playIvList.get(i).setBackgroundResource(playing ? R.drawable.ic_pause : R.drawable.ic_play);//需要将图标替换,true:播放中 false:未播放或暂停
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

    public boolean getStatus() {
        return status;
    }

    public SingleSongBean getCurrentSong(){
        return currentSong;
    }
}
