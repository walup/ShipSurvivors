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
    private Boolean movingUp, movingDown;


    //Ok these are the variables needed to implement movement
    private Rectangle moveUpRect;
    private Rectangle moveDownRect;
    private float velocityY;

    //private ShapeRenderer shapeRenderer;





    public Ship(World world, TextureAtlas shipTextures, float x, float y,float width, float height ){
        /*Shape renderer for debugging*/
        //shapeRenderer = new ShapeRenderer();

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
        moveUpRect = new Rectangle(wheel.getCenter().x-getWidth()/2,wheel.getCenter().y+wheel.getRadius()+Constantes.DOCK_HEIGHT,Constantes.TOUCH_MOVEMENT_RECT_WIDTH,Constantes.TOUCH_MOVEMENT_RECT_HEIGHT);
        moveDownRect = new Rectangle(wheel.getCenter().x-getWidth()/2,wheel.getCenter().y -wheel.getRadius()-Constantes.DOCK_HEIGHT -Constantes.SHIP_HEIGHT,Constantes.TOUCH_MOVEMENT_RECT_WIDTH,Constantes.TOUCH_MOVEMENT_RECT_HEIGHT);

        movingUp = false;
        movingDown = false;
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

        //shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.rect(moveUpRect.getX(),moveUpRect.getY(),moveUpRect.getWidth(),moveUpRect.getHeight());
        //shapeRenderer.end();

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

    public void setMovingUp(Boolean movingUp) {
        this.movingUp = movingUp;
    }
    public boolean isMovingUp(){
        return movingUp;
    }

    public void setMovingDown(Boolean movingDown) {
        this.movingDown = movingDown;
    }
    public boolean isMovingDown(){
        return movingDown;
    }

    public boolean isMoving(){
        return isMovingDown() || isMovingUp();
    }

    public void stopMovement(){
        setMovingDown(false);
        setMovingUp(false);
    }

    public void updateMovement(float delta){
        if(!isMoving() &&ShipControls.getTouchIntent()){
                //Activate Upward movement
                if(moveUpRect.contains(ShipControls.getMouseStagePosition().x,ShipControls.getMouseStagePosition().y)){
                    setMovingUp(true);
                    ShipControls.setTouchIntent(false);
                }
                //Downward movement
                else if(moveDownRect.contains(ShipControls.getMouseStagePosition().x,ShipControls.getMouseStagePosition().y)){
                   setMovingDown(true);
                    ShipControls.setTouchIntent(false);
                }
            }

        else if(isMovingUp() && !isOutOfBoundsTop()){
            velocityY = Constantes.SHIP_VELOCITY;
            setPosition(getX(),getY()+velocityY*delta);
            body.setTransform(body.getPosition().x,body.getPosition().y+(velocityY*delta)/Constantes.PIXELS_IN_METER,0);
            moveUpRect.setPosition(moveUpRect.getX(),moveUpRect.getY()+velocityY*delta);
            moveDownRect.setPosition(moveDownRect.getX(),moveDownRect.getY()+velocityY*delta);
            wheel.moveWheel(velocityY,delta);
        }
        else if(isMovingDown() && !isOutOfBoundsBottom()){
            velocityY = -Constantes.SHIP_VELOCITY;
            setPosition(getX(),getY()+velocityY*delta);
            body.setTransform(body.getPosition().x,body.getPosition().y+(velocityY*delta)/Constantes.PIXELS_IN_METER,0);
            moveDownRect.setPosition(moveDownRect.getX(),moveDownRect.getY()+velocityY*delta);
            moveUpRect.setPosition(moveUpRect.getX(),moveUpRect.getY()+velocityY*delta);
            wheel.moveWheel(velocityY,delta);
        }
        }

        public boolean isOutOfBoundsBottom(){
        return getY()<0;
        }

        public boolean isOutOfBoundsTop(){
        return getY()+getHeight()>Constantes.WORLD_HEIGHT;
        }

        public boolean isOutOfBounds(){
        return isOutOfBoundsBottom() && isOutOfBoundsTop();
        }
    }
