package com.example.alarm_application;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class AlarmService extends Service {
    private Vibrator vibrator;
    private MediaPlayer alarm,alarm1,alarm2 ;
    @Nullable


    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        alarm = MediaPlayer.create(this,R.raw.red);
        alarm.setLooping(true);
        alarm1 = MediaPlayer.create(this,R.raw.alarm_clock);
        alarm1.setLooping(true);
        alarm2 = MediaPlayer.create(this,R.raw.alarm_for_iphone_5);
        alarm2.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this,QuizActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"alarmapp")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                .setContentTitle("alarmTitle")
                .setContentText("Alarm...Alarm...Click to off the alarm")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(0,builder.build());

        SharedPreferences sharedPreferences = getSharedPreferences("musicname",MODE_PRIVATE);
        String Music = sharedPreferences.getString("music","Default");

        if(Music.equals("Music1"))
        {
            alarm1.start();
        }
        if(Music.equals("Music2"))
        {
            alarm2.start();
        }
        if(Music.equals("Default"))
        {
            alarm.start();
        }

        vibrator.vibrate(1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarm.stop();
        alarm1.stop();
        alarm2.stop();
        vibrator.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
