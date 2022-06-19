package com.example.musicplayer.notification;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.musicplayer.MainActivity;
import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.player.Player;

public class Notification {
    private Context context;

    public Notification (Context context){
        this.context = context;
    }

    public void sendNotification(){
        final String id ="channel_20";//⾃定义设置ID通道描述属性
        final String description ="123";//通知栏管理重要提⽰消息声⾳设定

        //前一首
        Intent prevIntent = new Intent();
        prevIntent.setAction("PREV");
        PendingIntent prevPendingIntent =
                PendingIntent.getBroadcast(context, 0, prevIntent, 0);

        //暂停
        Intent pauseIntent = new Intent();
        pauseIntent.setAction("PAUSE");
        PendingIntent pausePendingIntent =
                PendingIntent.getBroadcast(context, 0, pauseIntent, 0);

        //下一首
        Intent nextIntent = new Intent();
        nextIntent.setAction("NEXT");
        PendingIntent nextPendingIntent =
                PendingIntent.getBroadcast(context, 0, nextIntent, 0);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Player player = Player.getPlayer(context);

        TextView singerTV = ((Activity)context).findViewById(R.id.local_music_bottom_tv_singer);
        TextView songTV =  ((Activity)context).findViewById(R.id.local_music_bottom_tv_song);

        String song = "";
        String singer = "";
        if (songTV == null || singerTV == null){
            song = player.getCurrentSong().getSong();
            singer = player.getCurrentSong().getSinger();
        }else {
            song = songTV.getText().toString();
            singer = singerTV.getText().toString();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_splash)
                //按钮
                .addAction(R.drawable.ic_prev,"Previous",prevPendingIntent)
                .addAction(player.isPlaying()?R.drawable.ic_pause:R.drawable.ic_play,"PAUSE",pausePendingIntent)
                .addAction(R.drawable.ic_next,"Next",nextPendingIntent)
                //style
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1 /* #1: pause button */))
                //标题和文字
                .setContentTitle(singer)
                .setContentText(song)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(null)
                .setContentIntent(pendingIntent);
        builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());
    }
}
