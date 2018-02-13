package com.avoupavou.timefrenzy.levels;

/**
 * Created by Pantazis on 13-Feb-18.
 */

public class LevelController {
    private static Level[] levels;
    private static int levelsCount;

    public static void initLevels() {

        if(levels != null) return ;

        //TODO populate from db
        levelsCount = 10;
        levels = new Level[levelsCount];

        for(int i = 0; i < levelsCount / 2 ; i++){
            levels[i] = new Level(i);
            levels[i].setLocked(true);
            levels[i].setSpeed((i+1)/10.0f);
            levels[i].setScoreToPass(5);
        }
        for(int i = levelsCount / 2; i < levelsCount; i++){
            levels[i] = new Level(i);
            levels[i].setLocked(true);
            levels[i].setSpeed(i - levelsCount/2 +1);
            levels[i].setScoreToPass(5);
        }
        levels[0].setLocked(false);
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
        if(id+1 < levelsCount) levels[id+1].setLocked(false);
    }

    public static void unlockAll(){
        for(int i =0 ; i < levelsCount; i++){
            levels[i].setLocked(false);
        }
    }
}

