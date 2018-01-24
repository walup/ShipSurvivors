package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 13/01/2018.
 */



public class RockCarrier  extends Actor {
    private Texture texture;
    private Boolean carrying = false;
    private Boolean rightDirection = false;
    private Body rockBody;
    private float destinationX;
    private float startX = Constantes.ROCK_INITIAL_POSITION_X;

    public RockCarrier(Texture texture){
        this.texture  = texture;
        setSize(Constantes.CARRIER_WIDTH,Constantes.CARRIER_HEIGHT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isCarrying()){
            updatePosition();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(carrying) {
            batch.draw(texture, getX(), getY(), getWidth(), getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), rightDirection, false);
        }
    }

    public void setRockBody(Body rockBody) {
        this.rockBody = rockBody;
    }

    public Body getRockBody() {
        return rockBody;
    }

    public void assignOrder(Body body,float destinationX){
        setRockBody(body);
        setDestinationX(destinationX);
        setCarrying(true);
        setRightDirection(false);
    }

    public float getDestinationX() {
        return destinationX;
    }

    public void setDestinationX(float destinationX) {
        this.destinationX = destinationX;
    }

    public void setCarrying(Boolean carrying) {
        this.carrying = carrying;
    }

    public boolean isCarrying(){
        return carrying;
    }

    private void updatePosition(){
        setPosition(startX+getRockBody().getPosition().x* Constantes.PIXELS_IN_METER + getWidth(),getRockBody().getPosition().y*Constantes.PIXELS_IN_METER);
    }

    public void setRightDirection(Boolean rightDirection) {
        this.rightDirection = rightDirection;
    }
}
