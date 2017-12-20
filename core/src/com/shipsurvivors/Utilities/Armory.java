package com.shipsurvivors.Utilities;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.shipsurvivors.Entities.Weapon;

/**
 * Created by SEO on 19/12/2017.
 */
public class Armory {
    FileHandle jsonFile;
    public Armory(FileHandle jsonFile){
        this.jsonFile = jsonFile;
    }

    public Weapon[] weaponsRequest(String type, int numberOfWeapons){
        Weapon[] weapons = new Weapon[numberOfWeapons];
        for (int i = 0;i<weapons.length;i++){
            Weapon wep = weaponRequest(type);
            weapons[i] = wep;
        }
        return weapons;
    }

    public Weapon weaponRequest(String type){
        Weapon weapon;
        JsonReader jsonReader = new JsonReader();
        String rawString = jsonFile.readString();
        JsonValue jsonValue = jsonReader.parse(rawString);

        /*Now deliver the weapon*/
        try {
            if (type.equals("arduino_gun")) {
            /*We need two things, an atlas, and a Texture for the card*/
                String urlTexture = jsonValue.get("weapon").get(type).getString("card");
                Texture texture = new Texture(urlTexture);
                String urlAtlas = jsonValue.get("weapon").get(type).getString("atlas");
                TextureAtlas atlas = new TextureAtlas(urlAtlas);

                return new Weapon(atlas,texture);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
