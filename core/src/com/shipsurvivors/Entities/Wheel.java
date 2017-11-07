package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

import java.util.ArrayList;

/**
 * Created by SEO on 25/09/2017.
 */
public class Wheel extends Actor {
    private Dock[] docks = new Dock[3];
    private boolean rotating;
    private float angleOfSeparation;
    private float angle;
    private Body body;
    private Fixture fixture;
    private final float LOWEST_ANGULAR_VELOCITY = (float)0.01;
    TextureRegion bandTexture;


    public Wheel(World world, TextureAtlas shipAtlas, float x, float y, float width,float height){
        //Set Bounds
        setBounds(x,y,width,height);

        //Set the band texture
        bandTexture = shipAtlas.findRegion("band_texture");
        bandTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        /*  Calculate the angle of separation between docks. */
        angleOfSeparation= 360/docks.length;

        /*Create body and fixture*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body =world.createBody(bodyDef);
        body.setAngularDamping(1);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/(2*Constantes.PIXELS_IN_METER),height/(2*Constantes.PIXELS_IN_METER),new Vector2(x/Constantes.PIXELS_IN_METER,y/Constantes.PIXELS_IN_METER),0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 3;
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0f;
        fixture = body.createFixture(fixtureDef);

        /*Create the docks*/
    }

    @Override
    public void act(float delta) {

        /*Update the position of the band and the docks only if rotating*/
        if(rotating){
            angle = (float) Math.toDegrees(body.getAngle());

            /*Here we check if the thing is still rotating, this is going to be given by a
            * lower angular velocity if the velocity is less than that we will  consider it has stopped*/
            setRotating(Math.abs(body.getAngularVelocity())>LOWEST_ANGULAR_VELOCITY);
            if(!isRotating()){
                /*If it is no longer rotating by our criteria, then we will set the angular velocity
                * of the body to zero*/
                body.setAngularVelocity(0);
            }
        }
        System.out.println(body.getAngularVelocity());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(bandTexture,getX(),getY(),(getWidth())/2,getHeight()/2,getWidth(),getHeight(),1,1,angle);
    }

    public void rotate(float impulse){
        this.body.applyAngularImpulse(-impulse*4,true);
        setRotating(true);
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }
    public boolean isRotating(){
        return rotating;
    }

}
