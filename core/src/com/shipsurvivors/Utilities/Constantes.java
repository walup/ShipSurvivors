package com.shipsurvivors.Utilities;

/**
 * Created by SEO on 25/09/2017.
 */
public class Constantes {

    /*Adopting google conventions nothing will be hard coded.*/
    public final static float PIXELS_IN_METER = 90;
    public final static float CARD_WIDTH = 50;
    public final static float CARD_HEIGHT = 50;
    public final static float CARD_CONTAINER_WIDTH = CARD_WIDTH +20;
    public final static float CARD_CONTAINER_HEIGHT = 5*CARD_HEIGHT +20;
    public final static float SCREEN_WIDTH = 640;
    public final static float SCREEN_HEIGHT = 360;
    public final static float MAX_SCROLLING_SPEED = 20;
    public final static float SCROLL_ACCELERATION = 10;
    public final static float SHIP_HEIGHT = 154;
    public final static float SHIP_WIDTH = 260;
    public final static float WHEEL_WIDTH = 110;
    public final static float WHEEL_HEIGHT = 110;
    public final static float MAX_ROTATION_IMPULSE = 1;
    public final static int CONTAINER_CAPACITY = 5;
    public final static float DOCK_WIDTH = 30;
    public final static float DOCK_HEIGHT = 30;
    public final static float WEAPON_WIDTH = 80;
    public final static float WEAPON_HEIGHT = 60;
    public final static float BULLET_WIDTH = 10;
    public final static float BULLET_HEIGHT = 10;
    public final static float BULLET_VELOCITY = 500;

    //Rock managing things
    public final static int ROCK_NUM_SEGMENTS = 10;
    public final static int MAX_NUM_OF_ROCKS = 5;
    public final static float LITTLE_ROCK_SIZE = 50;
    public final static float TIME_ROCK_SPAWINING = 20;



    /*Colission codes*/
    public final static short CATEGORY_SHIP = 2;
    public final static short CATEGORY_BULLET = 4;
    public final static short CATEGORY_ROCK = 8;




}
