package com.example.listener;

import android.telephony.mbms.MbmsErrors;
import android.view.View;

import com.example.musicplayer.R;
import com.example.musicplayer.player.Player;

public class BtnListener implements View.OnClickListener {

    private Player player;

    private int btnType;

    public BtnListener(Player player,int btnType){
        this.player = player;
        this.btnType = btnType;
    }

    @Override
    public void onClick(View v) {
        switch (btnType){
            case BtnTypes.PLAY:
                player.play();
                break;
            case BtnTypes.NEXT:
                boolean bn = player.playNext();
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
                break;
        }
    }
}
