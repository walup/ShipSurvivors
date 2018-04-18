package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Utilities.Constantes;

import java.io.IOException;
import java.io.Writer;

import jdk.nashorn.internal.ir.debug.JSONWriter;

/**
 * Created by SEO on 25/09/2017.
 */
public class GameOverScreen extends BaseScreen{
    private float score;
    private String name;
    private Label gameOverLabel, highScoreLabel;
    private Preferences prefs;
    private Stage stage;
    private Boolean isChanging;
    private Table layout;


    public GameOverScreen(MainGame game,float score) {
        super(game);
        //Initialize the stage
        stage = new Stage(new FitViewport(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT));
        this.score = score;
        isChanging = false;

    }

    @Override
    public void show() {
        prefs = Gdx.app.getPreferences("My Preferences");
        float highscore = prefs.getFloat("highscore",0);
        name  = prefs.getString("name","2pac");
        Skin skin = game.getManager().get("settings.json");
        //Initiate the labels
        gameOverLabel = new Label("Game Over",skin,"label_style2");
        highScoreLabel = new Label("dummy text",skin,"label_style2");

        //Add the labels to the table
        layout = new Table();
        layout.setSize(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT);
        layout.align(Align.center);
        layout.add(gameOverLabel);
        layout.row();
        layout.add(highScoreLabel).padTop(100);

        //If the score is bigger than the highscore substitute it
        if(score>highscore){
            /*Get the name of the player*/
            NameInputListener inputListener = new NameInputListener();
            Gdx.input.getTextInput(inputListener,"New highscore name!","","Just enter the damn name");
            isChanging = true;
        }
        else{
            //If the new score isn't higher get the last ones.
            name  = prefs.getString("name","2pac");
            score = highscore;
            highScoreLabel.setText("Highscore "+name +":   "+score);
        }

        stage.addActor(layout);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,1, (float) 0.3);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        if(!isChanging) {
            System.out.println("actt");
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
        stage.getCamera().update();
    }

    public class NameInputListener implements Input.TextInputListener{

        @Override
        public void input(String text) {
            name = text;
            prefs.putFloat("highscore",score);
            prefs.putString("name",name);
            prefs.flush();
            isChanging = false;
            highScoreLabel.setText("Highscore "+name +":   "+score);

        }

        @Override
        public void canceled() {

        }
    }
}
