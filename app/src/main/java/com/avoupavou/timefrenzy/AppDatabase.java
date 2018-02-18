package com.avoupavou.timefrenzy;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.avoupavou.timefrenzy.levels.Level;
import com.avoupavou.timefrenzy.levels.LevelDao;

/**
 * Created by Pantazis on 18-Feb-18.
 */

@Database(entities = {Level.class}, version =1 )
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LevelDao levelDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "level-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static Level[] getLevels(final AppDatabase db) {
        Level[] levels = db.levelDao().loadAllLevels();
        return levels;
    }

    public static void addLevels(final AppDatabase db,Level[] levels) {
       db.levelDao().insertLevels(levels);
    }

    public static void updateLevels(final AppDatabase db,Level[] levels) {
        db.levelDao().updateLevels(levels);
    }
    public static void updateLevel(final AppDatabase db,Level level) {
        db.levelDao().updateLevel(level);
    }

}
