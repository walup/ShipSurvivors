package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Icon;

/**
 * Created by SEO on 25/09/2017.
 */
public class Attachable extends Actor{
    /*This kind of object has three general states, attached in hand and grabbed. This are mutually exclusive booleans.
    * and there has to be some sort of hierarchy for overriding the states of this, for instance if it is attached, you
    * definitely can not put it in container.*/

    private boolean attached;
    private boolean inContainer;
    private boolean grabbed;
    private boolean activated;
    private Icon card;
    /*This pointer, is one which we will be modifying while the plyer is panning*/
    private Vector2 pointer = new Vector2();

    /*We'll handle colissions between cards and docks using rectangles*/
    private Rectangle cardRectangle = new Rectangle(0,0,0,0);



    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*If it is in the container or grabbed, we will draw the card. Else if it is attached we will call a method called
        * drawAttachable*/
        if(inContainer || grabbed){
            batch.draw(card.getIconTexture(),getX(),getY(),card.getWidth(),card.getHeight());

        }
        else if(attached){
            drawAttachable(batch,parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        if(isGrabbed()){
            //Then follow the mouse of the player.
            setPosition(pointer.x,pointer.y);
            /*This rect thing requires better implementation, like maybe putting the card and the rect in the same kind of object
            * Card. for now, we'll just combine the two modes*/
            updateRect();
        }
        if(activated){
            activeAct();
        }

    }

    public void setPointer(Vector2 pointer){
        this.pointer = pointer;
    }

    public void activeAct(){

    }

    public Vector2 getPointer() {
        return pointer;
    }

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

    public boolean isInContainer() {
        return inContainer;
    }

    public void setGrabbed(boolean grabbed){
        this.grabbed = grabbed;
    }

    public void setInContainer(boolean inHand) {
        this.inContainer = inHand;
    }

    public boolean isGrabbed() {
        return grabbed;
    }

    public boolean isFree(){
        return !isAttached() && !isGrabbed() && !isInContainer();
    }

    public void drawAttachable(Batch batch, float parentAlpha) {
        /*This is a method that you should fill to draw your particular attachable. */

    }

    public void updateRect(){
        cardRectangle.set(getX(),getY(),card.getWidth(),card.getHeight());
    }

    public void setCardRectangle(Rectangle cardRectangle) {
        this.cardRectangle = cardRectangle;
    }

    public Rectangle getCardRectangle() {
        return cardRectangle;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

}
