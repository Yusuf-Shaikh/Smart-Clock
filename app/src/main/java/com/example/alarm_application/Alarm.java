package com.example.alarm_application;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getBooleanExtra("RECURRING", false)==false) {
            sendnotification(context);
        }
        else
        {
            if (alarmIsToday(intent)) {
                sendnotification(context);
            }
        }
    }

    private void sendnotification(Context context){
        Intent intentService = new Intent(context, AlarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentService);
        }
        else{
            context.startService(intentService);
        }
    }

    private boolean alarmIsToday(Intent intent) {
        boolean Today = false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra("MONDAY", false))
                { Today = true;}
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra("TUESDAY", false))
                { Today = true;}
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra("WEDNESDAY", false))
                { Today = true;}
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra("THURSDAY", false))
                { Today = true;}
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra("FRIDAY", false))
                { Today = true;}
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra("SATURDAY", false))
                { Today = true;}
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra("SUNDAY", false))
                { Today = true;}
        }
        return Today;
    }
}