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
import com.shipsurvivors.Entities.Weapons.FootballCat;
import com.shipsurvivors.Entities.Weapons.HeartBreaker;

import java.util.ArrayList;
import java.util.List;

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

    public List<Weapon> weaponsRequest(String type, AssetManager manager, int numberOfWeapons){
        List<Weapon> weapons = new ArrayList<Weapon>();
        for (int i = 0;i<numberOfWeapons;i++){
            Weapon wep = weaponRequest(type,manager);
            weapons.add(wep);
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
            else if(type.equals("football_cat")){
                String urlTexture = jsonValue.get("weapon").get(type).getString("card");
                Texture cardTexture = manager.get(urlTexture,Texture.class);
                String urlAtlas = jsonValue.get("weapon").get(type).getString("atlas");
                TextureAtlas atlas = manager.get(urlAtlas,TextureAtlas.class);
                String urlBulletTexture = jsonValue.get("weapon").get(type).getString("bullet");
                Texture bulletTexture = manager.get(urlBulletTexture,Texture.class);

                return new FootballCat(atlas,cardTexture,bulletTexture,world);
            }

            else if(type.equals("heartbreaker")){
                String urlTexture = jsonValue.get("weapon").get(type).getString("card");
                Texture cardTexture = manager.get(urlTexture,Texture.class);
                String urlAtlas = jsonValue.get("weapon").get(type).getString("atlas");
                TextureAtlas atlas = manager.get(urlAtlas,TextureAtlas.class);
                String urlBulletTexture1 = jsonValue.get("weapon").get(type).getString("bullet1");
                Texture bulletTexture1 = manager.get(urlBulletTexture1,Texture.class);
                String urlBulletTexture2 = jsonValue.get("weapon").get(type).getString("bullet2");
                Texture bulletTexture2 = manager.get(urlBulletTexture2,Texture.class);

                return new HeartBreaker(atlas,cardTexture,bulletTexture1,bulletTexture2,world);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
