package com.shipsurvivors.Utilities;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.shipsurvivors.Entities.Weapon;
import com.shipsurvivors.Entities.Weapons.ArduinoPistol;

/**
 * Created by SEO on 19/12/2017.
 */
public class Armory {
    FileHandle jsonFile;
    World world;
    public Armory(FileHandle jsonFile,World world){
        this.jsonFile = jsonFile;
        this.world = world;
    }

    public Weapon[] weaponsRequest(String type, AssetManager manager,int numberOfWeapons){
        Weapon[] weapons = new Weapon[numberOfWeapons];
        for (int i = 0;i<weapons.length;i++){
            Weapon wep = weaponRequest(type,manager);
            weapons[i] = wep;
        }
        return weapons;
    }

    public Weapon weaponRequest(String type, AssetManager manager){
        Weapon weapon;
        JsonReader jsonReader = new JsonReader();
        String rawString = jsonFile.readString();
        JsonValue jsonValue = jsonReader.parse(rawString);

        /*Now deliver the weapon*/
        try {
            if (type.equals("arduino_gun")) {
            /*We need two things, an atlas, and a Texture for the card*/
                String urlTexture = jsonValue.get("weapon").get(type).getString("card");
                Texture texture = manager.get(urlTexture,Texture.class);
                String urlAtlas = jsonValue.get("weapon").get(type).getString("atlas");
                TextureAtlas atlas =manager.get(urlAtlas,TextureAtlas.class);
                String urlBulletTexture = jsonValue.get("weapon").get(type).getString("bullet");
                Texture bulletTexture = manager.get(urlBulletTexture);

                return new ArduinoPistol(atlas,texture,bulletTexture,world);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
