package com.shipsurvivors.UI;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.shipsurvivors.Entities.CardContainer;
import com.shipsurvivors.Entities.Ship;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 02/10/2017.
 */
public class ShipControls implements GestureDetector.GestureListener{

    private Ship ship;
    private CardContainer cardContainer;
    private Vector2 mouseScreenPosition;
    private Vector2 mouseStagePosition;



    public ShipControls(Ship ship, CardContainer cardContainer){
        this.ship = ship;
        this.cardContainer = cardContainer;
        mouseScreenPosition = new Vector2();
        mouseStagePosition = new Vector2();

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        //We want to see where it touched
        mouseScreenPosition.x = x;
        mouseScreenPosition.y = y;
        mouseStagePosition = ship.getStage().screenToStageCoordinates(mouseScreenPosition);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //If the player flinged in the ship we will rotate the wheel
        if(ship.getStage().hit(mouseStagePosition.x,mouseStagePosition.y,false) ==ship){
            ship.getWheel().rotate((velocityX+velocityY)/(2* Constantes.PIXELS_IN_METER));
        }
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        if(cardContainer.getStage().hit(mouseStagePosition.x,mouseStagePosition.y,false) == cardContainer){
            cardContainer.grabCard(mouseStagePosition);
        }


        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        /*Here when the pan stops we will check first if a card was being grabbed
        * and also if the final position is that of a dock.*/
       if(cardContainer.isCardTaken()){
          //Here goes the condiction that the final position is a dock


           //Here we reset the card taken to its original position
           cardContainer.setCardTaken(false);
       }


        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
