package com.example.musicplayer.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bean.SingleSongBean;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.broadcast.MyBroadcastReceiver;
import com.example.musicplayer.player.Player;

public class Notification {

    private final String id ="channel_1";//⾃定义设置ID通道描述属性
    private final String description ="123";//通知栏管理重要提⽰消息声⾳设定

    private Context context;
    PendingIntent prevPendingIntent,nextPendingIntent,pausePendingIntent,pendingIntent;

    public Notification(Context context){
        this.context = context;
    }

    public void sendnotification(){
        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel =new NotificationChannel(id,"123", importance);
        manager.createNotificationChannel(mChannel);

        //前一首
        Intent prevIntent = new Intent(context, MyBroadcastReceiver.class);
        prevIntent.setAction("com.example.musicplayer.broadcast.PREV");
        prevPendingIntent =
                PendingIntent.getBroadcast(context, 0, prevIntent, 0);

        //暂停
        Intent pauseIntent = new Intent(context, MyBroadcastReceiver.class);
        pauseIntent.setAction("com.example.musicplayer.broadcast.PAUSE");
        pausePendingIntent =
                PendingIntent.getBroadcast(context, 0, pauseIntent, 0);

        //下一首
        Intent nextIntent = new Intent(context, MyBroadcastReceiver.class);
        nextIntent.setAction("com.example.musicplayer.broadcast.NEXT");
        nextPendingIntent =
                PendingIntent.getBroadcast(context, 0, nextIntent, 0);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Player player = Player.getPlayer(context);

        SingleSongBean singleSongBean = player.getCurrentSong();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_collect)
                //按钮
                .addAction(R.drawable.ic_prev,"Previous",prevPendingIntent)
                .addAction(player.isPlaying()?R.drawable.ic_pause:R.drawable.ic_play,"pause",pausePendingIntent)
                .addAction(R.drawable.ic_next,"Next",nextPendingIntent)
                //style
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1 /* #1: pause button */))
                //标题和文字
                .setContentTitle("singleSongBean.getSong()")
                .setContentText("singleSongBean.getSinger()")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());
    }

}
