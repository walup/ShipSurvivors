package com.shipsurvivors.Utilities.SandBox;

/**
 * Created by SEO on 23/12/2017.
 */
public class UserData {
    public static final int BULLET = 0;
    public static final int ROCK = 1;
    public static final int BALL = 2;
    public static final int SHIP = 3;
    public int type;
    public boolean mustDestroy = false;
    public boolean destroyed = false;
    public float splashRadius;

    public UserData(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setSplashRadius(float splashRadius) {
        this.splashRadius = splashRadius;
    }

    public float getSplashRadius() {
        return splashRadius;
    }
}