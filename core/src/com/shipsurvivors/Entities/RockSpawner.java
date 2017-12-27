package com.shipsurvivors.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
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
public class RockSpawner  {

    private Texture rockSpawnerRight;
    private Texture rockSpawnerLeft;
    /*The polyVerts will contain the info of a rock , the intention is to be able to create many but fordbugging onee will suffice*/
    private List<RockFixture> polyVerts = new ArrayList<RockFixture>();
    private PolygonPack[] polygonPacks = new PolygonPack[Constantes.MAX_NUM_OF_ROCKS];
    private boolean rockOrder;
    private TextureRegion rockPattern;
    private PolygonSpriteBatch polygonBatch = new PolygonSpriteBatch();
    private Stage stage;
    private boolean rockFull;
    private float time;


    public RockSpawner(){

    }
    public RockSpawner(Camera camera){
        this.stage = stage;
        polygonBatch.setProjectionMatrix(camera.combined);

        //Set the rock texture pattern
        rockPattern = new TextureRegion();
        rockPattern .setTexture(new Texture(Gdx.files.internal("rock_pattern.png")));

        //initialize rocks
        initRocks();

/*
        polyVerts.add(rockFixture);
        setRockOrder(true);
*/
    }

    /*Build Rock at position x, and position y, where these two are given in meters*/
    public void buildRock(World world,  float x, float y){
        //Create the body of the rock
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);


        for (int i = 0;i<polyVerts.size();i++){
            Body rockBody = world.createBody(bodyDef);
            UserData userData = new UserData(UserData.ROCK);
            rockBody.setUserData(userData);

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


                updatePolygonPacks(fixtures, rockBody);
                polyVerts.get(i).setFixtures(fixtures);

        }
        setRockOrder(false);
        polyVerts.clear();
    }


    public void changeRock(List<PolygonBox2DShape> newRock){
        setRockOrder(true);
        List<float[]> vertices = new ArrayList<float[]>();
        for(int i = 0;i<newRock.size();i++){
            vertices.add(newRock.get(i).verticesToLoop());
        }
        RockFixture rock = new RockFixture(vertices);
        polyVerts.add(rock);
    }

    public void destroyOldBodies(World world){
        for (int i = 0; i < world.getBodyCount(); i++) {
            Array<Body> bodies = new Array<Body>();
            world.getBodies(bodies);
            UserData data = ((UserData) bodies.get(i).getUserData());
            if (data != null && data.getType() == UserData.ROCK) {
                if ((data.mustDestroy || rockOrder) && !data.destroyed) {
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
        /*We see which of the bodies of the PolygonPacks is going to be destroyed and tag it accordingly
        * so that we can change the PolygonRegions when we create it again */

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

            }
        }
    }


    /*
    * Something needs to be said about how we are going to render the stones. We use a new kind of Object called
    * PolygonPack which holds information of the Polygons to render, like its PolygonShape or its Body. whenever the Body
    * needs to be destroyed we tag the PolygonPacks that need updating, and when the Body is built again we update them using
    * the method updatePolygonPacks, the PolygonShape can be used to draw all the rocks in the next method
    * */

    public void drawRocks(){
        polygonBatch.begin();
            for (int i = 0; i < polygonPacks.length; i++) {
                if(polygonPacks[i].getPolygonRegions()!=null) {
                    for (int j = 0; j < polygonPacks[i].getPolygonRegions().size(); j++) {
                        polygonBatch.draw(polygonPacks[i].getPolygonRegions().get(j), polygonPacks[i].getBody().getPosition().x * Constantes.PIXELS_IN_METER, polygonPacks[i].getBody().getPosition().y * Constantes.PIXELS_IN_METER);
                    }
                }
            }

        polygonBatch.end();
    }

    /*Methods to manage Rocks*/
    /*This method initializes the rocks*/
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
                buildNewRock(Constantes.LITTLE_ROCK_SIZE/Constantes.PIXELS_IN_METER,500/Constantes.PIXELS_IN_METER,100/Constantes.PIXELS_IN_METER);
                time = 0;
                System.out.println("New Rock created");

            }

        }
    }


    public void buildNewRock(float rockSize,float x,float y){
        for (int i = 0;i<polygonPacks.length;i++){
            RockFixture rockFixture = new RockFixture(rockSize,x,y);
            List<PolygonRegion> polygonRegions = polygonRegionExtractor(rockFixture);

            if(polygonPacks[i].getBody()==null || polygonPacks[i].isDestroyed() ){
                polygonPacks[i].setPolygonRegions(polygonRegions);
                polygonPacks[i].setDestroyTagged(true);
                polyVerts.add(rockFixture);
                setRockOrder(true);
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




}
