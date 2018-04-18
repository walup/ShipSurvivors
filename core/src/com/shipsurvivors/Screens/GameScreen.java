package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Entities.Attachable;
import com.shipsurvivors.Entities.CardContainer;
import com.shipsurvivors.Entities.HeartContainer.HeartContainer;
import com.shipsurvivors.Entities.Referee;
import com.shipsurvivors.Entities.RockSpawner;
import com.shipsurvivors.Entities.Ship;
import com.shipsurvivors.UI.ShipControls;
import com.shipsurvivors.UI.WorldCollisions;
import com.shipsurvivors.Utilities.Armory;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.ScrollingBackground;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEO on 25/09/2017.
 */

public class GameScreen extends BaseScreen {
    private Stage stage;
    private OrthographicCamera camera;
    private World world;
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
    private WorldCollisions worldCollisions;

    //For Debugging
    private Box2DDebugRenderer renderer;
    private OrthographicCamera cameraForDebug;

    //Referee
    private Referee referee;
    private Label scoreLabel;

    //We'll need a multiplexer
    private InputMultiplexer inputMultiplexer;

    //Finally the Dj
    private Dj dj;


    public GameScreen(MainGame game) {
        super(game);
        //initialize the stage
        stage = new Stage(new FitViewport(Constantes.WORLD_WIDTH,Constantes.WORLD_HEIGHT));

        //initialize the world
        world = new World(new Vector2(0,0),true);

        //Initialize the scrolling background
       background = new ScrollingBackground(game.getManager().get("background.png",Texture.class));

        //Initialize the Dj
        dj = new Dj(game.getManager().get("song2.wav",Music.class));

        //We initialize the ship
        ship = new Ship(world,game.getManager().get("shipatlas.atlas",TextureAtlas.class),20,20, Constantes.SHIP_WIDTH,Constantes.SHIP_HEIGHT);

        //Initialize the rock spawner

        rockSpawner = new RockSpawner(stage.getCamera(),game.getManager());
        //Initialize the heart Container
        heartContainer = new HeartContainer(game.getManager().get("hearts.atlas",TextureAtlas.class),Constantes.HEART_CONTAINER_X,Constantes.HEART_CONTAINER_Y);

        //Initialize the Referee (remember that this interacts with the heart container, and then also world colissions
        // needs the referee)

        referee = new Referee(heartContainer);

        //Initilize WorldColissions
        worldCollisions = new WorldCollisions(rockSpawner,ship,referee);

        //Initialize the label
        initializeLabel();

        //Initialize DebugShit

        //cameraForDebug = new OrthographicCamera(16,9);
        //renderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

        //Initialize the card container
        armory = new Armory(Gdx.files.internal("weapons.json"),world);

        List<Attachable> attachables = new ArrayList<Attachable>();
        attachables.addAll(armory.weaponsRequest("arduino_gun",game.getManager(),3));
        attachables.addAll(armory.weaponsRequest("football_cat",game.getManager(),3));
        attachables.addAll(armory.weaponsRequest("heartbreaker",game.getManager(),3));

        cardContainer = new CardContainer(attachables,game.getManager().get("card_container_background.png",Texture.class));

        //Initialize the controls
        shipControls = new ShipControls(ship,cardContainer);

        //Add everything to the stage
        stage.addActor(background);

        stage.addActor(ship);
        stage.addActor(cardContainer);
        stage.addActor(heartContainer);
        stage.addActor(scoreLabel);


        inputMultiplexer = new InputMultiplexer();
        /*We add to the multiplexer the ship controls as a GestureDetector, and the same object as an InputProcessor*/
        inputMultiplexer.addProcessor(new GestureDetector(shipControls));
        inputMultiplexer.addProcessor(shipControls);

        Gdx.input.setInputProcessor(inputMultiplexer);
        world.setContactListener(worldCollisions);

        //Play the music is hard to lose it, it's constantly on request cause you juice it.
        dj.playMusic();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*Check if the player is still alive */
        if(heartContainer.heDead()){
            game.setScreen(new GameOverScreen(game,referee.getScore()));
        }

        //Update the world and the stage.
        stage.act(delta);
        rockSpawner.updateRockManagement(delta);
        box2DTimeStep(delta);

        //Here change the score if neccesary
        if(referee.isChangedScore()){
            scoreLabel.setText("Score "+referee.getScore());
            referee.setChangedScore(false);
        }
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
//        renderer.render(world,cameraForDebug.combined);

        shipControls.renderShipControls();

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

    private void initializeLabel(){
        Skin skin = game.getManager().get("settings.json");
        scoreLabel = new Label("Score "+0,skin,"label_style");
    }


    public class Dj{
        private Music gameMusic;

        public Dj(Music gameMusic){
            gameMusic.setLooping(true);
            gameMusic.setVolume(game.getMusicVolumeLevel());
            this.gameMusic = gameMusic;
        }

        public void playMusic(){
            gameMusic.play();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
        stage.getCamera().update();
    }
}
