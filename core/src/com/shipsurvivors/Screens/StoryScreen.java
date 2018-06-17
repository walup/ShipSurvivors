package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
    private LinesTable linesTable;
    private int counter;
    private TextSnapshot[] textSnapshots;
    private Portrait portrait;
    private Stage stage;
    private ScrollPane textScroller;
    private Skin skin;
    private float scrollingVelocity;

    public StoryScreen(MainGame game) {
        super(game);

        counter = 0;

        FileHandle file = Gdx.files.internal("story.txt");
        String story = file.readString();
        /*This will split the story at every new line.*/
        String[] lines = story.split("\\*");

        /*Fill the text snapshots array*/
        textSnapshots = new TextSnapshot[4];
        for (int i = 0;i<textSnapshots.length;i++){
            Texture texture = game.getManager().get("StorySlides/story_slide_"+(i+1)+".png");
            textSnapshots[i] = new TextSnapshot(texture,lines[i]);
        }

        /*This is a skin that we still have to create*/
        skin = game.getManager().get("story_styles.json",Skin.class);
        /*Apply a linear filter to the textures of the atlas*/
        for(int i = 0;i<skin.getAtlas().getRegions().size;i++){
            skin.getAtlas().getRegions().get(i).getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        /*Initialize the buttons*/
        nextButton = new ImageButton(skin,"next_button_style");
        previousButton = new ImageButton(skin,"previous_button_style");
        skipButton = new ImageButton(skin,"skip_button_style");
        configureButtons();

        /*Initialize the Frame*/
        portrait = new Portrait(textSnapshots[0].getSnapshot(), Constantes.PORTRAIT_WIDTH,Constantes.PORTRAIT_HEIGHT);
        /*Initialize the label*/

        /*Make the lines table*/

        linesTable = new LinesTable(textSnapshots[0].getLine(),Constantes.TEXT_FRAME_WIDTH,Constantes.TEXT_FRAME_HEIGHT);

        /*Initialize the scroller*/
        textScroller = new ScrollPane(linesTable,skin,"scroller_style");

        /*Initialize the stage*/
        stage = new Stage(new FitViewport(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT));

        /*The scrolling velocity */
        scrollingVelocity = 0.5f;


    }

    @Override
    public void show() {
        /*We set the layout*/
        layout = new Table();
        layout.setPosition(0,0);
        layout.setSize(Constantes.SCREEN_WIDTH,Constantes.SCREEN_HEIGHT);
        layout.align(Align.center);
        layout.add(portrait).colspan(3);
        layout.row();
        layout.add(textScroller).size(Constantes.TEXT_FRAME_WIDTH,Constantes.TEXT_FRAME_HEIGHT).colspan(3);
        layout.row();
        layout.add(previousButton).size(Constantes.BUTTON_WIDTH,Constantes.BUTTON_HEIGHT);
        layout.add(skipButton).size(Constantes.BUTTON_WIDTH, Constantes.BUTTON_HEIGHT);
        layout.add(nextButton).size(Constantes.BUTTON_WIDTH,Constantes.BUTTON_HEIGHT);
        layout.validate();
        //Add the layout to the stage
        stage.addActor(layout);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(31f/255f,31f/255f,20f/255f,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateScroller();
        stage.act();
        stage.draw();
    }

    //Thre resize method


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
        stage.getCamera().update();
    }

    public void configureButtons(){
        /*Set a linear filter to the textures of the buttons, so they look nice*/
        //Configure the next button
        nextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                counter+=1;
                if(counter<textSnapshots.length) {
                    changeTextAndSnapshot(counter);
                    textScroller.setScrollY(0);
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
                if(counter!=0) {
                    counter -= 1;
                    changeTextAndSnapshot(counter);
                    textScroller.setScrollY(0);
                }
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
        portrait.setTexture(textSnapshots[counter].getSnapshot());
        linesTable.changeText(textSnapshots[counter].getLine());
    }

    public void updateScroller(){
            textScroller.setScrollY(textScroller.getScrollY()+scrollingVelocity);
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

        public void dispose(){
            snapshot.dispose();
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

        public void dispose(){
            texture.dispose();
        }
    }

    public class LinesTable extends Table{
        private float width;
        private float height;

        public LinesTable(String text,float width, float height){
            this.height = height;
            this.width = width;

            setSize(width,height);
            align(Align.center);
            String[] lines = text.split("\n");
            for (int i = 0;i<lines.length;i++){
                Label label = new Label(lines[i],skin,"label_style");
                label.setWrap(true);
                add(label).size(width,height).padBottom(10).padTop(10);
                row();
            }
        }
        public void changeText(String newText){
            //Erase the previous labels
            reset();

            setSize(width,height);
            align(Align.center);
            //Now fill them with the new ones
            String[] lines = newText.split("\n");
            for(int i = 0;i<lines.length;i++){
                Label label = new Label(lines[i],skin,"label_style");
                label.setWrap(true);
                add(label).size(width,height).padBottom(10).padTop(10);
                row();
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();

        for(int i = 0;i<textSnapshots.length;i++){
            textSnapshots[i].dispose();
        }
        portrait.dispose();


    }
}
