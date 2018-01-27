package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shipsurvivors.Entities.RockSpawner;
import com.shipsurvivors.UI.WorldCollisions;
import com.shipsurvivors.Utilities.SandBox.RockFixture;
import com.shipsurvivors.Utilities.SandBox.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEO on 26/12/2017.
 */
public class TestScreen extends BaseScreen {
    SpriteBatch batch;
    World world;
    Box2DDebugRenderer renderer;
    OrthographicCamera camera;
    private float accu;
    private static final float TIME_STEP = 1 / 60f;
    private static int speedIte = 6, posIte = 2;
    private int count = 0;
    private RockSpawner rockSpawner;
    private WorldCollisions worldCollisions;
    //public List<GroundFixture> groundFixtures = new ArrayList<GroundFixture>();



    public TestScreen(MainGame game) {
        super(game);

    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -9.81f), false);
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/10);

        rockSpawner = new RockSpawner();
        List<float[]> verts = new ArrayList<float[]>();
        float[] points = {-60,-10,-60,-40f,60,-40f,60,-10};
        verts.add(points);
        RockFixture grFix = new RockFixture(verts);
        rockSpawner.getPolyVerts().add(grFix);
        rockSpawner.setRockOrder(true);

        createBall(UserData.BALL, new Vector2(-10,0));
        createBall(UserData.BULLET, new Vector2(10,0));
        worldCollisions = new WorldCollisions(rockSpawner);
        world.setContactListener(worldCollisions);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(world, camera.combined);

        if(Gdx.input.justTouched()){
            int type;
            count++;
            if(count %2 == 0){
                type = UserData.BALL;
            }else{
                type = UserData.BULLET;
            }
            Vector3 box2Dpos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            createBall(type, new Vector2(box2Dpos.x, box2Dpos.y));
        }

       rockSpawner.destroyOldBodies(world);


        if(rockSpawner.isRockOrder()) {
            rockSpawner.buildRock(world);
        }
        box2dTimeStep(Gdx.graphics.getDeltaTime());
    }


    public Body createBall(int type, Vector2 position) {
        BodyDef defBall = new BodyDef();
        defBall.type = BodyDef.BodyType.DynamicBody;
        defBall.position.set(position);
        Body ball = world.createBody(defBall);
        ball.setUserData(new UserData(type));

        FixtureDef fixDefBall = new FixtureDef();
        fixDefBall.density = .25f;
        fixDefBall.restitution = .75f;
        CircleShape rond = new CircleShape();
        rond.setRadius(1);

        fixDefBall.shape = rond;
        ball.createFixture(fixDefBall);
        rond.dispose();

        return ball;
    }

    private void box2dTimeStep(float deltaTime) {
        float delta = Math.min(deltaTime, 0.25f);
        accu += delta;
        while (accu >= TIME_STEP) {
            world.step(TIME_STEP, speedIte, posIte);
            accu -= TIME_STEP;
        }
    }

}





