package com.example.musicplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.musicplayer.player.Player;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private Player player ;

    private static final String NEXT = "com.example.musicplayer.broadcast.NEXT";
    private static final String PREV = "com.example.musicplayer.broadcast.PREV";
    private static final String PAUSE = "com.example.musicplayer.broadcast.PAUSE";

    @Override
    public void onReceive(Context context, Intent intent) {
        player = Player.getPlayer(context);
        switch (intent.getAction()){
            case NEXT:
                player.playNext();
                break;
            case PREV:
                player.playLast();
                break;
            case PAUSE:
                player.play();
        }
    }
}
