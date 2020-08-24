package com.example.alarm_application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class AlarmFragment extends Fragment {

    TextView TimeDisplay;
    Button Set,PickTime,cancel;
    Integer mhour,mmin;
    Calendar c;
    CheckBox recurring;
    CheckBox mon;
    CheckBox tue;
    CheckBox wed;
    CheckBox thu;
    CheckBox fri;
    CheckBox sat;
    CheckBox sun;
    LinearLayout recurringOptions;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String music="Default";

    public AlarmFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        recurring = view.findViewById(R.id.fragment_createalarm_recurring);
        recurringOptions = view.findViewById(R.id.fragment_createalarm_recurring_options);
        mon = view.findViewById(R.id.fragment_createalarm_checkMon);
        tue = view.findViewById(R.id.fragment_createalarm_checkTue);
        wed = view.findViewById(R.id.fragment_createalarm_checkWed);
        thu = view.findViewById(R.id.fragment_createalarm_checkThu);
        fri = view.findViewById(R.id.fragment_createalarm_checkFri);
        sat = view.findViewById(R.id.fragment_createalarm_checkSat);
        sun = view.findViewById(R.id.fragment_createalarm_checkSun);
        TimeDisplay = view.findViewById(R.id.displaytime);
        Set = view.findViewById(R.id.Set);
        cancel = view.findViewById(R.id.cancel);
        PickTime = view.findViewById(R.id.picktime);
        radioGroup = view.findViewById(R.id.radiogrp);

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        createnotificationChannel();

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.INVISIBLE);
                }
            }
        });

        PickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mhour = hourOfDay;
                        mmin = minute;
                        TimeDisplay.setText(mhour+" : "+mmin);
                        c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.SECOND,0);
                    }
                },hour,min,android.text.format.DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        });

        Set.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() <= 0) {
                    Toast.makeText(getContext(), "Choose Radio Button Please", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int radioID = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(radioID);
                    music = radioButton.getText().toString();
                    savemusic();
                }

                Intent intent = new Intent(getActivity(),Alarm.class);
                intent.putExtra("RECURRING", recurring.isChecked());
                intent.putExtra("MONDAY", mon.isChecked());
                intent.putExtra("TUESDAY", tue.isChecked());
                intent.putExtra("WEDNESDAY", wed.isChecked());
                intent.putExtra("THURSDAY", thu.isChecked());
                intent.putExtra("FRIDAY", fri.isChecked());
                intent.putExtra("SATURDAY", sat.isChecked());
                intent.putExtra("SUNDAY", sun.isChecked());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,0);
                if(c.before(Calendar.getInstance()))
                {
                    c.add(Calendar.DATE,1);
                }
                AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                if(recurring.isChecked())
                {
                    final long RUN_DAILY = 24 * 60 * 60 * 1000;
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),RUN_DAILY,pendingIntent);
                    Toast.makeText(getContext(), "Repeating Alarm set for "+TimeDisplay.getText(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
                    Toast.makeText(getContext(), "Alarm set for "+TimeDisplay.getText(), Toast.LENGTH_SHORT).show();
                }
                savemusic();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void savemusic() {
        SharedPreferences sharedpreferences = getContext().getSharedPreferences("musicname",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("music",music);
        editor.apply();
    }

    private void createnotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "alarmappChannel";
            String description = "Channel for alram app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("alarmapp",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}