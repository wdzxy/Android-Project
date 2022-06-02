package com.example.listener;

import android.view.View;

import com.example.musicplayer.R;
import com.example.musicplayer.player.Player;

public class BtnListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.local_music_bottom_iv_play:
                Player.play();
                break;
            case R.id.local_music_bottom_iv_next:
                boolean bn = Player.playNext();
                break;
            case R.id.local_music_bottom_iv_last:
                boolean bl = Player.playLast();
                break;
        }
    }
}