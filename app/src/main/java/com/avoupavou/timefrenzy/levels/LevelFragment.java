package com.avoupavou.timefrenzy.levels;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avoupavou.timefrenzy.CircleProgressBar;
import com.avoupavou.timefrenzy.CountingTask;
import com.avoupavou.timefrenzy.R;

import java.util.Locale;
import java.util.Timer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LevelFragment.OnLevelFragmentInteractionListener} interface
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


    private OnLevelFragmentInteractionListener mListener;
    private TextView mTimerTextView;
    private TextView mTimerMillisTextView;
    private MediaPlayer mClickAudio;
    private CircleProgressBar mProgressBar;

    private int mGameState;
    private CountingTask mCountingTask;
    private Timer mainTimer;
    private Level mLevel;
    private Handler mLevelCounterHandler;
    private TextView mLevelTitle;
    private final String LOG_TAG = "LevelFragment";
    private ImageButton mBackButton;

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
        args.putInt(ARG_PARAM1,level.getId());
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int levelId = getArguments().getInt(ARG_PARAM1);
            mLevel = LevelController.getLevel(levelId);
        }

        mLevelCounterHandler = new Handler() {

            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if(mGameState == STOPPED_STATE) return;
                int sec  = message.getData().getInt("sec");
                int millis = message.getData().getInt("millis");
                mProgressBar.setProgress(millis);
                mTimerTextView.setText(String.format(Locale.ENGLISH,"%01d",sec));
                mTimerMillisTextView.setText(String.format(Locale.ENGLISH,"%03d",millis));
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        // Inflate the layout for this fragment
        mTimerTextView = view.findViewById(R.id.text_level_timer_seconds);
        mTimerMillisTextView = view.findViewById(R.id.text_level_timer_millis);
        mLevelTitle = view.findViewById(R.id.text_level_tittle);
        mBackButton = view.findViewById(R.id.button_level_back);

        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.backButtonPressed();
                }
                return true;
            }
        });
        mProgressBar = view.findViewById(R.id.progressBar_level);


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

    private int calculateScore(int sec, int ms){
        int score = 0;
        if(sec > 0 && ms < 500){
            score = ms;
        }else{
            score = 1000-ms;
        }

        return score;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLevelFragmentInteractionListener) {
            mListener = (OnLevelFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mainTimer != null) mainTimer.cancel();
        mainTimer = null;
        mListener = null;
    }


    public void screenTouched(){

        if(mGameState == READY_STATE){
            startTimer();
            mGameState = RUNNING_STATE;
        }else if (mGameState == RUNNING_STATE){
            mGameState = STOPPED_STATE;
            mClickAudio.start();
            int ms = Integer.parseInt((String) mTimerMillisTextView.getText());
            int sec = Integer.parseInt((String) mTimerTextView.getText());
            mainTimer.cancel();

            int score = calculateScore(sec,ms);
            LevelController.updateBestScore(mLevel.getId(),score);
            if(isLevelPassed(score)) levelPassed(score);

        }else if(mGameState == STOPPED_STATE){
            mTimerTextView.setText(String.format(Locale.ENGLISH,"%01d",0));
            mTimerMillisTextView.setText(String.format(Locale.ENGLISH,"%03d",0));
            mGameState = READY_STATE;
        }
    }

    private void levelPassed(int score) {
        LevelController.unLockNext(mLevel.getId());

        LevelDialog levelDialog= new LevelDialog();
        levelDialog.setLevel(mLevel.getName());
        levelDialog.setScore(score);
        levelDialog.show(this.getActivity().getFragmentManager(), "popup");
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
    public interface OnLevelFragmentInteractionListener {
        public void backButtonPressed();
    }
}
