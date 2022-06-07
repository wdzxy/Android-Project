package com.example.listener;

import android.media.MediaPlayer;

import com.example.musicplayer.player.Player;

public class OnCompletionListener implements MediaPlayer.OnCompletionListener {

    private Player player;

    private int[] playTypes = PlayTypes.TYPES;

    private int index;

    private int playType;//表示播放的状态（单曲循环、循环。。。。。。）

    public OnCompletionListener(Player player) {
        this.player = player;
        playType = playTypes[0];
        index = 0;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public int changeType(){
        if (index+1 < playTypes.length){
            playType = playTypes[++index];
        }else {
            playType = playTypes[0];
            index = 0;
        }
        return playType;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
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
    }
}
