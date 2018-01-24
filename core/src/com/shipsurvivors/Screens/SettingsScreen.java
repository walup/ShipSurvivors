package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.Icon;

/**
 * Created by SEO on 25/09/2017.
 */
public class SettingsScreen extends BaseScreen {
    Table settingsLayout;
    Slider musicVolumeSlider,specialEffectsSlider;
    Label musicVolumeLabel, specialEffectsLabel;
    Skin skin;
    IconActor musicVolumeIcon,specialEffectsIcon;
    Stage stage;
    TextButton returnButton;
    TextureAtlas textureAtlas;

    public SettingsScreen(MainGame game) {
        super(game);
        //Initialize the stage
        stage = new Stage(new FitViewport(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT));
        //Initialize the atlas
        textureAtlas = new TextureAtlas(Gdx.files.internal("settingsatlas.atlas"));
        //Initialize the skin
        skin = new Skin(Gdx.files.internal("settings.json"),new TextureAtlas(Gdx.files.internal("settingsatlas.atlas")));
        //Initialize the sliders
        musicVolumeSlider = new Slider(0,1,(float)0.01,false,skin,"slider_style");
        specialEffectsSlider = new Slider(0,1,(float)0.01,false,skin,"slider_style");
        musicVolumeSlider.setValue(game.getMusicVolumeLevel());
        specialEffectsSlider.setValue(game.getSoundEffectsLevel());
        //Initialize the labels
        musicVolumeLabel = new Label("Music Volume",skin,"label_style");
        specialEffectsLabel = new Label("Sfx Volume",skin,"label_style");
        //Initialize the return button
        returnButton = new TextButton("Return",skin,"text_button_style");
        //Initialize the Icons

        musicVolumeIcon = new IconActor(textureAtlas.findRegion("musicicon"),Constantes.SETTINGS_ICON_WIDTH,Constantes.SETTINGS_ICON_HEIGHT);
        specialEffectsIcon = new IconActor(textureAtlas.findRegion("soundeffectsicon"),Constantes.SETTINGS_ICON_WIDTH, Constantes.SETTINGS_ICON_HEIGHT);
        settingsLayout = new Table();
    }

    @Override
    public void show() {
        //Configure the layout
        settingsLayout.setSize(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT);
        settingsLayout.align(Align.center);

        settingsLayout.add(specialEffectsSlider);
        settingsLayout.add(specialEffectsLabel).pad(Constantes.STANDARD_ICON_PADDING);
        settingsLayout.add(specialEffectsIcon).pad(Constantes.STANDARD_ICON_PADDING);
        settingsLayout.row().pad(Constantes.STANDARD_ICON_PADDING);
        settingsLayout.add(musicVolumeSlider);
        settingsLayout.add(musicVolumeLabel).pad(Constantes.STANDARD_ICON_PADDING);
        settingsLayout.add(musicVolumeIcon).pad(Constantes.STANDARD_ICON_PADDING);
        settingsLayout.row();
        settingsLayout.add(returnButton).colspan(3);
        //Start widgets
        startWidgets();

        //Add the layout to the stage
        stage.addActor(settingsLayout);
        //Set the input processor
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,1, (float) 0.3);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        renderVolumes();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /*We need a version of the Icon class that we can add to the table*/
    public class IconActor extends Actor{
        Icon icon;
        IconActor(TextureRegion textureRegion,float width,float height){
            icon = new Icon(textureRegion,width,height);
            setSize(width,height);
        }

        IconActor(Texture texture, float width,float height){
            icon = new Icon(texture,width,height);
            setSize(width,height);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(icon.getTextureRegion(),getX(),getY(),getWidth(),getHeight());
        }

    }
    public void startWidgets(){
        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    public void renderVolumes(){
        game.setMusicVolumeLevel(musicVolumeSlider.getValue());
        game.setSoundEffectsLevel(specialEffectsSlider.getValue());
        game.getManager().get("game_song.wav",Music.class).setVolume(game.getMusicVolumeLevel());
    }


}
