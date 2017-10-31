package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 25/09/2017.
 */

/*This is the ship, it will have a band on its perimeter, where you can attach weapons*/
public class Ship extends Actor {
    private Body body;
    private Fixture fixture;
    private TextureRegion shipTexture;
    private Wheel wheel;



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
        body = world.createBody(bodyDef);

        /*Create the fixture*/

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/(2*Constantes.PIXELS_IN_METER),height/(2*Constantes.PIXELS_IN_METER));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 5;
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
    }

    @Override
    public void act(float delta) {
        wheel.act(delta);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        wheel.draw(batch,parentAlpha);
        batch.draw(shipTexture,getX(),getY(),getWidth(),getHeight());
    }



    public void dispose(){
    }

    public Wheel getWheel() {
        return wheel;
    }
}
