package com.avoupavou.timefrenzy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.avoupavou.timefrenzy.levels.Level;
import com.avoupavou.timefrenzy.levels.LevelFragment;
import com.avoupavou.timefrenzy.levels.LevelSelectFragment;

public class MainActivity extends AppCompatActivity implements WelcomeFragment.OnWelcomeFragmentInteractionListener, LevelSelectFragment.OnLevelSelectFragmentInteractionListener {


    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final String PREFS_NAME = "basicPrefs";

    FragmentManager mFragmentManager;
    private MediaPlayer mainAudio;

    private static boolean mSilentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create new fragment and transaction
        Fragment welcomeFragment = new WelcomeFragment();
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        //transaction.replace(R.id.fragment_container, welcomeFragment);
        transaction.add(R.id.fragment_container,welcomeFragment);
        // Commit the transaction
        transaction.commit();


        //Share preferences init
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        mSilentMode = sharedPreferences.getBoolean("silentMode",false);
        setMuteMusic(mSilentMode);

        ToggleButton muteButton = findViewById(R.id.toggleButton_mute);

        muteButton.setChecked(mSilentMode);

        muteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //muted
                    setMuteMusic(true);
                    saveMuteMusic(true);

                } else {
                    //unmuted
                    saveMuteMusic(false);
                    setMuteMusic(false);
                }
            }

        });


    }


    private void setMuteMusic(boolean mute){
        if(mute){
            if(mainAudio!= null && mainAudio.isPlaying()){
                mainAudio.stop();
                mainAudio.release();
                mainAudio =null;
                mSilentMode = true;
            }
        }else{
            if(mainAudio == null){
                mainAudio = MediaPlayer.create(MainActivity.this, R.raw.startbig1);
                mainAudio.setVolume(0.5f, 0.5f);
                mainAudio.setLooping(true);
                mainAudio.start();
                mSilentMode = false;
            }
        }
    }

    private void saveMuteMusic(boolean mute){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        sharedPreferences.edit()
                .putBoolean("silentMode",mute).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveMuteMusic(mSilentMode);
        if(mainAudio != null) {
            mainAudio.stop();
            mainAudio.release();
            mainAudio = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mainAudio == null && !mSilentMode) {
            mainAudio = MediaPlayer.create(this, R.raw.startbig1);
            mainAudio.setVolume(0.5f, 0.5f);
            mainAudio.setLooping(true);
            mainAudio.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPlayButtonClicked() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        //Replace whatever is in the fragment_container view with this fragment,
        //and add the transaction to the back stack
        Fragment timerFragment = new TimerFragment();
        transaction.replace(R.id.fragment_container, timerFragment);
        transaction.addToBackStack(null);
        //Commit the transaction
        transaction.commit();
    }

    @Override
    public void onLevelsButtonClicked() {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //Replace whatever is in the fragment_container view with this fragment,
        //and add the transaction to the back stack
        Fragment levelSelectFragment = new LevelSelectFragment();
        transaction.replace(R.id.fragment_container, levelSelectFragment);
        transaction.addToBackStack(null);
        //Commit the transaction
        transaction.commit();
    }

    @Override
    public void onSettingsButtonClicked() {

//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack
//        Fragment settingsFragment = new SettingsFragment();
//        transaction.replace(R.id.fragment_container, settingsFragment);
//        transaction.addToBackStack(null);
//        // Commit the transaction
//        transaction.commit();
    }

    @Override
    public void onLevelSelect(Level level) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //Replace whatever is in the fragment_container view with this fragment,
        //and add the transaction to the back stack
        Fragment levelFragment = LevelFragment.newInstance(level);
        transaction.replace(R.id.fragment_container, levelFragment);
        transaction.addToBackStack(null);
        //Commit the transaction
        transaction.commit();
    }
}
