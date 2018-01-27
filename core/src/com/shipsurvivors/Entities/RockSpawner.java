package com.shipsurvivors.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.*;
import com.shipsurvivors.Utilities.SandBox.RockFixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SEO on 22/12/2017.
 */

/*This class will create and handle all the rocks. The sequence goes a little bit like this:

* 1.- WorldColissions will detect a rock needs to be clipped, it will clip it and then pass it to changeRock, it will also
* tag the rock as mustDestroy to indicate that it has changed and we need to substitute it for the updated version of it
* in the world.
*
* 2.-changeRock will store the new clipped rocks in polyVerts and will call set the boolean rockOrder to true
*
* 3.-In the render method of the GameScreen, we will call the method destroyOldBodies, this will destroy all the bodies
* tagged  with mustDestroy
*
* 4.- In the same render method, when rockOrder is true, we will call the method buildRock, this will create the new body
* (by 3 the old one is already gone), and also create the new fixtures, after we've done this we can clear the polyVerts
* array, which was only a temporary allocation for the data of the modified rock.
* */




/*
* A few things must be said about how we are drawing the rocks. We created a PolygonPack class, which basically can
* store a Body and an array of PolygonRegion's. What we do is the following, we create a fixed size array of polygon
* packs, then every determined ammount of time we call the method buildNewRock which basically scans the whole
* polygon pack array, until it gets one which doesnt have a body or has been destroyed, and tags it as destroyed so that
* the method buildRock will add a new body to it. Ok, now what happens when something collides to the rock? following
* the scheme described before, the body is goind to be destroyed, and when that happens,in the method destroyBody
* we'll tag the corresponding body in our PolygonPack array, so that when the new rock is built, we can also update our
* PolygonPack's. The Polygon Regions inside the PolygonPack is what we'll be drawing.
* */

public class RockSpawner  {
    /*The polyVerts will contain the info of a rock , the intention is to be able to create many but fordbugging onee will suffice*/
    private List<RockFixture> polyVerts = new ArrayList<RockFixture>();
    private PolygonPack[] polygonPacks = new PolygonPack[Constantes.MAX_NUM_OF_ROCKS];
    private boolean rockOrder;
    private TextureRegion rockPattern;
    private PolygonSpriteBatch polygonBatch = new PolygonSpriteBatch();
    private boolean rockFull;
    private float time;
    private RockCarrier rockCarrier;
    private Random random;
    private boolean rockNew;

    public RockSpawner(){
        //Initialize the Rock Carrier
        rockCarrier = new RockCarrier(new Texture(Gdx.files.internal("cowboy_cat.png")));

        //initialize the Rnaodm
        random = new Random();
    }
    public RockSpawner(Camera camera, AssetManager manager) {
        //Initialize the rock carrier
        rockCarrier = new RockCarrier(manager.get("cowboy_cat.png",Texture.class));

        //Initialize the Random
        random = new Random();

        polygonBatch.setProjectionMatrix(camera.combined);

        //Set the rock texture pattern
        rockPattern = new TextureRegion();
        rockPattern.setTexture(manager.get("rock_pattern.png",Texture.class));

        //initialize rocks
        initRocks();
    }



    /*Build Rock at position x, and position y, where these two are given in meters*/
    public void buildRock(World world){
        //Create the body of the rock
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;


        for (int i = 0;i<polyVerts.size();i++){
            Body rockBody = world.createBody(bodyDef);
            UserData userData = new UserData(UserData.ROCK);
            userData.mustDestroy = false;

            //Put the rock body where it used to be when teh bullet hit it
            rockBody.setUserData(userData);
            rockBody.setLinearVelocity(-Constantes.ROCK_VELOCITY,0);
            rockBody.setTransform(polyVerts.get(i).getX(),polyVerts.get(i).getY(),0);

            List<Fixture> fixtures = new ArrayList<Fixture>();
            //Create the fixtures for the rock

            for (int j = 0;j<polyVerts.get(i).getVertices().size();j++) {
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
                }
            }
            polyVerts.get(i).setFixtures(fixtures);
            updatePolygonPacks(fixtures, rockBody);
            if(isRockNew()){
                rockCarrier.assignOrder(rockBody,Constantes.ROCK_FINAL_POSITION_X);
                setRockNew(false);
            }
            System.out.println("number of bodies "+world.getBodyCount());
        }
        setRockOrder(false);
        polyVerts.clear();
    }


    public void changeRock(List<PolygonBox2DShape> newRock,float x,float y){
        setRockOrder(true);
        List<float[]> vertices = new ArrayList<float[]>();
        for(int i = 0;i<newRock.size();i++){
            vertices.add(newRock.get(i).verticesToLoop());
        }
        RockFixture rock = new RockFixture(vertices);
        rock.setX(x);
        rock.setY(y);
        polyVerts.add(rock);
    }

    public void destroyOldBodies(World world) {
            for (int i = 0; i < world.getBodyCount(); i++) {
                Array<Body> bodies = new Array<Body>();
                world.getBodies(bodies);
                UserData data = ((UserData) bodies.get(i).getUserData());
                if (data != null && data.getType() == UserData.ROCK) {
                    if (data.mustDestroy && !data.destroyed) {
                        //Tag the PolygonPacks to be destroyed
                        tagPolygonPacks(bodies.get(i));
                        world.destroyBody(bodies.get(i));
                        bodies.removeIndex(i);
                    }
                }
            }

    }

    public void setRockOrder(boolean rockOrder) {
        this.rockOrder = rockOrder;
    }

    public boolean isRockOrder() {
        return rockOrder;
    }

    public void setPolyVerts(List<RockFixture> polyVerts) {
        this.polyVerts = polyVerts;
    }

    public List<RockFixture> getPolyVerts() {
        return polyVerts;
    }

    public List<PolygonRegion> polygonRegionExtractor(RockFixture rockFixture){
        List<PolygonRegion> polygonRegions = new ArrayList<PolygonRegion>();
        for (int i = 0;i<rockFixture.getVertices().size();i++){

            //Don't forget that the vertices are in meters, you have to do the appropriate transformation
            short triangles[] = new EarClippingTriangulator().computeTriangles(VectorOperations.multiplyArrayByFloat(rockFixture.getVertices().get(i),Constantes.PIXELS_IN_METER)).toArray();
            PolygonRegion polygonRegion = new PolygonRegion(rockPattern,VectorOperations.multiplyArrayByFloat(rockFixture.getVertices().get(i),Constantes.PIXELS_IN_METER),triangles);
            polygonRegions.add(polygonRegion);
        }
        return polygonRegions;
    }


    public List<PolygonRegion> polygonRegionExtractor(List<Fixture> fixtures){
        List<PolygonRegion> polygonRegions = new ArrayList<PolygonRegion>();
        for (int i = 0;i<fixtures.size();i++){
            //Create a Box2DShape with the Fixture
            PolygonBox2DShape polygonShape = new PolygonBox2DShape(fixtures.get(i).getShape());
            short triangles[] = new EarClippingTriangulator().computeTriangles(VectorOperations.multiplyArrayByFloat(polygonShape.vertices(),Constantes.PIXELS_IN_METER)).toArray();
            PolygonRegion polygonRegion = new PolygonRegion(rockPattern,VectorOperations.multiplyArrayByFloat(polygonShape.vertices(),Constantes.PIXELS_IN_METER),triangles);
            polygonRegions.add(polygonRegion);

        }
        return polygonRegions;
    }

    public void tagPolygonPacks(Body bodyToDestroy){

        for (int i = 0;i<polygonPacks.length;i++){
            if(polygonPacks[i].getBody()!=null && polygonPacks[i].getBody().equals(bodyToDestroy)){
                polygonPacks[i].setDestroyTagged(true);
            }
        }
    }

    public void updatePolygonPacks(List<Fixture>fixtures,Body newBody){
        for(int i = 0;i<polygonPacks.length;i++){
            if(polygonPacks[i].isDestroyTagged()){
                polygonPacks[i].setBody(newBody);
                polygonPacks[i].setPolygonRegions(polygonRegionExtractor(fixtures));
                polygonPacks[i].setDestroyTagged(false);
                return;
            }
        }
    }

    public void drawRocks(Batch batch,float parentAlpha){
        if(rockCarrier.isCarrying() || rockCarrier.isReachTurnPoint()){
            batch.begin();
            rockCarrier.draw(batch,parentAlpha);
            batch.end();
        }

        polygonBatch.begin();
            for (int i = 0; i < polygonPacks.length; i++) {
                if(polygonPacks[i].getPolygonRegions()!=null) {
                    for (int j = 0; j < polygonPacks[i].getPolygonRegions().size(); j++) {
                        //System.out.println(polygonPacks[i].getBody().getPosition().x*Constantes.PIXELS_IN_METER);
                        polygonBatch.draw(polygonPacks[i].getPolygonRegions().get(j), polygonPacks[i].getBody().getPosition().x * Constantes.PIXELS_IN_METER, polygonPacks[i].getBody().getPosition().y * Constantes.PIXELS_IN_METER);
                    }
                }
            }

        polygonBatch.end();
    }

    public void initRocks(){
        for(int i = 0;i<polygonPacks.length;i++){
            PolygonPack pack = new PolygonPack();
            polygonPacks[i] = pack;
        }

    }

    public void updateRockManagement(float delta) {
        if (!isRockFull()) {
            time+=delta;

            if (time > Constantes.TIME_ROCK_SPAWINING) {
                buildNewRock(Constantes.LITTLE_ROCK_SIZE/Constantes.PIXELS_IN_METER,Constantes.ROCK_INITIAL_POSITION_X/Constantes.PIXELS_IN_METER,100/Constantes.PIXELS_IN_METER);
                time = 0;
            }

        }
            rockCarrier.act(delta);

    }


    public void buildNewRock(float rockSize,float x,float y){
        RockFixture rockFixture = new RockFixture(rockSize,x,y);
        List<PolygonRegion> polygonRegions = polygonRegionExtractor(rockFixture);
        for (int i = 0;i<polygonPacks.length;i++){

            if(polygonPacks[i].getBody()==null || polygonPacks[i].isDestroyed() ){
                polygonPacks[i].setPolygonRegions(polygonRegions);
                polygonPacks[i].setDestroyTagged(true);
                polyVerts.add(rockFixture);
                setRockOrder(true);
                setRockNew(true);
                System.out.println("Rock created "+i);
                return;
            }
        }
    }


    public void setRockFull(boolean rockFull) {
        this.rockFull = rockFull;
    }

    public boolean isRockFull() {
        return rockFull;
    }

    public float randomHeight(){
        return 0 + random.nextFloat()*(Constantes.SCREEN_HEIGHT-0);
    }

    public void setRockNew(boolean rockNew) {
        this.rockNew = rockNew;
    }

    public boolean isRockNew() {
        return rockNew;
    }
}
