package com.example.listener;

import android.media.MediaPlayer;

import com.example.musicplayer.player.Player;

public class OnCompletionListener implements MediaPlayer.OnCompletionListener {

    private Player player;

    private int playType = PlayTypes.ORDER;//表示播放的状态（单曲循环、循环。。。。。。）

    public OnCompletionListener(Player player) {
        this.player = player;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        player.update();
        switch (playType){
            case PlayTypes.ORDER:
                player.replay();
                break;
        }
    }
}
