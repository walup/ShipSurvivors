package com.shipsurvivors.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

/**
 * Created by SEO on 03/04/2017.
 */
public class MainGame extends Game {
    public AssetManager manager;
    public float soundEffectsLevel = 0.5f;
    public float musicVolumeLevel=0.5f;

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }

}
