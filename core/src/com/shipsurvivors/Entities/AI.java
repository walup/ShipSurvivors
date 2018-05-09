package com.shipsurvivors.Entities;

import java.util.Random;

public class AI {
    /*This is the AI (which is not really smart enough to be called AI but whatever...)
    * this will control the flow of the game. will be able to change the rock size and maybe
    * make things a little harder as the time goes by i still haven't figured out
    * all the details of what how this class will work exactly. */

    private int rockSize;
    private float gameTime;
    private int playerPoints;
    private float probRockSize1 = 0.7f;
    private float probRockSize2 = 0.2f;
    private float probRockSize3 = 0.09f;
    private float probMassiveKillerRock =0.01f;
    /*I don't know anything about AI , but let's try to make some kind of probabiliistic thing. */
    public AI(){

    }


}
