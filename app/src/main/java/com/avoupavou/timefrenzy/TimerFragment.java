package com.avoupavou.timefrenzy;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int UPDATE_INTERVAL = 1;

    private static int READY_STATE = 0;
    private static int RUNNING_STATE = 1;
    private static int STOPPED_STATE = 2;

    private  TextView timerTextView;
    private  TextView timerMillisTextView;
    private  TextView bestScoreTextView;

    private int gameState = READY_STATE;
    private Timer mainTimer;
    private CountingTask countingTask;
    private int bestScore = 1000;

    public Handler mHandler;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MediaPlayer clickAudio;


    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mHandler =  new Handler() {

            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                int sec  = message.getData().getInt("sec");
                int millis = message.getData().getInt("millis");
                timerTextView.setText(String.format(Locale.ENGLISH,"%01d",sec));
                timerMillisTextView.setText(String.format(Locale.ENGLISH,"%03d",millis));
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        //Demo code for chrono
        //TODO remove from here and populate a proper function
        timerTextView = view.findViewById(R.id.text_timer_seconds);
        timerMillisTextView = view.findViewById(R.id.text_timer_millis);
        bestScoreTextView = view.findViewById(R.id.text_score_value);

        View mainView = view.findViewById(R.id.mainView);
        clickAudio = MediaPlayer.create(this.getActivity(),R.raw.click);
        clickAudio.setVolume(0.5f,0.5f);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    screenTouched();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                }
                return true;
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(gameState == RUNNING_STATE) mainTimer.cancel();
    }


    private void startTimer(){
        countingTask = new CountingTask(UPDATE_INTERVAL,mHandler);
        mainTimer = new Timer();
        mainTimer.scheduleAtFixedRate(countingTask,UPDATE_INTERVAL,UPDATE_INTERVAL);
    }


    public void screenTouched(){

        if(gameState == READY_STATE){
            startTimer();
            gameState = RUNNING_STATE;
        }else if (gameState == RUNNING_STATE){
            clickAudio.start();
            mainTimer.cancel();
            Bundle b = countingTask.getLastMoment();
            int score = calculateScore(b);
            if(score < bestScore) updateMaxScore(score);
            gameState = STOPPED_STATE;
        }else if(gameState == STOPPED_STATE){
            timerTextView.setText(String.format(Locale.ENGLISH,"%01d",0));
            timerMillisTextView.setText(String.format(Locale.ENGLISH,"%03d",0));
            gameState = READY_STATE;
        }
    }

    private void updateMaxScore(int score) {
        bestScoreTextView.setText(String.valueOf(score));
        bestScore = score;
    }

    private int calculateScore(Bundle time){
        int score = 0;

        int ms = time.getInt(CountingTask.MILLISECONDS);
        int sec = time.getInt(CountingTask.SECONDS);
        if(ms < 500 && sec >= 1){
            score = ms;
        }else{
            score = 1000-ms;
        }
        return score;
    }


}
