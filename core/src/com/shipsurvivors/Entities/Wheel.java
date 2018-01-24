package com.shipsurvivors.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
    private Dock[] docks = new Dock[4];
    private boolean rotating;
    private float angleOfSeparation;
    private float angle, previousAngle;
    private float radius;
    private Body body;
    private Fixture fixture;
    private final float LOWEST_ANGULAR_VELOCITY = (float) 1;
    private TextureRegion bandTexture;
    private Vector2 center;

    /*These are some extra variables needed to attach the dock*/
    private Dock dockToAttach;

    public Wheel(World world, TextureAtlas shipAtlas, float x, float y, float width, float height) {
        //Set Bounds
        setBounds(x, y, width, height);

        //Set the band texture
        bandTexture = shipAtlas.findRegion("band_texture");
        bandTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        /*  Calculate the angle of separation between docks. */
        angleOfSeparation = 360 / docks.length;

        /*Create body and fixture*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setAngularDamping(1);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / (2 * Constantes.PIXELS_IN_METER), height / (2 * Constantes.PIXELS_IN_METER), new Vector2(x / Constantes.PIXELS_IN_METER, y / Constantes.PIXELS_IN_METER), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 3;
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0f;
        fixture = body.createFixture(fixtureDef);

        /*Create the docks*/
        center = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        radius = (float) Math.sqrt(Math.pow(getHeight() / 2, 2) + Math.pow(getHeight() / 2, 2));
        Texture dockTexture = new Texture(Gdx.files.internal("dock.png"));
        TextureRegion textureRegion = new TextureRegion();
        textureRegion.setTexture(dockTexture);
        dockTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        for (int i = 0; i < docks.length; i++) {
            docks[i] = new Dock(textureRegion, center.x + radius * (float) Math.cos(i * (Math.toRadians(angleOfSeparation))), center.y + radius * (float) Math.sin(i * (Math.toRadians(angleOfSeparation))), Constantes.DOCK_WIDTH, Constantes.DOCK_HEIGHT, i * angleOfSeparation);
        }
    }

    @Override
    public void act(float delta) {

        /*Update the position of the band and the docks only if rotating*/
        if (rotating) {
            previousAngle = angle;
            angle = (float) Math.toDegrees(body.getAngle());

            /*Here we rotate the docks*/
            for (int i = 0; i < docks.length; i++) {
                docks[i].rotate(center.x, center.y, radius, angle - previousAngle);
            }


            /*Here we check if the thing is still rotating, this is going to be given by a
            * lower angular velocity if the velocity is less than that we will  consider it has stopped*/
            setRotating(Math.abs(body.getAngularVelocity()) > LOWEST_ANGULAR_VELOCITY);
            if (!isRotating()) {
                /*If it is no longer rotating by our criteria, then we will set the angular velocity
                * of the body to zero*/
                body.setAngularVelocity(0);
            }
        }

        /*We make the docks act*/
        for (Dock dock : docks) {
            if (!dock.isAvailable()) {
                dock.act(delta);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(bandTexture, getX(), getY(), getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1, 1, angle);
        for (int i = 0; i < docks.length; i++) {
            docks[i].draw(batch, parentAlpha);
        }
    }

    public void rotate(float impulse) {
        this.body.applyAngularImpulse(-impulse * 4, true);
        setRotating(true);
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setDockToAttach(Dock dockToAttach) {
        this.dockToAttach = dockToAttach;
    }


    public boolean touchedAvailableDock(Rectangle rect) {
        for (Dock dock : docks) {
            if (dock.getDockRect().overlaps(rect) && dock.isAvailable()) {
                setDockToAttach(dock);
                return true;
            }
        }
        return false;
    }

    public void attachDock(Attachable attachable) {
        /*You need to set the booleans of the attachable right, that is attached: true, grabbed: false, inContainer:false*/

        attachable.setAttached(true);
        attachable.setGrabbed(false);
        attachable.setInContainer(false);
        attachable.setActivated(false);

        dockToAttach.setAttachable(attachable);
        dockToAttach.setAvailable(false);
        dockToAttach.updateAttachablePosition();

    }


    public boolean touchedDockToTrigger(Vector2 coordinates) {
        for (Dock dock : docks) {
            if (dock.getDockRect().contains(coordinates.x,coordinates.y)&&!dock.isAvailable() && !dock.isTriggered()) {
                dockToAttach = dock;
                return true;
            }
        }
        return false;
    }

    public void triggerDock() {
        dockToAttach.trigger();
    }

    public Boolean bulletTouched(Fixture fixture){
        for(Dock dock:docks){
            if(dock.bulletStrike(fixture)){
                dock.detach();
                return true;
            }
        }
        return false;
    }
}
