package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.UI.ShipControls;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.UserData;

/**
 * Created by SEO on 25/09/2017.
 */

/*This is the ship, it will have a band on its perimeter, where you can attach weapons*/
public class Ship extends Actor {
    private Body body;
    private Fixture fixture;
    private TextureRegion shipTexture;
    private Wheel wheel;
    private Boolean restorePositionOrder;


    //Ok these are the variables needed to implement movement
    private Rectangle moveUpRect;
    private Rectangle moveDownRect;
    private float velocityY;





    public Ship(World world, TextureAtlas shipTextures, float x, float y,float width, float height ){
        /*Extract the ship texture*/
        shipTexture = shipTextures.findRegion("ship_texture");
        shipTexture.getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);

        /*Set the bounds of the thing*/
        setBounds(x,y,width,height);

        /*Set the band, it has to have a radius equal to the width of the ship*/
        wheel = new Wheel(world,shipTextures,getX()+(Constantes.SHIP_WIDTH -Constantes.WHEEL_WIDTH)/2,getY()+(Constantes.SHIP_HEIGHT -Constantes.WHEEL_HEIGHT)/2,Constantes.WHEEL_WIDTH,Constantes.WHEEL_HEIGHT);

        /*Create the body*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        UserData userData = new UserData(UserData.SHIP);
        body = world.createBody(bodyDef);
        body.setUserData(userData);

        /*Create the fixture*/

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/(2*Constantes.PIXELS_IN_METER),height/(2*Constantes.PIXELS_IN_METER));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 5;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constantes.CATEGORY_SHIP;
        fixtureDef.filter.maskBits = Constantes.CATEGORY_ROCK;
        fixture = body.createFixture(fixtureDef);

        /*Put the ship at x,y in the world*/
        body.setTransform(x/Constantes.PIXELS_IN_METER+(getWidth())/(2*Constantes.PIXELS_IN_METER), y/Constantes.PIXELS_IN_METER +(getHeight()/(2*Constantes.PIXELS_IN_METER)),0);
        setRestorePositionOrder(false);

        /*Set the moveUp, moveDown rectangles*/
        moveUpRect = new Rectangle(wheel.getCenter().x-getWidth()/2,wheel.getCenter().y+wheel.getRadius()+Constantes.DOCK_HEIGHT,Constantes.SHIP_WIDTH,Constantes.SHIP_HEIGHT);
        moveDownRect = new Rectangle(wheel.getCenter().x-getWidth()/2,wheel.getCenter().y -wheel.getRadius()-Constantes.DOCK_HEIGHT -Constantes.SHIP_HEIGHT,Constantes.SHIP_WIDTH,Constantes.SHIP_HEIGHT);
    }

    @Override
    public void act(float delta) {
        wheel.act(delta);
        if(getRestorePositionOrder()){
            restoreBodyPosition();
        }

        updateMovement(delta);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        wheel.draw(batch, parentAlpha);
        batch.draw(shipTexture, getX(), getY(), getWidth(), getHeight());

    }

    public void restoreBodyPosition(){
        body.setTransform(getX()/Constantes.PIXELS_IN_METER +(getWidth())/(2*Constantes.PIXELS_IN_METER),getY()/Constantes.PIXELS_IN_METER+ (getHeight()/(2*Constantes.PIXELS_IN_METER)),0);
    }



    public void dispose(){
    }

    public Wheel getWheel() {
        return wheel;
    }

    public Boolean getRestorePositionOrder() {
        return restorePositionOrder;
    }

    public void setRestorePositionOrder(Boolean restorePositionOrder) {
        this.restorePositionOrder = restorePositionOrder;
    }

    public void updateMovement(float delta){
        if(ShipControls.getTouchIntent()){
            //Upward movement
            if(moveUpRect.contains(ShipControls.getMouseStagePosition().x,ShipControls.getMouseStagePosition().y)){
                velocityY = Constantes.SHIP_VELOCITY;
                setPosition(getX(),getY()+velocityY*delta);
                moveUpRect.setPosition(moveUpRect.getX(),moveUpRect.getY()+velocityY*delta);
                moveDownRect.setPosition(moveDownRect.getX(),moveDownRect.getY()+velocityY*delta);
                wheel.moveWheel(velocityY,delta);
            }

            //Downward movement
            else if(moveDownRect.contains(ShipControls.getMouseStagePosition().x,ShipControls.getMouseStagePosition().y)){
                velocityY = -Constantes.SHIP_VELOCITY;
                setPosition(getX(),getY()+velocityY*delta);
                moveDownRect.setPosition(moveDownRect.getX(),moveDownRect.getY()+velocityY*delta);
                moveUpRect.setPosition(moveUpRect.getX(),moveUpRect.getY()+velocityY*delta);
                wheel.moveWheel(velocityY,delta);
                }
            }
        }
    }
