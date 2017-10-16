package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.Icon;

/**
 * Created by SEO on 25/09/2017.
 */
public class Weapon extends Attachable {
    Animation weaponShootingAnimation;
    float timeElapsed;



    public Weapon(TextureAtlas weaponAtlas){
        weaponShootingAnimation = new Animation(1/50f,weaponAtlas.findRegions("shooting"));
        timeElapsed = 0;
        setCard(new Icon(weaponShootingAnimation.getKeyFrame(0).getTexture(), (float) Constantes.CARD_WIDTH,(float)Constantes.CARD_HEIGHT));
    }
}
