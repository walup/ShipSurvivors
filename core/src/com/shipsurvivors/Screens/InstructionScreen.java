package com.shipsurvivors.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipsurvivors.Entities.Ship;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.CollisionGeometry;

public class InstructionScreen extends BaseScreen {
    /*We will display the background, and the ship. And we will then we will have some animations to show the player what we can do
    * the things that i'll cover is move up and down, attach card, turn the wheel and shoot,*/
    private Image background;
    private Ship ship;
    private Skin instructionsSkin;
    private AnimationActor arrowUpAnimation;
    private AnimationActor arrowDownAnimation;
    private AnimationActor turnAnimation;
    private int counter;
    private Stage stage;
    private World world;
    private MovableImage cardImage;
    private Image weaponImage;
    private Label instructionSquare;
    private String[] instructions;
    private MyTouchListener myTouchListener;

    public InstructionScreen(MainGame game) {
        super(game);
        //Initialize the instruction counter to zero (first instruction)
        counter = 0;
        //Get the background
        background =new Image(game.getManager().get("background.png",Texture.class));
        //The skin
        instructionsSkin = game.getManager().get("Instructions/instructions_styles.json");
        //We get the animations
        arrowUpAnimation = new AnimationActor(new Animation(1/50f, instructionsSkin.getAtlas().findRegions("arrow_up")),0,0,Constantes.INST_ARROW_UP_WIDTH,Constantes.INST_ARROW_UP_HEIGHT);
        arrowDownAnimation = new AnimationActor(new Animation<TextureRegion>(1/50f,instructionsSkin.getAtlas().findRegions("arrow_down")),0,0,Constantes.INST_ARROW_DOWN_WIDTH,Constantes.INST_ARROW_DOWN_HEIGHT);
        turnAnimation = new AnimationActor(new Animation<TextureRegion>(1/50f, instructionsSkin.getAtlas().findRegions("turning")),0,0,Constantes.INST_TURN_WIDTH,Constantes.INST_TURN_HEIGHT);
        //Load the card texture and the weapon texture
        cardImage = new MovableImage(game.getManager().get("Cards/arduino_pistol_card.png",Texture.class));
        weaponImage = new Image(game.getManager().get("Guns/arduinogunatlas.atlas",TextureAtlas.class).findRegions("shooting").get(0));
        cardImage.setSize(Constantes.CARD_WIDTH,Constantes.CARD_HEIGHT);
        weaponImage.setSize(Constantes.WEAPON_WIDTH,Constantes.WEAPON_HEIGHT);
        /*Set the instructions in a String array*/
        FileHandle file = Gdx.files.internal("Instructions/instructions.txt");
        String instruc = file.readString();
        instructions = instruc.split("\\*");

        /*Initialize the label*/
        instructionSquare = new Label("brr",instructionsSkin,"label_style");
        instructionSquare.setPosition(0,Constantes.WORLD_HEIGHT-50);

        /*Initialize the stage*/
        stage = new Stage(new FitViewport(Constantes.WORLD_WIDTH,Constantes.WORLD_HEIGHT));
        /*Initialize the world*/
        world = new World(new Vector2(0,0),true);
        /*Start the touch listener*/
        myTouchListener = new MyTouchListener();
    }


    @Override
    public void show() {
        //Initialize the ship
        ship = new Ship(world,game.getManager().get("shipatlas.atlas",TextureAtlas.class),200,85,Constantes.SHIP_WIDTH,Constantes.SHIP_HEIGHT);
        /*Vamos a agregar las cosas al Stage*/
        stage.addActor(background);
        stage.addActor(ship);
        stage.addActor(arrowUpAnimation);
        stage.addActor(arrowDownAnimation);
        stage.addActor(turnAnimation);
        stage.addActor(cardImage);
        stage.addActor(weaponImage);
        stage.addActor(instructionSquare);
        //Set the input proccessor
        Gdx.input.setInputProcessor(myTouchListener);
        //Set the first actors where they need to be.
        setActors(counter);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,1, (float) 0.3);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
    }

    public void setActors(int counter){
        switch(counter) {
            //First instruction
            case 0:
                /*Ok so in the first instructions we want to tell the player he can
                 * move the ship up so we're going to put the up */
                turnAnimation.setVisible(false);
                arrowDownAnimation.setVisible(false);
                cardImage.setVisible(false);
                weaponImage.setVisible(false);
                arrowUpAnimation.setVisible(true);
                /*Set the arrow where you want it*/
                arrowUpAnimation.setPosition(ship.getX() +(ship.getWidth()-arrowUpAnimation.getWidth())/2,ship.getY()+ship.getHeight());
                instructionSquare.setText(instructions[counter]);
                break;
                //Second instruction
            case 1:
                /*For the second instruction we want to tell the player he can move the ship down*/
                arrowUpAnimation.setVisible(false);
                arrowDownAnimation.setVisible(true);
                /*Set the arrow where you want it */
                arrowDownAnimation.setPosition(ship.getX() +(ship.getWidth()-arrowDownAnimation.getWidth())/2, ship.getY()-arrowDownAnimation.getHeight());
                instructionSquare.setText(instructions[counter]);
                break;

                //Third instruction
            case 2:
                /*For the third instruction we want to tell the player he can drag a card to a dock*/
                arrowDownAnimation.setVisible(false);
                cardImage.setVisible(true);
                cardImage.setTrajectory(new Vector2(Constantes.WORLD_WIDTH-Constantes.CARD_WIDTH,100),new Vector2(ship.getX()+(ship.getWidth()-cardImage.getWidth())/2,ship.getY()+ship.getHeight()));
                instructionSquare.setText(instructions[counter]);
                break;
            case 3:
                cardImage.setVisible(false);
                weaponImage.setVisible(true);
                arrowDownAnimation.setVisible(true);
                weaponImage.setPosition(cardImage.getFinalPos().x,cardImage.getFinalPos().y);
                arrowDownAnimation.setPosition(weaponImage.getX()+(weaponImage.getWidth()-arrowDownAnimation.getWidth())/2,weaponImage.getY()+weaponImage.getHeight());
                instructionSquare.setText(instructions[counter]);
                break;
            case 4:
                /*Finally if we get here we just display the last line.*/
                weaponImage.setVisible(false);
                arrowDownAnimation.setVisible(false);
                instructionSquare.setText(instructions[counter]);
                break;
            case 5:
                game.setScreen(new MainMenuScreen(game));
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
        stage.getCamera().update();
    }

    public class AnimationActor extends Actor {
        private Animation<TextureRegion> animation;
        private float elapsedTime = 0;
        public AnimationActor(Animation<TextureRegion> animation,float x, float y, float width, float height){
            this.animation = animation;
            //Apply a filter to the animations
            for (TextureRegion textureRegion:animation.getKeyFrames()){
                textureRegion.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
            setBounds(x,y,width,height);
        }

        @Override
        public void act(float delta) {
            if(isVisible()) {
                elapsedTime += delta;
                if (elapsedTime>animation.getAnimationDuration()){
                    elapsedTime = 0;
                }
            }
        }

        @Override
        public void draw(Batch batch, float a) {
            batch.draw(animation.getKeyFrame(elapsedTime,true),getX(),getY(),getWidth(),getHeight());
        }

        public Animation<TextureRegion> getAnimation() {
            return animation;
        }
    }


    //Este va a ser nuestro procesador de entrada, el cual basicamente cuando se toque la pantalla va a cambiar el contador.
    public class MyTouchListener implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            counter+=1;
            setActors(counter);
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    public class MovableImage extends Image{
        private boolean moving;
        private Vector2 initialPos = new Vector2(0,0);
        private Vector2 finalPos = new Vector2(0,0);
        private Vector2 velocity = new Vector2(0,0);
        private float tolerance = 0.01f;

        public MovableImage(Texture texture) {
            super(texture);
        }

        @Override
        public void act(float delta) {
            if(isVisible() && isMoving()){
                setPosition(getX()+velocity.x*delta,getY()+velocity.y*delta);
                if(CollisionGeometry.distanceBetween2Points(getX(),getY(),initialPos.x,initialPos.y)+CollisionGeometry.distanceBetween2Points(getX(),getY(),finalPos.x,finalPos.y)> CollisionGeometry.distanceBetween2Points(initialPos.x,initialPos.y,finalPos.x,finalPos.y)){
                    //Si excedemos de la positción final lo regresamos a la inicial.
                    setPosition(initialPos.x,initialPos.y);
                }
            }
        }

        public void setTrajectory(Vector2 initialPos, Vector2 finalPos){
            //We are going to make the trajectory linear
            this.initialPos = initialPos;
            this.finalPos = finalPos;
            velocity.x= ((finalPos.x-initialPos.x)/(CollisionGeometry.distanceBetween2Points(0,0,(finalPos.x-initialPos.x),(finalPos.y-initialPos.y))))*Constantes.VELOCITY_CARD;
            velocity.y= ((finalPos.y-initialPos.y)/CollisionGeometry.distanceBetween2Points(0,0,finalPos.x-initialPos.x,finalPos.y-initialPos.y))*Constantes.VELOCITY_CARD;
            setMoving(true);
            setPosition(initialPos.x,initialPos.y);
        }

        public boolean isMoving() {
            return moving;
        }

        public void setMoving(boolean moving) {
            this.moving = moving;
        }

        public Vector2 getFinalPos() {
            return finalPos;
        }
    }
}
