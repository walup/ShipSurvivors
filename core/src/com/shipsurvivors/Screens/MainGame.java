package com.shipsurvivors.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 03/04/2017.
 */
public class MainGame extends Game {
    public AssetManager manager;
    private float soundEffectsLevel;
    private float musicVolumeLevel;

    @Override
    public void create() {
        manager = new AssetManager();

        //Get the music and effects volume
        Preferences prefs = Gdx.app.getPreferences(Constantes.PREFERENCES_KEY);
        soundEffectsLevel = prefs.getFloat(Constantes.SPECIAL_EFFECTS_KEY,0.5f);
        musicVolumeLevel = prefs.getFloat(Constantes.MUSIC_VOLUME_KEY,0.5f);
        //Load the menu Aassets
        manager.load("game_song.wav",Music.class);
        manager.load("uistuff.atlas",TextureAtlas.class);
        manager.load("menu_styles.json", Skin.class,new SkinLoader.SkinParameter("uistuff.atlas"));
        manager.load("green_flame",ParticleEffect.class,new ParticleEffectLoader.ParticleEffectParameter());
        manager.load("red_flame",ParticleEffect.class,new ParticleEffectLoader.ParticleEffectParameter());

        //Load the settings assets
        manager.load("settingsatlas.atlas",TextureAtlas.class);
        manager.load("settings.json",Skin.class,new SkinLoader.SkinParameter("settingsatlas.atlas"));

        //Load the game assets
        manager.load("background.png",Texture.class);
        manager.load("shipatlas.atlas",TextureAtlas.class);

        manager.load("Guns/arduino_bullet.png",Texture.class);
        manager.load("Guns/arduinogunatlas.atlas",TextureAtlas.class);
        manager.load("Cards/arduino_pistol_card.png",Texture.class);
        manager.load("card_container_background.png",Texture.class);
        manager.load("rock_pattern.png",Texture.class);
        manager.load("cowboy_cat.png",Texture.class);
        manager.load("hearts.atlas",TextureAtlas.class);
        manager.load("Guns/football_cat.atlas",TextureAtlas.class);
        manager.load("Guns/football.png",Texture.class);
        manager.load("Cards/football_card.png",Texture.class);
        manager.load("Cards/halfheart_card.png",Texture.class);
        manager.load("Guns/halfheart_left.png",Texture.class);
        manager.load("Guns/halfheart_right.png",Texture.class);
        manager.load("Guns/hertbreaker.atlas",TextureAtlas.class);
        manager.load("blueprint_ship.jpg",Texture.class);
        manager.load("song2.wav",Music.class);
        manager.load("story_styles.json", Skin.class,new SkinLoader.SkinParameter("story_styles.atlas"));
        //Load the story slides
        manager.load("StorySlides/story_slide_1.png",Texture.class);
        manager.load("StorySlides/story_slide_2.png",Texture.class);
        manager.load("StorySlides/story_slide_3.png",Texture.class);
        manager.load("StorySlides/story_slide_4.png",Texture.class);
        manager.load("game_styles.json",Skin.class,new SkinLoader.SkinParameter("game_styles.atlas"));
        manager.load("Instructions/instructions_styles.json",Skin.class,new SkinLoader.SkinParameter("Instructions/instructions.atlas"));
        manager.finishLoading();


        setScreen(new GameScreen(this));

    }

    public void setSoundEffectsLevel(float soundEffectsLevel) {
        this.soundEffectsLevel = soundEffectsLevel;
    }

    public void setMusicVolumeLevel(float musicVolumeLevel) {
        this.musicVolumeLevel = musicVolumeLevel;
    }

    public float getSoundEffectsLevel() {
        return soundEffectsLevel;
    }

    public float getMusicVolumeLevel() {
        return musicVolumeLevel;
    }

    public AssetManager getManager() {
        return manager;
    }
}
