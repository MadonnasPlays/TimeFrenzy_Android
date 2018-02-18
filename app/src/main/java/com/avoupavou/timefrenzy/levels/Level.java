package com.avoupavou.timefrenzy.levels;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Pantazis on 13-Feb-18.
 */
@Entity(tableName = "levels")
public class Level{

    @PrimaryKey
    private int id;

    private float speed;
    private boolean locked;
    private int scoreToPass;
    private int bestScore;

    public Level() {

    }

    public int getScoreToPass() {
        return scoreToPass;
    }

    public void setScoreToPass(int scoreToPass) {
        this.scoreToPass = scoreToPass;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Level(int id) {
        this.id = id;
        speed = 1;
        locked = true;
        bestScore = 1000;
    }

    public String getSpeedString(){
        return new StringBuilder().append("x").append(speed).toString();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getName() {
        return new StringBuilder().append("x").append(speed).toString();
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }


    public void updateBestScore(int bestScore) {
        if(this.bestScore  > bestScore) this.bestScore = bestScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
