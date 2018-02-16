package com.avoupavou.timefrenzy.levels;

import java.io.Serializable;

/**
 * Created by Pantazis on 13-Feb-18.
 */

public class Level{
    private int id;
    private float speed;
    private String name;
    private boolean locked;
    private int scoreToPass;

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
        name = new StringBuilder().append("x").append(speed).toString();
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
