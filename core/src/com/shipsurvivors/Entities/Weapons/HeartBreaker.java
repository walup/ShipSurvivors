package com.shipsurvivors.Entities.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.shipsurvivors.Entities.Weapon;
import com.shipsurvivors.UI.ShipControls;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.CollisionGeometry;
import com.shipsurvivors.Utilities.SandBox.UserData;

/**
 * Created by SEO on 25/03/2018.
 */
public class HeartBreaker extends Weapon {
    /*Implementation for the HeartBreaker cat. besically when you touch it it will launch two half pieces of heart.
     * taking as the middle line the direction in which you threw it i can't figure out other way to implement it
     * than to use a shit ton of variables.*/

    private Rectangle touchRectangle;
    private float catX, catY, catVelocityX, catVelocityY;
    private float leftHeartX, leftHeartY, leftHeartVelocityX, leftHeartVelocityY;
    private float rightHeartX, rightHeartY, rightHeartVelocityX, rightHeartVelocityY;
    private float halfHeartHeight,halfHeartWidth;
    private Body rightHeartBody,leftHeartBody;
    private Fixture rightHeartFixture, leftHeartFixture;
    private Texture halfHeartLeftTexture, halfHeartRightTexture;
    private Boolean halfHeartsDeployed;

    public HeartBreaker(TextureAtlas weaponAtlas, Texture cardTexture,Texture halfHeartLeftTexture, Texture halfHeartRightTexture,World world) {
        super(weaponAtlas, cardTexture);
        cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //Store the textures for the half hearts
        this.halfHeartLeftTexture = halfHeartLeftTexture;
        this.halfHeartRightTexture = halfHeartRightTexture;
        this.halfHeartLeftTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.halfHeartRightTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        //Create the bodies of the half hearts
        createBulletBodies(world);

        System.out.println(rightHeartBody == null);
        //Set the cat size
        setSize(Constantes.HEARTBREAKER_WIDTH,Constantes.HEARTBREAKER_HEIGHT);

        //Set the size of the half hearts
        setHalfHeartSize(Constantes.HALFHEART_WIDTH,Constantes.HALFHEART_HEIGHT);

        //Start the rectangle
        touchRectangle = new Rectangle();
        touchRectangle.setSize(getWidth(),getHeight());

    }

    /*How we are going to position the stuff when we start shooting*/

    @Override
    public void startAccesory() {
        //Start the position of the heartbreaker where the card is
        setCatPosition(getX(),getY());

        //Start the positions of the half hearts
        setRightHeartPosition(getCatX()+getWidth(),getCatY()+getWidth()/2 -getHalfHeartHeight());
        setLeftHeartPosition(getCatX()+getWidth(),getCatY()+getWidth()/2);

        //Set the booleans to their initial conditions
        setHalfHeartsDeployed(false);
    }

    @Override
    public void actAccesory(float delta) {
        updateCatPosition(delta);
        flingCheck();
        updateHeartsPositions();

    }



    public void updateCatPosition(float delta){
        /*Move the cat a little to the left*/
        setCatPosition(getCatX()+delta*Constantes.HEARTBREAKER_VELOCITY,getCatY());
        touchRectangle.setPosition(getCatX(),getCatY());

        /*Check that it is within bounds if not set shooting to false
        * that itself will tell the dock to call the detach method*/
        if(isCatOutOfBounds()){
            setShooting(false);
        }
    }

    public void flingCheck(){
        if(!getHalfHeartsDeployed()&& ShipControls.getFlingIntent() && touchRectangle.contains(ShipControls.getMouseStagePosition()) &&isShooting()){
            setHalfHeartsDeployed(true);
            throwHalfHearts(ShipControls.getFlingVelocityX(),ShipControls.getFlingVelocityY());
            System.out.println("yay");

        }
    }

    public void updateHeartsPositions(){
        if(!getHalfHeartsDeployed()){
            setRightHeartPosition(getCatX()+getWidth(),getCatY()+getWidth()/2 -getHalfHeartHeight());
            setLeftHeartPosition(getCatX()+getWidth(),getCatY()+getWidth()/2);

        }
        else if(getHalfHeartsDeployed()){
            setRightHeartPosition(rightHeartBody.getPosition().x*Constantes.PIXELS_IN_METER,rightHeartBody.getPosition().y*Constantes.PIXELS_IN_METER);
            setLeftHeartPosition(leftHeartBody.getPosition().x*Constantes.PIXELS_IN_METER,leftHeartBody.getPosition().y*Constantes.PIXELS_IN_METER);
        }


    }

    /*How to draw the cat*/
    @Override
    public void drawStandingTexture(Batch batch, float parentAlpha) {
        batch.draw(getWeaponShootingAnimation().getKeyFrame(getElapsedTime()),getCatX(),getCatY(),getWidth(),getHeight());
        setElapsedTime((getElapsedTime()+ Gdx.graphics.getDeltaTime())%getWeaponShootingAnimation().getAnimationDuration());
    }

    @Override
    public void drawAccesory(Batch batch, float parentAlpha) {
        if(leftHeartBody.isActive()){
            //Draw the right heart
            batch.draw(halfHeartLeftTexture,getLeftHeartX(),getLeftHeartY(),getHalfHeartWidth(),getHalfHeartHeight());
        }
        if(rightHeartBody.isActive()){
            batch.draw(halfHeartRightTexture,getRightHeartX(),getRightHeartY(),getHalfHeartWidth(),getHalfHeartHeight());
        }
    }



    //Getters and setters and other helping methods.

    public void throwHalfHearts(float velocityX, float velocityY){
        float norm = CollisionGeometry.distanceBetween2Points(0,0,velocityX,velocityY);
        float angle = (float) Math.toRadians(Constantes.SPLIT_HEART_ANGLE);
        setLeftHeartVelocityX((float) ((velocityX/norm)*Constantes.HALF_HEART_VEL + Constantes.HALF_HEART_VEL*Math.cos(angle)));
        setLeftHeartVelocityY((float)((velocityY/norm)*Constantes.HALF_HEART_VEL +Constantes.HALF_HEART_VEL*Math.sin(angle)));
        setRightHeartVelocityX((float) ((velocityX/norm)*Constantes.HALF_HEART_VEL +Constantes.HALF_HEART_VEL*Math.cos(angle)));
        setRightHeartVelocityY((float) ((velocityY/norm)*Constantes.HALF_HEART_VEL -Constantes.HALF_HEART_VEL*Math.sin(angle)));
        leftHeartBody.setActive(true);
        leftHeartBody.setTransform(getLeftHeartX()/Constantes.PIXELS_IN_METER,getLeftHeartY()/Constantes.PIXELS_IN_METER,0);
        leftHeartBody.setLinearVelocity(getLeftHeartVelocityX()/Constantes.PIXELS_IN_METER,getLeftHeartVelocityY()/Constantes.PIXELS_IN_METER);
        rightHeartBody.setActive(true);
        rightHeartBody.setTransform(getRightHeartX()/Constantes.PIXELS_IN_METER,getRightHeartY()/Constantes.PIXELS_IN_METER,0);
        rightHeartBody.setLinearVelocity(getRightHeartVelocityX()/Constantes.PIXELS_IN_METER,getRightHeartVelocityY()/Constantes.PIXELS_IN_METER);




    }


    @Override
    public void detach() {
        leftHeartBody.setActive(false);
        rightHeartBody.setActive(false);
    }

    public void setCatX(float catX) {
        this.catX = catX;
    }


    public void setCatY(float catY) {
        this.catY = catY;
    }

    public void setCatPosition(float catX, float catY){
        setCatX(catX);
        setCatY(catY);

    }

    public float getCatX() {
        return catX;
    }

    public float getCatY() {
        return catY;
    }

    public float getLeftHeartY() {
        return leftHeartY;
    }

    public void setLeftHeartY(float leftHeartY) {
        this.leftHeartY = leftHeartY;
    }

    public void setLeftHeartX(float leftHeartX) {
        this.leftHeartX = leftHeartX;
    }

    public void setLeftHeartPosition(float leftHeartX, float leftHeartY){
        setLeftHeartX(leftHeartX);
        setLeftHeartY(leftHeartY);
    }

    public float getLeftHeartX() {
        return leftHeartX;
    }

    public float getRightHeartX() {
        return rightHeartX;
    }

    public void setRightHeartX(float rightHeartX) {
        this.rightHeartX = rightHeartX;
    }

    public void setRightHeartY(float rightHeartY) {
        this.rightHeartY = rightHeartY;
    }

    public void setRightHeartPosition(float rightHeartX,float rightHeartY){
        setRightHeartX(rightHeartX);
        setRightHeartY(rightHeartY);
    }

    public float getRightHeartY() {
        return rightHeartY;
    }

    public void setHalfHeartHeight(float halfHeartHeight) {
        this.halfHeartHeight = halfHeartHeight;
    }

    public void setHalfHeartWidth(float halfHeartWidth) {
        this.halfHeartWidth = halfHeartWidth;
    }

    public float getHalfHeartHeight() {
        return halfHeartHeight;
    }

    public float getHalfHeartWidth() {
        return halfHeartWidth;
    }

    public void setHalfHeartSize(float halfHeartWidth, float halfHeartHeight){
        setHalfHeartHeight(halfHeartHeight);
        setHalfHeartWidth(halfHeartWidth);
    }

    public Boolean getHalfHeartsDeployed() {
        return halfHeartsDeployed;
    }

    public void setHalfHeartsDeployed(Boolean halfHeartsDeployed) {
        this.halfHeartsDeployed = halfHeartsDeployed;
    }


    public boolean isCatOutOfBounds(){
        return getCatX()>Constantes.SCREEN_WIDTH;
    }


    public void setLeftHeartVelocityX(float leftHeartVelocityX) {
        this.leftHeartVelocityX = leftHeartVelocityX;
    }

    public float getLeftHeartVelocityX() {
        return leftHeartVelocityX;
    }

    public void setLeftHeartVelocityY(float leftHeartVelocityY) {
        this.leftHeartVelocityY = leftHeartVelocityY;
    }

    public float getLeftHeartVelocityY() {
        return leftHeartVelocityY;
    }

    public void setRightHeartVelocityX(float rightHeartVelocityX) {
        this.rightHeartVelocityX = rightHeartVelocityX;
    }

    public void setRightHeartVelocityY(float rightHeartVelocityY) {
        this.rightHeartVelocityY = rightHeartVelocityY;
    }

    public Body getLeftHeartBody() {
        return leftHeartBody;
    }

    public Body getRightHeartBody() {
        return rightHeartBody;
    }

    public float getRightHeartVelocityX() {
        return rightHeartVelocityX;
    }

    public float getRightHeartVelocityY() {
        return rightHeartVelocityY;
    }

    public void createBulletBodies(World world){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        leftHeartBody = world.createBody(bodyDef);
        UserData userData = new UserData(UserData.BULLET);
        userData.setSplashRadius(Constantes.HALFHEART_SPLASH_RADIUS);
        leftHeartBody.setUserData(userData);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constantes.HALFHEART_WIDTH/(2* Constantes.PIXELS_IN_METER),Constantes.HALFHEART_HEIGHT/(2*Constantes.PIXELS_IN_METER));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constantes.CATEGORY_BULLET;
        fixtureDef.filter.maskBits = Constantes.CATEGORY_ROCK;
        leftHeartFixture = leftHeartBody.createFixture(fixtureDef);
        leftHeartBody.setActive(false);

        BodyDef bodyDef2 = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        rightHeartBody = world.createBody(bodyDef);
        UserData userData2 = new UserData(UserData.BULLET);
        userData2.setSplashRadius(Constantes.HALFHEART_SPLASH_RADIUS);
        rightHeartBody.setUserData(userData2);
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(Constantes.HALFHEART_WIDTH/(2* Constantes.PIXELS_IN_METER),Constantes.HALFHEART_HEIGHT/(2*Constantes.PIXELS_IN_METER));
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = shape;
        fixtureDef2.filter.categoryBits = Constantes.CATEGORY_BULLET;
        fixtureDef2.filter.maskBits = Constantes.CATEGORY_ROCK;
        rightHeartFixture = rightHeartBody.createFixture(fixtureDef2);
        rightHeartBody.setActive(false);
    }
    public boolean halfHeartsActive(){
        return leftHeartBody.isActive() && rightHeartBody.isActive();
    }





}
