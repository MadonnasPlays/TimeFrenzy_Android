package com.avoupavou.timefrenzy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.TimerTask;


/**
 * Created by Pantazis on 09-Feb-18.
 */

public class CountingTask extends TimerTask {

    public static final String MILLISECONDS = "millis";
    public static final String SECONDS = "sec";
    private final Handler mHandler;

    private int sec =0;
    private float millis = 0;
    private Bundle timeBundle;
    private Message message;
    private float mStep;


    public CountingTask(float step , Handler handler){

        this.mStep =step;
        this.mHandler = handler;
    }

    public Bundle getLastMoment(){
        Bundle tBundle = new Bundle();
        tBundle.putInt(SECONDS,sec);
        tBundle.putInt(MILLISECONDS ,(int)millis);
        return tBundle;
    }

    @Override
    public void run() {
        millis += mStep;
        if(millis >= 999){
            sec ++;
            millis =0;
        }
        if(sec > 60) sec =0;
        timeBundle = new Bundle();
        timeBundle.putInt(SECONDS,sec);
        timeBundle.putInt(MILLISECONDS,(int)millis);
        message = new Message();
        message.setData(timeBundle);
        this.mHandler.sendMessage(message);
    }
}
