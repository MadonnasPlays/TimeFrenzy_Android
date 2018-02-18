package com.avoupavou.timefrenzy.levels;

import android.arch.persistence.room.Database;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;

import com.avoupavou.timefrenzy.AppDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Pantazis on 13-Feb-18.
 */

public class LevelController {

    private static final String levelsJSON = "{\"levels\":[{\"id\":1,\"locked\":false,\"speed\":0.1,\"scoreToPass\":5},{\"id\":2,\"locked\":true,\"speed\":0.2,\"scoreToPass\":5},{\"id\":3,\"locked\":true,\"speed\":0.3,\"scoreToPass\":5},{\"id\":4,\"locked\":true,\"speed\":0.4,\"scoreToPass\":5},{\"id\":5,\"locked\":true,\"speed\":0.5,\"scoreToPass\":5},{\"id\":6,\"locked\":true,\"speed\":0.6,\"scoreToPass\":5},{\"id\":7,\"locked\":true,\"speed\":0.7,\"scoreToPass\":5},{\"id\":8,\"locked\":true,\"speed\":0.8,\"scoreToPass\":5},{\"id\":9,\"locked\":true,\"speed\":0.9,\"scoreToPass\":5},{\"id\":10,\"locked\":true,\"speed\":1,\"scoreToPass\":5},{\"id\":11,\"locked\":true,\"speed\":1.2,\"scoreToPass\":5},{\"id\":12,\"locked\":true,\"speed\":1.4,\"scoreToPass\":5},{\"id\":13,\"locked\":true,\"speed\":1.6,\"scoreToPass\":5},{\"id\":14,\"locked\":true,\"speed\":1.8,\"scoreToPass\":5},{\"id\":15,\"locked\":true,\"speed\":2,\"scoreToPass\":5}]}";
    private static final String LOG_TAG = "LevelController";
    private static Level[] levels;
    private static int levelsCount;
    private static AppDatabase db;

    public static void initLevels() {

        if(levels != null) return ;

        //load levels from database
        db = AppDatabase.getAppDatabase(null);
        levels = AppDatabase.getLevels(db);
        levelsCount = levels.length;

        Log.d(LOG_TAG,String.valueOf(levelsCount));
        //if no levels found create some
        if(levels.length == 0){

            try {
                JSONObject lvlsJSON = new JSONObject(levelsJSON);
                JSONArray array = lvlsJSON.getJSONArray("levels");
                levels = new Level[array.length()];
                for(int i =0 ; i < array.length(); i++){
                    JSONObject lJSON = array.getJSONObject(i);
                    levels[i] = new Level(lJSON.getInt("id")-1);
                    levels[i].setSpeed((float)lJSON.getDouble("speed"));
                    levels[i].setLocked(lJSON.getBoolean("locked"));
                    levels[i].setBestScore(1000);
                    levels[i].setScoreToPass(lJSON.getInt("scoreToPass"));

                }
                AppDatabase.addLevels(db,levels);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public static Level[] getLevels() {
        return levels;
    }

    public static int getLevelsCount() {
        return levelsCount;
    }

    public static Level getLevel(int id){
        if(id < levelsCount && !levels[id].isLocked()) return levels[id];
        return null;
    }

    public static void unLockNext(int id){
        if(id+1 < levelsCount) {
            levels[id + 1].setLocked(false);
            saveLevelToDb(levels[id + 1]);
        }
    }

    public static void updateBestScore(int id,int bestScore){
        if (id < 0 || id > levelsCount) return;
        Level level  = levels[id];
        level.setBestScore(bestScore);
        saveLevelToDb(level);
    }

    public static void unlockAll(){
        for(int i =0 ; i < levelsCount; i++){
            levels[i].setLocked(false);
        }
    }

    public static void saveLevelsToDb(){
        AppDatabase.updateLevels(db,levels);
    }

    public static void saveLevelToDb(Level level){
        Log.d(LOG_TAG,"level updated");
        AppDatabase.updateLevel(db,level);
    }


}

