package com.shipsurvivors.Screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import javafx.scene.control.Skin;

/**
 * Created by SEO on 25/09/2017.
 */
public class MainMenuScreen extends BaseScreen {
    Skin skin;
    TextButton startGameButton;
    TextButton settingsButton;
    TextButton sotryButton;
    TextureAtlas menuAtlas;
    Table menuLayout;
    Stage stage;


    public MainMenuScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
