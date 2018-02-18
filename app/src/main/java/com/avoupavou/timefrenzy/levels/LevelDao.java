package com.avoupavou.timefrenzy.levels;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by Pantazis on 18-Feb-18.
 */

@Dao
    public interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertLevel(Level level);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertLevels(Level[] level);

    @Update
    public void updateLevel(Level level);

    @Update
    public void updateLevels(Level[] level);

    @Query("SELECT * FROM levels")
    public Level[] loadAllLevels();
}
