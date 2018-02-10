package com.avoupavou.timefrenzy;

import android.os.Bundle;
import android.os.Message;

import java.util.TimerTask;

import static com.avoupavou.timefrenzy.TimerFragment.mHandler;

/**
 * Created by Pantazis on 09-Feb-18.
 */

public class CountingTask extends TimerTask {

    public static final String MILLISECONDS = "millis";
    public static final String SECONDS = "sec";

    private int sec =0;
    private float millis = 0;
    private Bundle timeBundle;
    private Message message;
    private int updateInterval;


    public CountingTask(int interval){
        this.updateInterval =interval;
    }

    public Bundle getLastMoment(){
        Bundle tBundle = new Bundle();
        tBundle.putInt(SECONDS,sec);
        tBundle.putInt(MILLISECONDS ,(int)millis);
        return tBundle;
    }

    @Override
    public void run() {
        millis += updateInterval;
        if(millis >= 999){
            sec ++;
            millis =0;
        }
        if(sec > 60) sec =0;
        timeBundle = new Bundle();
        timeBundle.putInt("sec",sec);
        timeBundle.putInt("millis",(int)millis);
        message = new Message();
        message.setData(timeBundle);
        mHandler.sendMessage(message);
    }
}
