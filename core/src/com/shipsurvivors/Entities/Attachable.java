package com.shipsurvivors.Entities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Icon;

/**
 * Created by SEO on 25/09/2017.
 */
public class Attachable extends Actor{
    /*This kind of object has three general states, attached in hand and grabbed. its sort of like plants vs zombies, where cards would drop
    * and you could grab one of those and play it. well its the same here, in hand means you can grab it.*/


    private boolean attached;
    private boolean inHand;
    private boolean grabbed;
    private Icon card;

    public Icon getCard() {
        return card;
    }

    public void setCard(Icon card) {
        this.card = card;
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public boolean isInHand() {
        return inHand;
    }

    public void setInHand(boolean inHand) {
        this.inHand = inHand;
    }

    public boolean isGrabbed() {
        return grabbed;
    }
}
