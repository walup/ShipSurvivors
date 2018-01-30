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
    private boolean reachTurnPoint,reachStopPoint;
    private float velocityX;

    public RockCarrier(Texture texture){
        this.texture  = texture;
        setSize(Constantes.CARRIER_WIDTH,Constantes.CARRIER_HEIGHT);
    }

    @Override
    public void act(float delta) {
        if(isCarrying()){
            updateCarryingPosition();
            setReachTurnPoint(getX()<destinationX);
            if(isReachTurnPoint()){
                getRockBody().setLinearVelocity(-Constantes.ROCK_VELOCITY,0);
                setVelocityX(Constantes.ROCK_CARRIER_VELOCITY);
                setCarrying(false);
                setRightDirection(true);
            }
        }
        else if (reachTurnPoint){
            updatePosition(delta);
            setReachStopPoint(getX()>Constantes.ROCK_INITIAL_POSITION_X);
            if(isReachStopPoint()){
                setReachTurnPoint(false);
                setVelocityX(0);
                setReachStopPoint(false);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isCarrying() || isReachTurnPoint()) {
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
        body.setLinearVelocity(-Constantes.ROCK_TRANSPORTATION_VELOCITY,0);
        setRockBody(body);
        setDestinationX(destinationX);
        setCarrying(true);
        setRightDirection(false);
        setVelocityX(Constantes.ROCK_CARRIER_VELOCITY);
        setPosition(startX + getRockBody().getPosition().x * Constantes.PIXELS_IN_METER + getWidth(), getRockBody().getPosition().y * Constantes.PIXELS_IN_METER);
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

    private void updateCarryingPosition(){
        setPosition(startX + getRockBody().getPosition().x * Constantes.PIXELS_IN_METER + getWidth(), getRockBody().getPosition().y * Constantes.PIXELS_IN_METER);
    }

    private void updatePosition(float delta){
        setPosition(getX()+velocityX*delta,getY());
    }

    public void setRightDirection(Boolean rightDirection) {
        this.rightDirection = rightDirection;
    }

    public boolean isReachStopPoint() {
        return reachStopPoint;
    }

    public void setReachStopPoint(boolean reachStopPoint) {
        this.reachStopPoint = reachStopPoint;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public boolean isReachTurnPoint() {
        return reachTurnPoint;
    }

    public void setReachTurnPoint(boolean reachTurnPoint) {
        this.reachTurnPoint = reachTurnPoint;
    }
}
