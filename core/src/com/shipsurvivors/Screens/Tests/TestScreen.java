package com.shipsurvivors.Screens.Tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shipsurvivors.Screens.BaseScreen;
import com.shipsurvivors.Screens.MainGame;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.CollisionGeometry;
import com.shipsurvivors.Utilities.SandBox.PolygonBox2DShape;
import com.shipsurvivors.Utilities.SandBox.RockFixture;
import com.shipsurvivors.Utilities.SandBox.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEO on 28/01/2018.
 */
public class TestScreen extends BaseScreen {
    private World world;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera cameraForDebug;
    private float bulletRadius = 0.4f;
    private List<RockFixture> polyVerts = new ArrayList<RockFixture>();
    /*This is the test screen, here i will do some experiments regarding the polygon clipping, which diesn't seem
    * sto be working*/

    public TestScreen(MainGame game) {
        super(game);
        world = new World(new Vector2(0, 0),true);
        cameraForDebug = new OrthographicCamera(16,9);
        renderer = new Box2DDebugRenderer();

    }




    @Override
    public void show() {
        //Ground
        List<float[]> groundVertices = new ArrayList<float[]>();
        float[] vertices = {-2.0f,-2.0f,-2.0f,2.0f,2.0f,2.0f,2.0f,-2.0f};
        groundVertices.add(vertices);
        RockFixture groundFixture = new RockFixture(groundVertices);

        RockFixture bulletFixture = new RockFixture(bulletRadius,-1f,2f);
        RockFixture rockFixture = new RockFixture(2f,0,0);
        //Create the two rocks
        polyVerts.add(bulletFixture);
        buildRocks(world);
        polyVerts.add(rockFixture);
        buildRocks(world);
        polyVerts.add(groundFixture);
        //Add them to the world
        buildRocks(world);
        //Now clip them
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        clippingRock(bodies.get(2),bodies.get(0));
        buildRocks(world);
        world.getBodies(bodies);
        bodies.get(3).setTransform(5,0,0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render(world,cameraForDebug.combined);
    }

    public void buildRocks(World world){
        //Create the body of the rock
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;


        for (int i = 0;i<polyVerts.size();i++){
            Body rockBody = world.createBody(bodyDef);

            List<Fixture> fixtures = new ArrayList<Fixture>();
            //Create the fixtures for the rock

            for (int j = 0;j<polyVerts.get(i).getVertices().size();j++) {
                //If we have at least a square then we create a new fixture with it
                if (this.polyVerts.get(i).getVertices().get(j).length >= 6) {
                    ChainShape chainShape = new ChainShape();
                    chainShape.createLoop(polyVerts.get(i).getVertices().get(j));
                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = chainShape;
                    fixtureDef.density = 10;
                    fixtureDef.friction = 0.8f;
                    fixtureDef.filter.categoryBits = Constantes.CATEGORY_ROCK;
                    fixtureDef.filter.maskBits = Constantes.CATEGORY_BULLET;
                    fixtures.add(rockBody.createFixture(fixtureDef));

                    if (fixtures.get(0).getShape() instanceof ChainShape) {
                        System.out.println("chain looped " + ((ChainShape) fixtures.get(0).getShape()).isLooped());
                    }
                }
            }
            polyVerts.get(i).setFixtures(fixtures);
        }
        polyVerts.clear();
    }

    private void clippingRock(Body a, Body b) {
            Body body = a;
            List<PolygonBox2DShape> totalRS = new ArrayList<PolygonBox2DShape>();

            float[] circleVertices = CollisionGeometry.approxCircle(b.getPosition().x, b.getPosition().y, bulletRadius, Constantes.ROCK_NUM_SEGMENTS);
            ChainShape shape = new ChainShape();
            shape.createLoop(circleVertices);

            PolygonBox2DShape circlePoly = new PolygonBox2DShape(shape);
            System.out.println(circlePoly.toString());

            Array<Fixture> fixtureList = body.getFixtureList();
            int fixCount = fixtureList.size;

            for (int i = 0; i < fixCount; i++) {

                PolygonBox2DShape polyClip = null;
                if (fixtureList.get(i).getShape() instanceof PolygonShape) {
                    polyClip = new PolygonBox2DShape((PolygonShape) fixtureList.get(i).getShape());
                } else if (fixtureList.get(i).getShape() instanceof ChainShape) {
                    polyClip = new PolygonBox2DShape((ChainShape) fixtureList.get(i).getShape());
                    System.out.println(((ChainShape) fixtureList.get(i).getShape()).isLooped());
                }
                System.out.println(polyClip.toString());

                List<PolygonBox2DShape> rs = polyClip.differenceCS(circlePoly);
                for (int y = 0; y < rs.size(); y++) {
                    //rs.get(y).circleContact(b.getPosition(), bulletRadius);
                    totalRS.add(rs.get(y));
                    System.out.println(rs.get(y).toString());
                }
            }

        /*Here you give the clipped rock to the RockSpawner, so it will change the damn rock you also
        * set the mustDestroy boolean within the rock to true, so that the act method will know it has
        * to reconstruct it. */
            changeRock(totalRS,a.getPosition().x,a.getPosition().y);
    }

    public void changeRock(List<PolygonBox2DShape> newRock,float x,float y){
        List<float[]> vertices = new ArrayList<float[]>();
        for(int i = 0;i<newRock.size();i++){
            vertices.add(newRock.get(i).verticesToLoop());
        }
        RockFixture rock = new RockFixture(vertices);
        rock.setX(x);
        rock.setY(y);
        polyVerts.add(rock);
    }
}
