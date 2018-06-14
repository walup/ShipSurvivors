package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Entities.AI;
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

    //Referee and AI
    private Referee referee;
    private Label scoreLabel;
    private AI sapien;

    //We'll need a multiplexer
    private InputMultiplexer inputMultiplexer;

    //Finally the Dj
    private Dj dj;

    //The pause window and stuff it needs.
    private ImageButton pauseButton;
    private MyPauseWindow pauseWindow;
    private TextButton goToMenuButton;
    private TextButton settingsButton;
    private TextButton continueButton;



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
        //Initialize the AI
        sapien = new AI();
        //Initialize the rock spawner
        rockSpawner = new RockSpawner(stage.getCamera(),game.getManager(),sapien);
        //Initialize the heart Container
        heartContainer = new HeartContainer(game.getManager().get("hearts.atlas",TextureAtlas.class),Constantes.HEART_CONTAINER_X,Constantes.HEART_CONTAINER_Y);
        //Initialize the Referee (remember that this interacts with the heart container, and then also world colissions
        // needs the referee)
        referee = new Referee(heartContainer);
        //Initilize WorldColissions
        worldCollisions = new WorldCollisions(rockSpawner,ship,referee);
        //Initialize the label
        initializeLabel();
        //Here we initialize everything we need for the pause window.
        configurePauseScreen();
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
        stage.addActor(pauseButton);
        stage.addActor(ship);
        stage.addActor(cardContainer);
        stage.addActor(heartContainer);
        stage.addActor(scoreLabel);
        stage.addActor(pauseWindow);
        stage.addActor(pauseWindow.changeVolWindow);
        inputMultiplexer = new InputMultiplexer();
        /*We add to the multiplexer the ship controls as a GestureDetector, and the same object as an InputProcessor*/
        inputMultiplexer.addProcessor(new GestureDetector(shipControls));
        inputMultiplexer.addProcessor(shipControls);
        inputMultiplexer.addProcessor(stage);

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
        sapien.updateAI(delta,referee.getScore());
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

        public void changeSfxVolume(float volume){

        }
    }


    public class MyPauseWindow extends Window{

        /*My Pause window will have another inside Window, which will allow the player to change to volume of the music
        * from inside the game*/

        private Window changeVolWindow;
        private Slider musicVolumeSlider;
        private Slider specialEffectsSlider;
        private Label musicVolumeLabel;
        private Label specialEffectsVolumeLabel;
        private Image musicImage;
        private Image specialEffectsImage;
        private TextButton returnButton;


        public MyPauseWindow(String title, Skin skin) {
            super(title, skin);
        }

        public MyPauseWindow(String title, Skin skin, String styleName){
            super(title,skin,styleName);
            changeVolWindow = new Window("Settings",skin,styleName);
            Skin settingsSkin = game.getManager().get("settings.json",Skin.class);

            /*Initialize the sliders*/
            musicVolumeSlider = new Slider(0,1, (float) 0.01,false,settingsSkin,"slider_style");
            musicVolumeSlider.setValue(game.getMusicVolumeLevel());
            specialEffectsSlider = new Slider(0,1,0.01f,false,settingsSkin,"slider_style");
            specialEffectsSlider.setValue(game.getSoundEffectsLevel());


            /*Initialize the labels*/
            musicVolumeLabel = new Label("Music Volume",settingsSkin,"label_style");
            specialEffectsVolumeLabel = new Label("Sfx Volume",settingsSkin,"label_style");

            /*Initialize the return button*/
            returnButton = new TextButton("Return",settingsSkin,"text_button_style");

            /*Initialize the images*/
            TextureRegion musicTexture = settingsSkin.getAtlas().findRegion("musicicon");
            musicTexture.getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            musicImage = new Image(musicTexture);
            musicImage.setSize(Constantes.SETTINGS_ICON_WIDTH,Constantes.SETTINGS_ICON_HEIGHT);

            TextureRegion specialEffectsTexture = settingsSkin.getAtlas().findRegion("soundeffectsicon");
            specialEffectsTexture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            specialEffectsImage = new Image(specialEffectsTexture);
            specialEffectsImage.setSize(Constantes.SETTINGS_ICON_WIDTH,Constantes.SETTINGS_ICON_HEIGHT);

            /*Add everything to the Window*/
            changeVolWindow.setSize(Constantes.MINI_WINDOW_WIDTH,Constantes.MINI_WINDOW_HEIGHT);
            changeVolWindow.setPosition((Constantes.SCREEN_WIDTH-changeVolWindow.getWidth())/2,(Constantes.SCREEN_HEIGHT-changeVolWindow.getHeight())/2);
            changeVolWindow.align(Align.center);
            changeVolWindow.add(specialEffectsSlider);
            changeVolWindow.add(specialEffectsVolumeLabel).pad(Constantes.STANDARD_BUTTON_PADDING);
            changeVolWindow.add(specialEffectsImage).pad(Constantes.STANDARD_BUTTON_PADDING);
            changeVolWindow.row();
            changeVolWindow.add(musicVolumeSlider);
            changeVolWindow.add(musicVolumeLabel).pad(Constantes.STANDARD_BUTTON_PADDING);
            changeVolWindow.add(musicImage).pad(Constantes.STANDARD_BUTTON_PADDING);
            changeVolWindow.row();
            changeVolWindow.add(returnButton).colspan(3);

            changeVolWindow.setVisible(false);

            /*Here we set the listener for the button*/
            returnButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Preferences prefs = Gdx.app.getPreferences(Constantes.PREFERENCES_KEY);
                    prefs.putFloat(Constantes.MUSIC_VOLUME_KEY,musicVolumeSlider.getValue());
                    prefs.putFloat(Constantes.SPECIAL_EFFECTS_KEY,specialEffectsSlider.getValue());
                    game.setMusicVolumeLevel(musicVolumeSlider.getValue());
                    game.setSoundEffectsLevel(specialEffectsSlider.getValue());
                    changeVolWindow.setVisible(false);
                    setVisible(true);
                }
            });

            /*Here we set the listeners for the sliders. */
            musicVolumeSlider.addListener(new EventListener() {
                @Override
                public boolean handle(Event event) {
                    dj.gameMusic.setVolume(musicVolumeSlider.getValue());
                    return true;
                }
            });

            specialEffectsSlider.addListener(new EventListener() {
                @Override
                public boolean handle(Event event) {
                    dj.changeSfxVolume(specialEffectsSlider.getValue());
                    return true;
                }
            });

        }

        public void activateSettings(){
            setVisible(false);
            changeVolWindow.setVisible(true);
        }
    }
    //In this method we add the buttons to the Pause Screen
    public void configurePauseScreen(){
        pauseButton = new ImageButton(game.getManager().get("game_styles.json",Skin.class),"pause_button_style");
        pauseButton.setSize(Constantes.BUTTON_WIDTH,Constantes.BUTTON_HEIGHT);
        pauseButton.setPosition(Constantes.WORLD_WIDTH-Constantes.CARD_CONTAINER_WIDTH-pauseButton.getWidth(),Constantes.WORLD_HEIGHT-pauseButton.getHeight());
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseWindow.setVisible(true);
            }
        });



        goToMenuButton = new TextButton("Return to menu",game.getManager().get("game_styles.json",Skin.class),"text_button_style");
        continueButton = new TextButton("Continue",game.getManager().get("game_styles.json",Skin.class),"text_button_style");
        settingsButton = new TextButton("Settings",game.getManager().get("game_styles.json",Skin.class),"text_button_style");

        pauseWindow = new MyPauseWindow("Pause",game.getManager().get("game_styles.json",Skin.class),"window_style");
        pauseWindow.setSize(Constantes.MINI_WINDOW_WIDTH,Constantes.MINI_WINDOW_HEIGHT);
        pauseWindow.setPosition((Constantes.SCREEN_WIDTH-pauseWindow.getWidth())/2,(Constantes.SCREEN_HEIGHT-pauseWindow.getHeight())/2);
        pauseWindow.align(Align.center);
        pauseWindow.setResizable(false);
        pauseWindow.setMovable(false);
        pauseWindow.row();
        pauseWindow.add(continueButton).size(Constantes.MENU_BUTTON_WIDTH,Constantes.MENU_BUTTON_HEIGHT).pad(Constantes.STANDARD_BUTTON_PADDING);
        pauseWindow.row();
        pauseWindow.add(settingsButton).size(Constantes.MENU_BUTTON_WIDTH,Constantes.MENU_BUTTON_HEIGHT).pad(Constantes.STANDARD_BUTTON_PADDING);
        pauseWindow.row();
        pauseWindow.add(goToMenuButton).size(Constantes.MENU_BUTTON_WIDTH,Constantes.MENU_BUTTON_HEIGHT).pad(Constantes.STANDARD_BUTTON_PADDING);
        pauseWindow.setVisible(false);

        /*Configure the buttons*/

        goToMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dj.gameMusic.stop();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseWindow.setVisible(false);
            }
        });
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseWindow.activateSettings();
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
        stage.getCamera().update();
    }
}
