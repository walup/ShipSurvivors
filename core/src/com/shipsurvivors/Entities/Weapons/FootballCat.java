package com.shipsurvivors.Entities.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.shipsurvivors.Entities.Weapon;
import com.shipsurvivors.UI.ShipControls;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.CollisionGeometry;
import com.shipsurvivors.Utilities.SandBox.UserData;

/**
 * Created by SEO on 05/02/2018.
 */
public class FootballCat extends Weapon {

    private TextureRegion footballTexture;
    private Body playerBody;
    private Body footballBody;
    private boolean ballThrown,ballOutOfRange,ballCollided,footballTurnedOff;
    private float playerX,playerY,playerWidth,playerHeight;
    private float footballAngle;
    private float footballX,footballY,footballWidth,footballHeight;
    private float footballVelocityX, footballVelocityY;
    private boolean playerOutOfRange;
    private Rectangle rectangle;

    public FootballCat(TextureAtlas weaponAtlas, Texture cardTexture,Texture footballTexture,World world) {
        super(weaponAtlas, cardTexture);
        cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.footballTexture = new TextureRegion(footballTexture);
        setFootballSize(Constantes.FOOTBALL_WIDHT,Constantes.FOOTBALL_HEIGHT);
        setPlayerSize(Constantes.FOOTBALL_CAT_WIDTH,Constantes.FOOTBALL_CAT_HEIGHT);

        //Initialize the bodie and fixture of the player
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(playerBodyDef);
        UserData playerUserData = new UserData(UserData.BULLET);
        playerUserData.setSplashRadius(0.1f);
        playerBody.setUserData(playerUserData);
        PolygonShape footballCatShape = new PolygonShape();
        footballCatShape.setAsBox(getPlayerWidth()/(2*Constantes.PIXELS_IN_METER),getPlayerHeight()/(2*Constantes.PIXELS_IN_METER));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = footballCatShape;
        fixtureDef.filter.categoryBits = Constantes.CATEGORY_BULLET;
        fixtureDef.filter.maskBits = Constantes.CATEGORY_ROCK;
        setAccesoryFixture(playerBody.createFixture(fixtureDef));

        //Initialize the bodie and fixture of the football
        BodyDef footballBodyDef = new BodyDef();
        footballBodyDef.type = BodyDef.BodyType.DynamicBody;
        footballBody = world.createBody(footballBodyDef);
        UserData footballUserData = new UserData(UserData.BULLET);
        footballUserData.setSplashRadius(0.4f);
        footballBody.setUserData(footballUserData);
        PolygonShape footballShape = new PolygonShape();
        footballShape.setAsBox(getFootballWidth()/(2*Constantes.PIXELS_IN_METER),getFootballHeight()/(2*Constantes.PIXELS_IN_METER));
        FixtureDef footballFixtureDef = new FixtureDef();
        footballFixtureDef.shape = footballShape;
        footballFixtureDef.filter.categoryBits = Constantes.CATEGORY_BULLET;
        footballFixtureDef.filter.maskBits = Constantes.CATEGORY_ROCK;
        footballBody.createFixture(fixtureDef);
        footballBody.setActive(false);

        setFootballTurnedOff(false);
        rectangle = new Rectangle();
        rectangle.setSize(playerWidth,playerHeight);
    }

    @Override
    public void startAccesory() {
        setPlayerPosition(getX(),getY());
        setFootballPosition(getPlayerX()+getPlayerWidth(),getPlayerY() +(getPlayerHeight()-getFootballWidth())/2);
        playerBody.setTransform(getPlayerX()/Constantes.PIXELS_IN_METER,getPlayerY()/Constantes.PIXELS_IN_METER,0);
        footballBody.setTransform(getFootballX()/Constantes.PIXELS_IN_METER,getFootballY()/Constantes.PIXELS_IN_METER,0);
        playerBody.setActive(true);
        playerBody.setLinearVelocity(Constantes.FOOTBALL_CAT_VELOCITY/Constantes.PIXELS_IN_METER,0);
        setBallThrown(false);
    }



    @Override
    public void actAccesory(float delta) {
        //update the player position
        updatePlayerPosition();
        flingCheck();
        updateFootballPosition();

    }

    @Override
    public void drawStandingTexture(Batch batch, float parentAlpha) {
        batch.draw(getWeaponShootingAnimation().getKeyFrame(getElapsedTime()),getPlayerX(),getPlayerY(),getPlayerWidth(),getPlayerHeight());
        setElapsedTime((getElapsedTime()+ Gdx.graphics.getDeltaTime())%getWeaponShootingAnimation().getAnimationDuration());
    }

    @Override
    public void drawAccesory(Batch batch, float parentAlpha) {
        if(footballBody.isActive()){
            batch.draw(footballTexture,footballX,footballY,footballWidth/2,footballHeight/2,footballWidth,footballHeight,1,1, (float) Math.toDegrees(footballAngle));
        }
    }

    public void updatePlayerPosition(){
        setPlayerPosition(playerBody.getPosition().x*Constantes.PIXELS_IN_METER,playerBody.getPosition().y*Constantes.PIXELS_IN_METER);
        rectangle.setPosition(playerX,playerY);
        setPlayerOutOfRange(getPlayerX()>=Constantes.SCREEN_WIDTH);
        if(isPlayerOutOfRange()){
            playerBody.setActive(false);
            footballBody.setActive(false);
            setShooting(false);
        }
    }

    public void updateFootballPosition(){
        if(footballBody.isActive()){
            setFootballPosition(footballBody.getPosition().x*Constantes.PIXELS_IN_METER,footballBody.getPosition().y*Constantes.PIXELS_IN_METER);
            setBallOutOfRange(CollisionGeometry.distanceBetween2Points(playerX,playerY,footballX,footballY)>Constantes.SCREEN_WIDTH);
           // setBallCollided(footballBody.getLinearVelocity().epsilonEquals(footballVelocityX/Constantes.PIXELS_IN_METER,footballVelocityY/Constantes.PIXELS_IN_METER));
            System.out.println(CollisionGeometry.distanceBetween2Points(playerX,playerY,footballX,footballY));
            if(isBallOutOfRange()||ballCollided){
                footballBody.setActive(false);
                setFootballTurnedOff(true);
            }
        }
        else{
            setFootballPosition(getPlayerX()+getPlayerWidth(),getPlayerY() +(getPlayerHeight()-getFootballWidth())/2);
            footballBody.setTransform(getFootballX()/Constantes.PIXELS_IN_METER,getFootballY()/Constantes.PIXELS_IN_METER,0);
            if(isBallThrown() && !isFootballTurnedOff()){
                System.out.println(footballVelocityX);
                System.out.println(footballVelocityY);
                footballBody.setActive(true);
                footballBody.setLinearVelocity(footballVelocityX/Constantes.PIXELS_IN_METER,footballVelocityY/Constantes.PIXELS_IN_METER);
                footballBody.setTransform(footballBody.getPosition().x,footballBody.getPosition().y,footballAngle);
            }
        }
    }

    public boolean isBallThrown() {
        return ballThrown;
    }

    public void setBallThrown(boolean ballThrown) {
        this.ballThrown = ballThrown;
    }

    public void setPlayerPosition(float x, float y ){
        playerX = x;
        playerY = y;
    }

    public void setFootballPosition(float x, float y ){
        footballX = x;
        footballY = y;
    }

    public void setPlayerSize(float width,float height){
        playerWidth = width;
        playerHeight = height;
    }

    public void setFootballSize(float width,float height){
        footballWidth = width;
        footballHeight = height;
    }

    public void setFootballAngle(float footballAngle) {
        this.footballAngle = footballAngle;
    }

    public void setFootballVelocityX(float footballVelocityX) {
        this.footballVelocityX = footballVelocityX;
    }

    public void setFootballVelocityY(float footballVelocityY) {
        this.footballVelocityY = footballVelocityY;
    }

    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public float getFootballX() {
        return footballX;
    }

    public float getFootballY() {
        return footballY;
    }

    public float getPlayerWidth() {
        return playerWidth;
    }

    public float getPlayerHeight() {
        return playerHeight;
    }

    public float getFootballHeight() {
        return footballHeight;
    }

    public float getFootballWidth() {
        return footballWidth;
    }


    public void throwFootball(float velocityX, float velocityY,float angle){
        setFootballVelocityX(velocityX);
        setFootballVelocityY(velocityY);
        setFootballAngle(angle);
    }

    public boolean isBallCollided() {
        return ballCollided;
    }

    public void setBallCollided(boolean ballCollided) {
        this.ballCollided = ballCollided;
    }

    public boolean isBallOutOfRange() {
        return ballOutOfRange;
    }

    public void setBallOutOfRange(boolean ballOutOfRange) {
        this.ballOutOfRange = ballOutOfRange;
    }

    public void setFootballTurnedOff(boolean footballTurnedOff) {
        this.footballTurnedOff = footballTurnedOff;
    }

    public boolean isFootballTurnedOff() {
        return footballTurnedOff;
    }

    public void setPlayerOutOfRange(boolean playerOutOfRange) {
        this.playerOutOfRange = playerOutOfRange;
    }

    public boolean isPlayerOutOfRange() {
        return playerOutOfRange;
    }

    public void flingCheck() {
        if (ShipControls.getFlingIntent() && rectangle.contains(ShipControls.getMouseStagePosition()) && isShooting()){
            if (!footballBody.isActive() && !isFootballTurnedOff()) {
                setBallThrown(true);
                throwFootball(ShipControls.getFlingVelocityX(), -ShipControls.getFlingVelocityY(), (float) Math.atan2(ShipControls.getFlingVelocityY(), ShipControls.getFlingVelocityX()));
            }
        }
    }

    @Override
    public void detach() {
        setFootballTurnedOff(false);
        footballBody.setActive(false);
        playerBody.setActive(false);
    }
}
