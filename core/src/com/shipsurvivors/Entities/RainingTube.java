package com.shipsurvivors.Entities;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SEO on 25/09/2017.
 */

/*The raining tube is where the cards will drop so that you can select them it will only be able to hold five of them at a
 * time */
public class RainingTube extends Table {
    private ArrayList<Attachable> attachables = new ArrayList<Attachable>();
    private final float RAINING_FREQUENCY = 5;
    private float clock;
    private Random random;
    private Attachable dummyAttachable;


    ArrayList<Attachable> hand = new ArrayList<Attachable>();

    public RainingTube(ArrayList<Attachable> attachables){
        this.attachables = attachables;
        clock = 0;
    }

    @Override
    public void act(float delta) {
        clock+=0;

        if(clock>RAINING_FREQUENCY && hand.size()<5){
                dummyAttachable = attachables.get(random.nextInt(attachables.size()));
                dummyAttachable.setInHand(true);
                attachables.remove(dummyAttachable);
                hand.add(dummyAttachable);
        }
    }

    public void attachObject(){

    }
}
