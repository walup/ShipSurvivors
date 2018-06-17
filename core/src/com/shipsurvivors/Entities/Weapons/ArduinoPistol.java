package com.shipsurvivors.Entities.Weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.shipsurvivors.Entities.Weapon;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.SandBox.UserData;

/**
 * Created by SEO on 05/11/2017.
 */
public class ArduinoPistol extends Weapon {
    private Texture bulletTexture;
    private float bulletX,bulletY,bulletWidth,bulletHeight;
    private Body bulletBody;
    private Fixture bulletFixture;
    private boolean inRange;
    private boolean collided;

    public ArduinoPistol(TextureAtlas weaponAtlas,Texture cardTexture,Texture bulletTexture,World world) {
        super(weaponAtlas,cardTexture);

        /*Set the bullet texture*/
        this.bulletTexture = bulletTexture;

        /*Set the bullet body*/

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bulletBody = world.createBody(bodyDef);
        //Set the appropriate user data
        UserData userData = new UserData(UserData.BULLET);
        userData.setSplashRadius(0.4f);
        bulletBody.setUserData(userData);

        /*Set the bullet fixture*/
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(Constantes.BULLET_WIDTH/(2*Constantes.PIXELS_IN_METER),Constantes.BULLET_HEIGHT/(2*Constantes.PIXELS_IN_METER));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1;
        fixtureDef.filter.categoryBits =Constantes.CATEGORY_BULLET;
        fixtureDef.filter.maskBits = Constantes.CATEGORY_ROCK;
        bulletFixture = bulletBody.createFixture(fixtureDef);
        setAccesoryFixture(bulletFixture);

        bulletBody.setActive(false);

        //Set the size of the bullet
        bulletWidth = Constantes.BULLET_WIDTH;
        bulletHeight = Constantes.BULLET_HEIGHT;



    }

    /*The cool thing is now we only need to worry about what the bullet is going to do when it is realeased*/

    @Override
    public void startAccesory() {
        bulletBody.setActive(true);
        setBulletPosition(getX()+getWidth(),getY()+(getHeight()-bulletHeight)/2);
        bulletBody.setTransform(bulletX/Constantes.PIXELS_IN_METER,bulletY/Constantes.PIXELS_IN_METER,0);
        bulletBody.setLinearVelocity(Constantes.BULLET_VELOCITY/Constantes.PIXELS_IN_METER,0);
    }

    @Override
    public void drawAccesory(Batch batch, float parentAlpha) {

        if(bulletBody.isActive()){
            batch.draw(bulletTexture,bulletX,bulletY,bulletWidth,bulletHeight);
        }
    }

    @Override
    public void actAccesory(float delta) {

        setBulletPosition(bulletBody.getPosition().x*Constantes.PIXELS_IN_METER,bulletBody.getPosition().y*Constantes.PIXELS_IN_METER);
        setInRange(bulletX<Constantes.WORLD_WIDTH);



        if(!inRange){
            bulletBody.setActive(false);
            setShooting(false);
        }
    }

    public void setBulletPosition(float x, float y){
        bulletX = x;
        bulletY = y;
    }

    //This is the method where we shut down the accesories, in this case the bullet


    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public boolean isInRange() {
        return inRange;
    }

    @Override
    public void dispose() {
        super.dispose();
        bulletTexture.dispose();
    }
}
