package com.shipsurvivors.Utilities.SandBox;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.List;

/**
 * Created by SEO on 26/12/2017.
 */
/*This will encapsulate a PolygonShape and its Body*/


public class PolygonPack {
    private List<PolygonRegion> polygonRegions;
    private Body body;
    private boolean destroyTagged;


    public void setPolygonRegions(List<PolygonRegion> polygonRegions) {
        this.polygonRegions = polygonRegions;
    }

    public List<PolygonRegion> getPolygonRegions() {
        return polygonRegions;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public void setDestroyTagged(boolean destroyTagged) {
        this.destroyTagged = destroyTagged;
    }

    public boolean isDestroyTagged() {
        return destroyTagged;
    }



    public boolean isDestroyed() {
        return body.getFixtureList().size==0;
    }
}
