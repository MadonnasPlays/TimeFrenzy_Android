package com.avoupavou.timefrenzy.levels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avoupavou.timefrenzy.CountingTask;
import com.avoupavou.timefrenzy.R;
import com.google.gson.Gson;

import java.io.Console;
import java.io.Serializable;
import java.util.Locale;
import java.util.Timer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LevelFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LevelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "level";
    private static final int UPDATE_INTERVAL = 1;

    private static int READY_STATE = 0;
    private static int RUNNING_STATE = 1;
    private static int STOPPED_STATE = 2;


    private OnFragmentInteractionListener mListener;
    private TextView mTimerTextView;
    private TextView mTimerMillisTextView;
    private MediaPlayer mClickAudio;
    private int mGameState;
    private CountingTask mCountingTask;
    private Timer mainTimer;
    private Level mLevel;
    private Handler mLevelCounterHandler;
    private TextView mLevelTitle;

    public LevelFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param level Level to populate fragment.
     * @return A new instance of fragment LevelFragment.
     */
    public static LevelFragment newInstance(Level level) {
        LevelFragment fragment = new LevelFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String levelJSON = gson.toJson(level);
        args.putString(ARG_PARAM1,levelJSON);
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String levelJSON = getArguments().getString(ARG_PARAM1);
            Gson gson = new Gson();
            mLevel = gson.fromJson(levelJSON,Level.class);
        }

        mLevelCounterHandler = new Handler() {

            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                int sec  = message.getData().getInt("sec");
                int millis = message.getData().getInt("millis");
                mTimerTextView.setText(String.format(Locale.ENGLISH,"%01d",sec));
                mTimerMillisTextView.setText(String.format(Locale.ENGLISH,"%03d",millis));
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        // Inflate the layout for this fragment
        mTimerTextView = view.findViewById(R.id.text_level_timer_seconds);
        mTimerMillisTextView = view.findViewById(R.id.text_level_timer_millis);
        mLevelTitle = view.findViewById(R.id.text_level_tittle);

        mLevelTitle.setText(mLevel.getName());


        View mainView = view.findViewById(R.id.constraint_level);
        mClickAudio = MediaPlayer.create(this.getActivity(),R.raw.click);
        mClickAudio.setVolume(0.5f,0.5f);
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

    private boolean isLevelPassed(int score){
        boolean passed =  score < mLevel.getScoreToPass();

        return passed;
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
        //Log.d("LevelFragment","Score: "+score);
        return score;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void screenTouched(){

        if(mGameState == READY_STATE){
            startTimer();
            mGameState = RUNNING_STATE;
        }else if (mGameState == RUNNING_STATE){
            mClickAudio.start();
            mainTimer.cancel();
            Bundle b = mCountingTask.getLastMoment();
            mGameState = STOPPED_STATE;
            int score = calculateScore(b);
            if(isLevelPassed(score)) levelPassed();
        }else if(mGameState == STOPPED_STATE){
            mTimerTextView.setText(String.format(Locale.ENGLISH,"%01d",0));
            mTimerMillisTextView.setText(String.format(Locale.ENGLISH,"%03d",0));
            mGameState = READY_STATE;
        }
    }

    private void levelPassed() {
        LevelController.unLockNext(mLevel.getId());
        Toast.makeText(this.getActivity(),mLevel.getName()+ "Passed!!!!",Toast.LENGTH_SHORT).show();
    }

    private void startTimer(){
        mCountingTask = new CountingTask(mLevel.getSpeed(),mLevelCounterHandler);
        mainTimer = new Timer();
        mainTimer.scheduleAtFixedRate(mCountingTask,UPDATE_INTERVAL,UPDATE_INTERVAL);
    }





    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
