package com.shipsurvivors.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

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
        //Load the game song
        manager.load("game_song.wav",Music.class);

        manager.finishLoading();
        setScreen(new MainMenuScreen(this));
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
