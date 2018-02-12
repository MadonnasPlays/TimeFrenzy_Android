package com.avoupavou.timefrenzy;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements WelcomeFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = "MAIN_ACTIVITY";

    FragmentManager mFragmentManager;
    private MediaPlayer mainAudio;

    private static boolean sIsMuted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create new fragment and transaction
        Fragment welcomeFragment = new WelcomeFragment();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.add(R.id.fragment_container, welcomeFragment);
        // Commit the transaction
        transaction.commit();

        sIsMuted = true;

        ToggleButton muteButton = findViewById(R.id.toggleButton_mute);
        muteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(mainAudio!= null && mainAudio.isPlaying()){
                        mainAudio.stop();
                        mainAudio.release();
                        mainAudio =null;
                        sIsMuted = true;
                    }
                } else {
                    if(mainAudio == null){
                        mainAudio = MediaPlayer.create(MainActivity.this, R.raw.startbig1);
                        mainAudio.setVolume(0.5f, 0.5f);
                        mainAudio.setLooping(true);
                        mainAudio.start();
                        sIsMuted = false;
                    }
                }
            }

        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mainAudio != null) {
            mainAudio.stop();
            mainAudio.release();
            mainAudio = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mainAudio == null && !sIsMuted) {
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

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        Fragment timerFragment = new TimerFragment();
        transaction.replace(R.id.fragment_container, timerFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
}
