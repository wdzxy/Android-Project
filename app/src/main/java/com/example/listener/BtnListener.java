package com.example.listener;

import android.app.NotificationManager;
import android.content.Context;
import android.telephony.mbms.MbmsErrors;
import android.view.View;

import com.example.bean.SingleSongBean;
import com.example.db.DBHelper;
import com.example.musicplayer.R;
import com.example.musicplayer.notification.Notification;
import com.example.musicplayer.player.Player;

public class BtnListener implements View.OnClickListener {

    private Player player;

    private int btnType;

    private Context context;

    private SingleSongBean currentSong;

    public BtnListener(Player player,int btnType){
        this.player = player;
        this.btnType = btnType;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCurrentSong(SingleSongBean currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    public void onClick(View v) {
        switch (btnType){
            case BtnTypes.PLAY:
                player.play();
                break;
            case BtnTypes.NEXT:
                int playType = player.getListener().getPlayType();
                player.update();
                switch (playType){
                    case PlayTypes.ORDER:
                        player.playNext();
                        break;
                    case PlayTypes.LOOP_ONE:
                        player.replay();
                        break;
                    case PlayTypes.LOOP:
                        boolean b = player.playNext();//返回false说明已经是播放完最后一首了，回到第一首
                        if (!b){
                            player.playFirst();
                        }
                        break;
                    case PlayTypes.RANDOM:
                        player.randomPlay();
                        break;
                }
                //boolean bn = player.playNext();
                break;
            case BtnTypes.LAST:
                boolean bl = player.playLast();
                break;
            case BtnTypes.FORWARD:
                player.seekTo(player.getCurrent()+1000);
                break;
            case BtnTypes.REWARD:
                int current = player.getCurrent();
                if (current < 1000){
                    current = 0;
                }else {
                    current -= 1000;
                }
                player.seekTo(current);
                break;
            case BtnTypes.PLAY_STATUS_CHANGE:
                int type = player.getListener().changeType();
                int drawableId;
                if (type == PlayTypes.LOOP_ONE){
                    drawableId = R.drawable.ic_loop_one;
                }else if (type == PlayTypes.ORDER){
                    drawableId = R.drawable.ic_list_order;
                }else if (type == PlayTypes.LOOP){
                    drawableId = R.drawable.ic_loop;
                }else {
                    drawableId = R.drawable.ic_random;
                }
                v.setBackgroundResource(drawableId);
                break;
            case BtnTypes.COLLECT:
                //判断当前歌曲是否已收藏
                DBHelper dbHelper = new DBHelper(context);
                currentSong = player.getCurrentSong();
                boolean collected = dbHelper.isCollected(currentSong.getID());
                if (collected){
                    //取消收藏
                    dbHelper.unCollect(currentSong.getID());
                    v.setBackgroundResource(R.drawable.ic_un_clollected);
                }else {
                    //收藏
                    dbHelper.collect(currentSong.getID(),currentSong.getSong());
                    v.setBackgroundResource(R.drawable.ic_collect);
                }
                break;
        }
    }
}
