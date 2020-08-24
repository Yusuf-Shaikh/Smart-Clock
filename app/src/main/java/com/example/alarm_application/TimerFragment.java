package com.example.alarm_application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class TimerFragment extends Fragment {


    boolean timerrunning;
    ImageButton start,pause,reset;
    Button settime;
    EditText gettime;
    TextView displaytime;

     long mStartTimeInMillis;
    CountDownTimer countDownTimer;
    long mTimeLeftInMillis;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_timer, container, false);


        start=view.findViewById(R.id.timerplay);
        pause=view.findViewById(R.id.timerpause);
        reset=view.findViewById(R.id.timerreset);
        settime=view.findViewById(R.id.setTimer);
        gettime=view.findViewById(R.id.getTime);
        displaytime=view.findViewById(R.id.timerdisplaytime);

        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = gettime.getText().toString();
                if(input.length()==0)
                {
                    Toast.makeText(getActivity(), "Enter valid time...", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input)*60000;
                if(millisInput == 0)
                {
                    Toast.makeText(getActivity(), "Enter valid number...", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                gettime.setText("");
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timerrunning){
                    starttimer();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerrunning){
                    pausetimer();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resettimer();
            }
        });
        updateCountDownText();
        return view;
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resettimer();
    }

    private void resettimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        start.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.VISIBLE);
        settime.setVisibility(View.VISIBLE);
        gettime.setVisibility(View.VISIBLE);
    }

    private void pausetimer() {
        countDownTimer.cancel();
        timerrunning = false;
        reset.setVisibility(View.VISIBLE);
        settime.setVisibility(View.VISIBLE);
        gettime.setVisibility(View.VISIBLE);
    }

    private void starttimer() {
        pause.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timerrunning = false;
                displaytime.setText("00:00");
                start.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
            }
        }.start();
        timerrunning = true;
        reset.setVisibility(View.INVISIBLE);
        settime.setVisibility(View.INVISIBLE);
        gettime.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownText() {
        int hours = (int)(mTimeLeftInMillis/1000)/3600;
        int mins = (int) ((mTimeLeftInMillis /1000)%3600)/60;
        int secs = (int) (mTimeLeftInMillis /1000)%60;
        String timeleftFormatted;
        if(hours>0)
        {
            timeleftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d",hours,mins,secs);
        }
        else
        {
            timeleftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d",mins,secs);
        }
        displaytime.setText(timeleftFormatted);
    }
}

