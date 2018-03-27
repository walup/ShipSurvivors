package com.shipsurvivors.Entities;

import com.shipsurvivors.Entities.HeartContainer.HeartContainer;

/**
 * Created by SEO on 26/03/2018.
 */
public class Referee {
    /*This is the referee class, designed to keep score.*/
    private float score;
    private final float ROCK_CRUSHED_DELTA = 20;
    private HeartContainer heartContainer;
    private boolean changedScore;

    public Referee(HeartContainer heartContainer){
        this.heartContainer = heartContainer;
        score = 0;
        setChangedScore(false);
    }

    public float getScore() {
        return score;
    }

    public void crushedRock(){
        score+=ROCK_CRUSHED_DELTA;
        setChangedScore(true);
    }

    public void rockSplashedShip(){
        System.out.println("here");
        heartContainer.removePiece();
        setChangedScore(true);
    }

    public boolean isChangedScore() {
        return changedScore;
    }

    public void setChangedScore(boolean changedScore) {
        this.changedScore = changedScore;
    }
}
