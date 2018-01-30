package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Entities.CardContainer;
import com.shipsurvivors.Entities.HeartContainer.HeartContainer;
import com.shipsurvivors.Entities.RockSpawner;
import com.shipsurvivors.Entities.Ship;
import com.shipsurvivors.UI.ShipControls;
import com.shipsurvivors.UI.WorldCollisions;
import com.shipsurvivors.Utilities.Armory;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.ScrollingBackground;

/**
 * Created by SEO on 25/09/2017.
 */

public class GameScreen extends BaseScreen {
    private Stage stage;
    private OrthographicCamera camera;
    private World world;
    private Sound rotatingSound;
    private Music gameMusic;
    public static  ShipControls shipControls;
    private Ship ship;
    private CardContainer cardContainer;
    private ScrollingBackground background;
    private Armory armory;
    private RockSpawner rockSpawner;
    private HeartContainer heartContainer;
    // Variables needed for the rendering
    private float accu;
    private static final float TIME_STEP = 1 / 60f;
    private static int speedIte = 6, posIte = 2;
    private int count = 0;
    // The contact handler
    WorldCollisions worldCollisions;

    //For Debugging
    Box2DDebugRenderer renderer;
    OrthographicCamera cameraForDebug;







    public GameScreen(MainGame game) {
        super(game);
        //initialize the stage
        stage = new Stage(new FitViewport(640,360));

        //initialize the world
        world = new World(new Vector2(0,0),true);

        //Initialize the scrolling background
       background = new ScrollingBackground(game.getManager().get("background.png",Texture.class));


        //Initialize the sounds and music


        //We initialize the ship
        ship = new Ship(world,game.getManager().get("shipatlas.atlas",TextureAtlas.class),20,20, Constantes.SHIP_WIDTH,Constantes.SHIP_HEIGHT);

        //Initialize the rock spawner

        rockSpawner = new RockSpawner(stage.getCamera(),game.getManager());
        //Initilize WorldColissions
        worldCollisions = new WorldCollisions(rockSpawner,ship);

        //Initialize DebugShit

        cameraForDebug = new OrthographicCamera(16,9);
        renderer = new Box2DDebugRenderer();

        //Initialize the heart container
        heartContainer = new HeartContainer(game.getManager().get("hearts.atlas",TextureAtlas.class),Constantes.HEART_CONTAINER_X,Constantes.HEART_CONTAINER_Y);


    }

    @Override
    public void show() {

        //Initialize the card container
        armory = new Armory(Gdx.files.internal("weapons.json"),world);

        cardContainer = new CardContainer(armory.weaponsRequest("arduino_gun",game.getManager(),5),game.getManager().get("card_container_background.png",Texture.class));

        //Initialize the controls
        shipControls = new ShipControls(ship,cardContainer);

        //Add everything to the stage
        stage.addActor(background);

        stage.addActor(ship);
        stage.addActor(cardContainer);
        stage.addActor(heartContainer);
        Gdx.input.setInputProcessor(new GestureDetector(shipControls));
        world.setContactListener(worldCollisions);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update the world and the stage.
        stage.act(delta);
        rockSpawner.updateRockManagement(delta);
        box2DTimeStep(delta);

        //Destroy bodies which need destroying

        rockSpawner.destroyOldBodies(world);

        //if needed build the new bodies

        if(rockSpawner.isRockOrder()){
            rockSpawner.buildRock(world);
        }


        //Render the stage.
        stage.draw();
        //Draw the rocks
        rockSpawner.drawRocks(stage.getBatch(),1);
        renderer.render(world,cameraForDebug.combined);

    }

    @Override
    public void dispose() {
        heartContainer.dispose();

    }


    private void box2DTimeStep(float deltaTime) {
        float delta = Math.min(deltaTime, 0.25f);
        accu += delta;
        while (accu >= TIME_STEP) {
            world.step(TIME_STEP, speedIte, posIte);
            accu -= TIME_STEP;
        }
    }



}
