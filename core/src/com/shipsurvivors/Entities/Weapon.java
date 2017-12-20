package com.shipsurvivors.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    Animation weaponShootingAnimation;
    boolean shooting;
    boolean inActiveDock;
    float elapsedTime;


    public Weapon(TextureAtlas weaponAtlas, Texture cardTexture){
        weaponShootingAnimation = new Animation(1/50f,weaponAtlas.findRegions("shooting"));
        setCard(new Icon(cardTexture,Constantes.CARD_WIDTH,Constantes.CARD_HEIGHT));
        elapsedTime= 0;

    }



    @Override
    public void drawAttachable(Batch batch, float parentAlpha) {

        /*Aqui distinguiremos tres casos cuando esta disparando, cuando no esta en ´posición de disparo y cuando si lo esta, en el primer caso corremos la animacion
        * en el segundo no vamos a dibujar nada y en el tercero vamos a dibujar solo el primer cuadro de la animacion (cuando aun no esta dis
        * parando)*/
        if(shooting) {
            batch.draw(weaponShootingAnimation.getKeyFrame(elapsedTime), getX(), getY(), getWidth(), getHeight());
            elapsedTime = elapsedTime + Gdx.graphics.getDeltaTime();
            if(elapsedTime>weaponShootingAnimation.getAnimationDuration()){
                elapsedTime = 0;
                shooting = false;
            }
        }

        else if(inActiveDock){
            batch.draw(weaponShootingAnimation.getKeyFrame(0),getX(),getY(),getWidth(),getHeight());
        }
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public boolean isInActiveDock() {
        return inActiveDock;
    }

    public void setInActiveDock(boolean inActiveDock) {
        this.inActiveDock = inActiveDock;
    }

}
