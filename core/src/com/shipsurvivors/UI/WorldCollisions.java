package com.shipsurvivors.UI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.shipsurvivors.Entities.Referee;
import com.shipsurvivors.Entities.RockSpawner;
import com.shipsurvivors.Entities.Ship;
import com.shipsurvivors.Screens.GameScreen;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.CollisionGeometry;
import com.shipsurvivors.Utilities.SandBox.PolygonBox2DShape;
import com.shipsurvivors.Utilities.SandBox.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEO on 23/12/2017.
 */
public class WorldCollisions implements ContactListener {
    public float circRadius = 1f;
    public int segments = (int) 20;
    private RockSpawner rockSpawner;
    private boolean clipped;
    private Ship ship;
    private final float CORRECTION_X = Constantes.SCREEN_WIDTH/Constantes.PIXELS_IN_METER;
    private Referee referee;
    private GameScreen.Dj dj;

    public WorldCollisions(RockSpawner rockSpawner){
        this.rockSpawner =rockSpawner;
    }

    public WorldCollisions(RockSpawner rockSpawner, GameScreen.Dj dj, Ship ship, Referee referee){
        this.rockSpawner = rockSpawner;
        this.ship = ship;
        this.referee = referee;
        this.dj = dj;
    }

    @Override
    public void beginContact(Contact contact) {
        ship.getWheel().bulletTouched(contact.getFixtureA());
        ship.getWheel().bulletTouched(contact.getFixtureB());
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

        /**
         * The particular implementation of postSolve for colissions. As always get the bodies, and now what you'll
         * check is the UserData of the bodies, if one of them is a BULLET, and the other is of type ROCK, then you're
         * going to clip the rock (see the method clippingRock)
         * **/

        Body a= contact.getFixtureA().getBody();
        Body b= contact.getFixtureB().getBody();
        UserData dataA = (UserData)a.getUserData();
        UserData dataB = (UserData)b.getUserData();

        if(dataA instanceof UserData && dataA.getType() == UserData.ROCK && dataB instanceof UserData && dataB.getType() == UserData.BULLET){

            clippingRock(a, b, dataB);
            referee.crushedRock();
            dj.playBoulderBreakSound();
        }else if(dataB instanceof UserData && dataB.getType() == UserData.ROCK && dataA instanceof UserData && dataA.getType() == UserData.BULLET){
            clippingRock(b, a,dataA);
            referee.crushedRock();
            dj.playBoulderBreakSound();
        }
        clipped = false;


        /*Ok so here we will handle the colissions between the rocks and the ship */
        if(dataA instanceof UserData && dataA.getType() == UserData.ROCK && dataB instanceof UserData && dataB.getType() == UserData.SHIP){
            referee.rockSplashedShip();
            ship.setRestorePositionOrder(true);


        }else if(dataB instanceof UserData && dataB.getType() == UserData.ROCK && dataA instanceof UserData && dataA.getType() == UserData.SHIP){
            referee.rockSplashedShip();
            ship.setRestorePositionOrder(true);
        }
    }

    /**
     * clippingGround basically receives two bodies a: which is going to be clipped (say the ground) and b: whieh is
     * a clipper (say a bomb or a bullet). And it basically gets the difference between one and the other, and gives it too
     * the method switch ground (see what it does on Game Screen), something that is quite important is that some adjustment
     * needs to be make to the position of the bullet so that it clips the rock properly. that's the reason we always take out
     * CORRECTION_X when clipping the body
     * **/
    private void clippingRock(Body a, Body b, UserData dataB) {
        Body body = a;
        if (!((UserData)body.getUserData()).mustDestroy) {
            List<PolygonBox2DShape> totalRS = new ArrayList<PolygonBox2DShape>();

            float[] circleVertices = CollisionGeometry.approxCircle(b.getPosition().x-CORRECTION_X, b.getPosition().y, dataB.getSplashRadius(), Constantes.ROCK_NUM_SEGMENTS);
            ChainShape shape = new ChainShape();
            shape.createLoop(circleVertices);

            PolygonBox2DShape circlePoly = new PolygonBox2DShape(shape);

            Array<Fixture> fixtureList = body.getFixtureList();
            int fixCount = fixtureList.size;

            for (int i = 0; i < fixCount; i++) {

                PolygonBox2DShape polyClip = null;
                if (fixtureList.get(i).getShape() instanceof PolygonShape) {
                    polyClip = new PolygonBox2DShape((PolygonShape) fixtureList.get(i).getShape());
                } else if (fixtureList.get(i).getShape() instanceof ChainShape) {
                    polyClip = new PolygonBox2DShape((ChainShape) fixtureList.get(i).getShape());
                }

                List<PolygonBox2DShape> rs = polyClip.differenceCS(circlePoly);
                for (int y = 0; y < rs.size(); y++) {
                    rs.get(y).circleContact(b.getPosition(), circRadius);
                    totalRS.add(rs.get(y));
                }
            }

        /*Here you give the clipped rock to the RockSpawner, so it will change the damn rock you also
        * set the mustDestroy boolean within the rock to true, so that the act method will know it has
        * to reconstruct it. */
            rockSpawner.changeRock(totalRS,a.getPosition().x,a.getPosition().y);
            ((UserData) body.getUserData()).mustDestroy = true;
        }
    }


    private float[] getVerts(Shape shape) {
        float [] verts = new float[0];
        if(shape instanceof PolygonShape){
            PolygonShape polyShape = (PolygonShape) shape;
            verts = new float[polyShape.getVertexCount()*2];
            for(int i = 0, j = 0; i < polyShape.getVertexCount(); i++){
                Vector2 vect = new Vector2();
                polyShape.getVertex(i, vect);
                verts[j++] = vect.x;
                verts[j++] = vect.y;
            }
        }
        if(shape instanceof ChainShape){
            ChainShape cshape = (ChainShape) shape;
            verts = null;
            if(cshape.isLooped()){
                verts = new float[cshape.getVertexCount()*2 - 2];
                for(int i = 0, j = 0; i < cshape.getVertexCount() - 1; i++){
                    Vector2 vect = new Vector2();
                    cshape.getVertex(i, vect);
                    verts[j++] = vect.x;
                    verts[j++] = vect.y;
                }
            }else{
                verts = new float[cshape.getVertexCount()*2];
                for(int i = 0, j = 0; i < cshape.getVertexCount(); i++){
                    Vector2 vect = new Vector2();
                    cshape.getVertex(i, vect);
                    verts[j++] = vect.x;
                    verts[j++] = vect.y;
                }
            }
        }
        return verts;
    }


}
