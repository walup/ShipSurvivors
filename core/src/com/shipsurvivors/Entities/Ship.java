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

/**
 * Created by SEO on 25/09/2017.
 */

/*This is the ship, it will have a band on its perimeter, where you can attach weapons*/
public class Ship extends Actor {
    private Body body;
    private Fixture fixture;
    private TextureRegion shipTexture;
    private Band band;



    public Ship(World world, TextureAtlas shipTextures, float x, float y,float width, float height ){
        /*Extract the ship texture*/
        shipTexture = shipTextures.findRegion("ship_texture");

        /*Set the bounds of the thing*/
        setBounds(x,y,width,height);

        /*Set the band, it has to have a radius equal to the width of the ship*/
        band = new Band(world,shipTextures,getX(),getY(),getWidth());

        /*Create the body*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        world.createBody(bodyDef);

        /*Create the fixture*/

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/(2*Constantes.PIXELS_IN_METER),height/(2*Constantes.PIXELS_IN_METER));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 5;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(shipTexture,getX(),getY(),getWidth(),getHeight());
    }


    public void dispose(){
    }

}
