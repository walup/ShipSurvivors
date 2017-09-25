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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 25/09/2017.
 */

/*This is the ship, it basically is a round thing, which will have a band on its perimeter, where you can attach weapons*/
public class Ship extends Actor {
    private Body body;
    private Fixture fixture;
    private TextureRegion shipTexture;



    public Ship(World world, TextureAtlas shipTextures, float x, float y, float radius){
        this.shipTexture = shipTextures.findRegion("ship");

        /*Set the bounds of the thing*/
        setBounds(x,y,radius,radius);


        /*Create the body*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        world.createBody(bodyDef);

        /*Create the fixture, it is circular*/

        CircleShape shape = new CircleShape();
        shape.setRadius(radius/Constantes.PIXELS_IN_METER);
        shape.setPosition(new Vector2(x/Constantes.PIXELS_IN_METER,y/Constantes.PIXELS_IN_METER));
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
