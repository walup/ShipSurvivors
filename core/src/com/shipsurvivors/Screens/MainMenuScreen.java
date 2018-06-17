package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.Icon;
import com.shipsurvivors.Utilities.IconActor;

import java.util.Random;


/**
 * Created by SEO on 25/09/2017.
 */
public class MainMenuScreen extends BaseScreen {
    private Skin skin;
    private TextButton startGameButton;
    private TextButton settingsButton;
    private TextButton storyButton;
    private TextButton instructionsButton;
    private Table menuLayout;
    private Stage stage;
    private FlameDance flameDance;
    private Label title;
    private Texture blueprintTexture;
    private IconActor blueprint;
    private Label highScore;



    public MainMenuScreen(MainGame game) {
        super(game);
        //Set the stage
        stage = new Stage(new FitViewport(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT));
        //Set the Skin
        skin = game.getManager().get("menu_styles.json",Skin.class);
        //Initialize the buttons
        startGameButton = new TextButton("Start",skin,"menu_text_button_style");
        settingsButton = new TextButton("Settings",skin,"menu_text_button_style");
        storyButton = new TextButton("Story",skin,"menu_text_button_style");
        instructionsButton = new TextButton("Instructions",skin,"menu_text_button_style");
        //THe title
        title = new Label("Ship Survivors",skin,"label_style_title");

        //The highscore
        Preferences prefs = Gdx.app.getPreferences(Constantes.PREFERENCES_KEY);
        highScore = new Label("HighScore "+ prefs.getString(Constantes.PLAYER_NAME_KEY)+" "+ prefs.getFloat(Constantes.HIGHSCORE_KEY),skin,"label_style_title");
        highScore.setPosition(0,0);

        //Initilalize the table
        menuLayout = new Table();
        menuLayout.setSize(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT);
        menuLayout.align(Align.center);
        //set the flame dance
        flameDance = new FlameDance();

        //Set the blueprint
        blueprintTexture = game.getManager().get("blueprint_ship.jpg",Texture.class);
        blueprintTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blueprint = new IconActor(blueprintTexture,Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT);


    }

    @Override
    public void show() {
        //Now put everything where it is supposed to go on the table
        menuLayout.add(title).pad(Constantes.STANDARD_BUTTON_PADDING);
        menuLayout.row();
        menuLayout.add(startGameButton).padTop(Constantes.STANDARD_BUTTON_PADDING).padBottom(Constantes.STANDARD_BUTTON_PADDING);
        menuLayout.row();
        menuLayout.add(settingsButton).padTop(Constantes.STANDARD_BUTTON_PADDING).padBottom(Constantes.STANDARD_BUTTON_PADDING);
        menuLayout.row();
        menuLayout.add(storyButton).padTop(Constantes.STANDARD_BUTTON_PADDING).padBottom(Constantes.STANDARD_BUTTON_PADDING);
        menuLayout.row();
        menuLayout.add(instructionsButton).padTop(Constantes.STANDARD_BUTTON_PADDING).padBottom(Constantes.STANDARD_BUTTON_PADDING);
        menuLayout.row();
        menuLayout.add(highScore);
        //Add the layout to the stage
        stage.addActor(blueprint);
        stage.addActor(flameDance);
        stage.addActor(menuLayout);

        //Activate the buttons
        activateButtons();
        //Set the input proccesor
        Gdx.input.setInputProcessor(stage);
        //Start the music
        game.getManager().get("game_song.wav",Music.class).setVolume(game.getMusicVolumeLevel());
        game.getManager().get("game_song.wav",Music.class).setLooping(true);
        game.getManager().get("game_song.wav",Music.class).play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        stage.dispose();
        flameDance.dispose();
        blueprintTexture.dispose();


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
        stage.getViewport().getCamera().update();
    }

    private void activateButtons(){
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getManager().get("game_song.wav",Music.class).stop();
                game.setScreen(new GameScreen(game));
            }
        });

        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        storyButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StoryScreen(game));
            }
        });
        instructionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InstructionScreen(game));
            }
        });
    }

    public class FlameDance extends Actor {
        Flame greenFlame;
        Flame redFlame;
        Random random = new Random();
        ParticleEffect greenFlameEffect;
        ParticleEffect redFlameEffect;

        public FlameDance(){
            //Initiate the flames
            greenFlame = new Flame(Constantes.SCREEN_WIDTH*random.nextFloat(),Constantes.SCREEN_HEIGHT*random.nextFloat(),Constantes.FLAME_VELOCITY*random.nextFloat(),Constantes.FLAME_VELOCITY*random.nextFloat(),Constantes.FLAME_RADIUS*random.nextFloat());
            redFlame = new Flame(Constantes.SCREEN_WIDTH*random.nextFloat(),Constantes.SCREEN_HEIGHT*random.nextFloat(),Constantes.FLAME_VELOCITY*random.nextFloat(),Constantes.FLAME_VELOCITY*random.nextFloat(),Constantes.FLAME_RADIUS*random.nextFloat());
            //Set the particles effects
            greenFlameEffect = game.getManager().get("green_flame",ParticleEffect.class);
            greenFlameEffect.getEmitters().first().setPosition(greenFlame.getX(),greenFlame.getY());
            greenFlameEffect.start();
            redFlameEffect = game.getManager().get("red_flame", ParticleEffect.class);
            redFlameEffect.getEmitters().first().setPosition(redFlame.getX(),redFlame.getY());
            redFlameEffect.start();

        }

        @Override
        public void act(float delta) {
            //Update the position of the flames
            redFlame.updateCircle(delta);
            greenFlame.updateCircle(delta);
            //Check if the flame touched the edge of screen and correct velocity
            checkBoundaries(redFlame);
            checkBoundaries(greenFlame);
            //Adjust positions of the flame effects
            redFlameEffect.setPosition(redFlame.getX(),redFlame.getY());
            greenFlameEffect.setPosition(greenFlame.getX(),greenFlame.getY());
            redFlameEffect.update(delta);
            greenFlameEffect.update(delta);

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            redFlameEffect.draw(batch);
            greenFlameEffect.draw(batch);
        }

        public void checkBoundaries(Flame flame){
            //Right boundary
            if(flame.getX()>Constantes.SCREEN_WIDTH){
                flame.setVelocityX(-Constantes.FLAME_VELOCITY);
            }
            //Left boundary
            else if(flame.getX()<0){
                flame.setVelocityX(Constantes.FLAME_VELOCITY);
            }
            //Upper boundary
            if(flame.getY()>Constantes.SCREEN_HEIGHT){
                flame.setVelocityY(-Constantes.FLAME_VELOCITY);
            }
            if(flame.getY()<0){
                flame.setVelocityY(Constantes.FLAME_VELOCITY);
            }
        }

        public void dispose(){
            greenFlameEffect.dispose();
            redFlameEffect.dispose();
        }
    }

    public class Flame{
        private Circle circle;
        private float velocityX;
        private float velocityY;

        public Flame(float centerX, float centerY,float velocityX,float velocityY,float radius){
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            circle = new Circle();
            circle.set(centerX,centerY,radius);
        }

        public void updateCircle(float delta){
            circle.set(circle.x +delta*velocityX,circle.y +delta*velocityY,circle.radius);
        }

        public Circle getCircle() {
            return circle;
        }

        public void setVelocityX(float velocityX) {
            this.velocityX = velocityX;
        }

        public void setVelocityY(float velocityY) {
            this.velocityY = velocityY;
        }

        public float getX(){
            return circle.x;
        }

        public float getY(){
            return circle.y;
        }
    }
}
