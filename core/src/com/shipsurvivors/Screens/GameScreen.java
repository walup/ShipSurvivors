package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.UI.ShipControls;

/**
 * Created by SEO on 25/09/2017.
 */

/*This will be the */
public class GameScreen extends BaseScreen {
    Stage stage;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;
    World world;
    Sound rotatingSound;
    Music gameMusic;
    ShipControls shipControls;




    public GameScreen(MainGame game) {
        super(game);
        //initialize the stage
        stage = new Stage();
        stage.setViewport(new FitViewport(640,360));
        //initialize the world
        world = new World(new Vector2(0,0),true);
        //initialize the camera
        camera = new OrthographicCamera();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera.setToOrtho(false,w,h);
        //Initialize the tiled map

        //Initialize the controls

        shipControls = new ShipControls();
        //Initialize the sounds and music


    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
