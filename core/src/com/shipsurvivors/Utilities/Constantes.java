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
    public final static float FOOTBALL_CAT_WIDTH = 120;
    public final static float FOOTBALL_CAT_HEIGHT = 100;
    public final static float FOOTBALL_HEIGHT  = 40;
    public final static float FOOTBALL_WIDHT = 60;
    public final static float FOOTBALL_CAT_VELOCITY = 100;
    public final static float FOOTBALL_VELOCITY = 150;


    //Rock managing things
    public final static int ROCK_NUM_SEGMENTS = 10;
    public final static int MAX_NUM_OF_ROCKS = 5;
    public final static float LITTLE_ROCK_SIZE = 50;
    public final static float TIME_ROCK_SPAWINING = 20;



    /*Colission codes*/
    public final static short CATEGORY_SHIP = 2;
    public final static short CATEGORY_BULLET = 4;
    public final static short CATEGORY_ROCK = 8;

    /*Constants for the spacing in the menu */
    public final static float STANDARD_BUTTON_PADDING = 20;
    public final static float SETTINGS_ICON_WIDTH = 50;
    public final static float SETTINGS_ICON_HEIGHT = 50;
    public final static float STANDARD_ICON_PADDING = 10;

    public final static float ROCK_VELOCITY = 0.2f;
    public final static float ROCK_INITIAL_POSITION_X = 800;
    public final static float CARRIER_WIDTH = 70;
    public final static float CARRIER_HEIGHT = 100;
    public final static float ROCK_CARRIER_VELOCITY = (float) 200;
    public final static float ROCK_FINAL_POSITION_X = SCREEN_WIDTH-LITTLE_ROCK_SIZE-CARRIER_WIDTH;
    public final static float ROCK_TRANSPORTATION_VELOCITY = 0.5f;


    public final static float FLAME_VELOCITY = 200;
    public final static float FLAME_RADIUS = 10;


    public final static int HEART_PIECES = 4;
    public final static int NUMBER_OF_HEARTS = 3;
    public final static float HEART_WIDTH = 60;
    public final static float HEART_HEIGHT = 40;
    public final static float HEART_CONTAINER_X = 0;
    public final static float HEART_CONTAINER_Y = SCREEN_HEIGHT-HEART_HEIGHT;










}
