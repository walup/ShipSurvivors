package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 15/10/2017.
 */

/*This is the Screen where the story will be displayed*/

public class StoryScreen extends BaseScreen  {

    private ImageButton nextButton, previousButton, skipButton;
    private Table layout;
    private Label label;
    private int counter;
    private TextSnapshot[] textSnapshots;
    private Portrait portrait;
    private Stage stage;


    public StoryScreen(MainGame game) {
        super(game);
        counter = 0;

        FileHandle file = Gdx.files.internal("story.txt");
        String story = file.readString();
        /*This will split the story at every new line.*/
        String[] lines = story.split("\n");

        /*Fill the text snapshots array*/
        textSnapshots = new TextSnapshot[5];
        for (int i = 0;i<textSnapshots.length;i++){
            Texture texture = game.getManager().get("story_slide_"+(i+1));
            textSnapshots[i] = new TextSnapshot(texture,lines[i]);
        }

        /*This is a skin that we still have to create*/
        Skin skin = game.getManager().get("story_styles.json",Skin.class);

        /*Initialize the buttons*/
        nextButton = new ImageButton(skin,"next_button_style");
        previousButton = new ImageButton(skin,"previous_button_style");
        skipButton = new ImageButton(skin,"skip_button_style");
        configureButtons();

        /*Initialize the Frame*/
        portrait = new Portrait(textSnapshots[0].getSnapshot(), Constantes.PORTRAIT_WIDTH,Constantes.PORTRAIT_HEIGHT);
        /*Initialize the label*/
        label = new Label(textSnapshots[0].getLine(),skin,"label_style");
        label.setWrap(true);

        /*Initialize the stage*/
        stage = new Stage(new FitViewport(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT));
    }

    @Override
    public void show() {
        /*We set the layout*/
        layout = new Table();
        layout.setSize(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT);
        layout.align(Align.center);
        layout.add(portrait);
        layout.row();
        layout.add(label);
        layout.row();
        layout.add(previousButton);
        layout.add(skipButton);

        //Add the layout to the stage
        stage.addActor(layout);
        Gdx.input.setInputProcessor(stage);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,1, (float) 0.3);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    public void configureButtons(){
        //Configure the next button
        nextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                counter+=1;
                if(counter<textSnapshots.length) {
                    changeTextAndSnapshot(counter);
                }
                else{
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });

        //Configure the previous button

        previousButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                counter-=1;
                changeTextAndSnapshot(counter);
            }
        });

        //Configure the skip button

        skipButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }


    public void changeTextAndSnapshot(int counter){
        label.setText(textSnapshots[counter].getLine());
        portrait.setTexture(textSnapshots[counter].getSnapshot());
    }

    /*This is the text snap shot class, it basically stores one texture and one line of text corresponding to
    * such texture*/

    public class TextSnapshot{
        private String line;
        private Texture snapshot;

        public TextSnapshot(Texture snapshot, String line){
            snapshot.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
            this.snapshot = snapshot;
            this.line = line;
        }

        public Texture getSnapshot() {
            return snapshot;
        }

        public String getLine() {
            return line;
        }
    }

    public class Portrait extends Actor {
        private Texture texture;
        public Portrait(Texture texture,float width, float height){
            this. texture = texture;
            setSize(width,height);
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(texture,getX(),getY(),getWidth(),getHeight());
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
        }
    }
}
