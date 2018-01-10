package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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


/**
 * Created by SEO on 25/09/2017.
 */
public class MainMenuScreen extends BaseScreen {
    Skin skin;
    TextButton startGameButton;
    TextButton settingsButton;
    TextButton storyButton;
    Table menuLayout;
    Stage stage;


    public MainMenuScreen(MainGame game) {
        super(game);

        //Set the stage
        stage = new Stage(new FitViewport(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT));
        //Set the Skin
        skin = new Skin(Gdx.files.internal("menu_styles.json"),new TextureAtlas(Gdx.files.internal("uistuff.atlas")));
        //Initialize the buttons
        startGameButton = new TextButton("Start",skin,"menu_text_button_style");
        settingsButton = new TextButton("Settings",skin,"menu_text_button_style");
        storyButton = new TextButton("Story",skin,"menu_text_button_style");
        //Initilalize the table
        menuLayout = new Table();
        menuLayout.setSize(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT);
        menuLayout.align(Align.center);


    }

    @Override
    public void show() {
        //Now put everything where it is supposed to go on the table
        menuLayout.add(startGameButton).padTop(Constantes.STANDARD_BUTTON_PADDING).padBottom(Constantes.STANDARD_BUTTON_PADDING);
        menuLayout.row();
        menuLayout.add(settingsButton).padTop(Constantes.STANDARD_BUTTON_PADDING).padBottom(Constantes.STANDARD_BUTTON_PADDING);
        menuLayout.row();
        menuLayout.add(storyButton).padTop(Constantes.STANDARD_BUTTON_PADDING).padBottom(Constantes.STANDARD_BUTTON_PADDING);
        //Add the layout to the stage
        stage.addActor(menuLayout);
        //Activate the buttons
        activateButtons();
        //Set the input proccesor
        Gdx.input.setInputProcessor(stage);
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
    }

    private void activateButtons(){
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
                super.clicked(event, x, y);
            }
        });
    }
}
