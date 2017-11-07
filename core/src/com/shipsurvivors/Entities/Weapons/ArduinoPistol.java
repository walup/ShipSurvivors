package com.shipsurvivors.Entities.Weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shipsurvivors.Entities.Weapon;

/**
 * Created by SEO on 05/11/2017.
 */
public class ArduinoPistol extends Weapon {
    TextureRegion bulletTexture;

    public ArduinoPistol(TextureAtlas weaponAtlas,Texture cardTexture) {
        super(weaponAtlas,cardTexture);
        bulletTexture = weaponAtlas.findRegion("bullet");
    }


}
