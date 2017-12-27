package com.shipsurvivors.Utilities.SandBox;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEO on 23/12/2017.
 */

public class RockFixture {
    private Body body;
    private List<Fixture> fixtures;
    private List<float[]> vertices;

    public RockFixture(List<float[]> vertices){
        this.vertices = vertices;
    }

    public RockFixture(float radius, float centerX, float centerY){
        List<float[]> verts = new ArrayList<float[]>();
        float[] circleVertices = CollisionGeometry.approxCircle(centerX,centerY,radius, Constantes.ROCK_NUM_SEGMENTS);
        verts.add(circleVertices);
        this.setVertices(verts);
    }

    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
    }


    public List<float[]> getVertices() {
        return vertices;
    }

    public void setVertices(List<float[]> vertices) {
        this.vertices = vertices;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

}
