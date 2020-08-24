package com.example.alarm_application;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StopwatchFragment extends Fragment {

    TextView diaplaytime;
    ImageButton start, pause, lap, reset;
    Boolean running=false;
    long pauseOffset;
    Handler handler;
    long tmillisec,tstart,tbuff,tupdate = 0L;
    int sec,min,millisec;
    LinearLayout container;
    public StopwatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        start = view.findViewById(R.id.play);
        lap = view.findViewById(R.id.add);
        pause = view.findViewById(R.id.pause);
        reset = view.findViewById(R.id.reset);
        diaplaytime=view.findViewById(R.id.Stopwatchtime);
        container = view.findViewById(R.id.container);
        handler = new Handler();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseChronometer();
            }
        });
        final ViewGroup finalContainer1 = container;
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseChronometer();
                tmillisec = 0L;
                tstart = 0L;
                tbuff = 0L;
                tupdate = 0L;
                sec = 0;
                min = 0;
                millisec = 0;
                diaplaytime.setText("00:00:00");
                pause.setVisibility(View.GONE);
                lap.setVisibility(View.GONE);
                finalContainer1.removeAllViews();
            }
        });
        final ViewGroup finalContainer = container;
        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addView = inflater.inflate(R.layout.row,null);
                TextView txtValue = (TextView)addView.findViewById(R.id.textcontent);
                txtValue.setText(diaplaytime.getText());
                finalContainer.addView(addView);
            }
        });
        return view;
    }

    private void lapfunction() {

    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tmillisec = SystemClock.uptimeMillis()-tstart;
            tupdate = tbuff + tmillisec;
            sec = (int) (tupdate/1000);
            min = sec/60;
            sec = sec%60;
            millisec = (int) (tupdate%100);
            diaplaytime.setText(String.format("%02d",min)+":"+String.format("%02d",sec)+":"
                    +String.format("%02d",millisec));
            handler.postDelayed(this,60);
        }
    };

    public void startChronometer(){
        if(!running)
        {
            pause.setVisibility(View.VISIBLE);
            lap.setVisibility(View.VISIBLE);
            tstart = SystemClock.uptimeMillis();
            handler.postDelayed(runnable,0);
            running = true;
        }
    }

    public void pauseChronometer(){
        if(running)
        {
            tbuff+=tmillisec;
            handler.removeCallbacks(runnable);
            running = false;
        }
    }

    public void resetChronometer(){

    }

}