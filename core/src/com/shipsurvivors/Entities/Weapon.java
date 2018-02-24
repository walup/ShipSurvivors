package com.shipsurvivors.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.Icon;

/**
 * Created by SEO on 25/09/2017.
 */
public class Weapon extends Attachable {
    private Animation<TextureRegion> weaponShootingAnimation;
    private TextureRegion standingTexture;
    private float elapsedTime;

    public Weapon(TextureAtlas weaponAtlas, Texture cardTexture){
        //Set the animation
        weaponShootingAnimation = new Animation(1/50f,weaponAtlas.findRegions("shooting"));
        for (int i = 0;i<weaponShootingAnimation.getKeyFrames().length;i++){
            weaponShootingAnimation.getKeyFrames()[i].getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        //Set the card and standing texture
        setCard(new Icon(cardTexture,Constantes.CARD_WIDTH,Constantes.CARD_HEIGHT));
        setSize(Constantes.WEAPON_WIDTH,Constantes.WEAPON_HEIGHT);
        elapsedTime= 0;
        standingTexture = weaponShootingAnimation.getKeyFrame(0);
    }



    @Override
    public void drawAttachable(Batch batch, float parentAlpha) {

        /*Aqui distinguiremos tres casos cuando esta disparando, cuando no esta en ´posición de disparo y cuando si lo esta, en el primer caso corremos la animacion
        * en el segundo no vamos a dibujar nada y en el tercero vamos a dibujar solo el primer cuadro de la animacion (cuando aun no esta dis
        * parando)*/
        if(isActivated()) {
            batch.draw(weaponShootingAnimation.getKeyFrame(elapsedTime), getX(), getY(), getWidth(), getHeight());
            elapsedTime = elapsedTime + Gdx.graphics.getDeltaTime();
            if(elapsedTime>weaponShootingAnimation.getAnimationDuration()){
                elapsedTime = 0;
                setActivated(false);
                setShooting(true);
                startAccesory();
            }
        }
        else if(isShooting()){
            drawStandingTexture(batch,parentAlpha);
            drawAccesory(batch,parentAlpha);
        }
        else{
            batch.draw(standingTexture,getX(),getY(),getWidth(),getHeight());
        }
    }


    public void drawStandingTexture(Batch batch,float parentAlpha){
        batch.draw(standingTexture,getX(),getY(),getWidth(),getHeight());
    }

    public Animation<TextureRegion> getWeaponShootingAnimation() {
        return weaponShootingAnimation;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

}
