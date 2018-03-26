package com.shipsurvivors.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by SEO on 03/04/2017.
 */
public class MainGame extends Game {
    public AssetManager manager;
    private float soundEffectsLevel = 0.5f;
    private float musicVolumeLevel=0.5f;

    @Override
    public void create() {
        manager = new AssetManager();

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
