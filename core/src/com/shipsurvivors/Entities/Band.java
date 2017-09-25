package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

import java.util.ArrayList;

/**
 * Created by SEO on 25/09/2017.
 */
public class Band extends Actor {
    private float bandThickness = 5;
    private Dock[] docks = new Dock[3];
    private float angleOfSeparation;
    private float angle;
    private Body body;
    private Fixture fixture;

    public Band(World world, TextureAtlas shipAtlas,float x, float y,  float radius){
        /*  Calculate the angle of separation.*/
        angleOfSeparation= 360/docks.length;

        /*Create body and fixture*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body =world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(x/ Constantes.PIXELS_IN_METER,y/Constantes.PIXELS_IN_METER));
        shape.setRadius(radius+bandThickness);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 3;
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

        /*Create the docks*/


    }
}
