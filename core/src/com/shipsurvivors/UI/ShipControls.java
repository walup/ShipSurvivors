package com.shipsurvivors.UI;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.shipsurvivors.Entities.CardContainer;
import com.shipsurvivors.Entities.Ship;
import com.shipsurvivors.Screens.GameScreen;
import com.shipsurvivors.Utilities.Constantes;

/**
 * Created by SEO on 02/10/2017.
 */
public class ShipControls extends ApplicationAdapter implements GestureDetector.GestureListener, InputProcessor {
    private Ship ship;
    private CardContainer cardContainer;
    private Vector2 mouseScreenPosition;
    private static Vector2 mouseStagePosition;
    private static Boolean flingIntent = false;
    private static float flingVelocityX, flingVelocityY;
    private static Boolean touchIntent = false;
    private GameScreen.Dj dj;

    public ShipControls(Ship ship, GameScreen.Dj dj, CardContainer cardContainer){
        this.ship = ship;
        this.cardContainer = cardContainer;
        mouseScreenPosition = new Vector2();
        mouseStagePosition = new Vector2();
        this.dj = dj;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        //If it touched we put touchIntent as true
        setTouchIntent(true);
        //We want to see where it touched
        mouseScreenPosition.x = x;
        mouseScreenPosition.y = y;
        mouseStagePosition = ship.getStage().screenToStageCoordinates(mouseScreenPosition);

        if(ship.getWheel().touchedDockToTrigger(mouseStagePosition)){
            ship.getWheel().triggerDock();
            dj.playSummonSound();
        }

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
        else{
            flingIntent = true;
            flingVelocityX = velocityX;
            flingVelocityY = velocityY;
        }
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if(cardContainer.getStage().hit(mouseStagePosition.x,mouseStagePosition.y,false) == cardContainer && !cardContainer.isCardTaken()){
            cardContainer.grabCard(mouseStagePosition);
            if(cardContainer.getGrabbedCard()!= null) {
                cardContainer.getGrabbedCard().setPointer(mouseStagePosition);
            }
            setTouchIntent(false);
        }
        else if(cardContainer.isCardTaken()){
            /*If a card is taken then we will update its pointer so that it goes where we are dragging our finger*/
            mouseScreenPosition.x = x;
            mouseScreenPosition.y = y;
            mouseStagePosition = cardContainer.getStage().screenToStageCoordinates(mouseScreenPosition);
            cardContainer.getGrabbedCard().setPointer(mouseStagePosition);
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        /*If the ship is moving we will stop it, this is sort of a complement
        * to the touch up mechanism for stopping the ship. */
        if(ship.isMoving()){
            ship.stopMovement();
        }

        /*Here when the pan stops we will check first if a card was being grabbed
        * and also if the final position is that of a dock.*/
       if(cardContainer.isCardTaken()){
          //Here goes the condiction that the final position is a dock
           if(ship.getWheel().touchedAvailableDock(cardContainer.getGrabbedCard().getCardRectangle())){
               ship.getWheel().attachDock(cardContainer.getGrabbedCard());

               /*Ok so, now we just need to tell the container to pop the card out, to leave space for a new one. */
               try {
                   cardContainer.cardAttached();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }

           //For now we are just going to return the card to the deck
           else {
               cardContainer.returnCardToDeck();
           }
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


    public static Boolean getFlingIntent() {
        return flingIntent;
    }

    public static Vector2 getMouseStagePosition() {
        return mouseStagePosition;
    }

    public static float getFlingVelocityX() {
        return flingVelocityX;
    }

    public static float getFlingVelocityY() {
        return flingVelocityY;
    }

    public static void setFlingIntent(Boolean flingIntent) {
        ShipControls.flingIntent = flingIntent;
    }

    public void renderShipControls(){
        if(flingIntent = true){
            flingIntent = false;
        }
    }

    public static Boolean getTouchIntent() {
        return touchIntent;
    }

    public static void setTouchIntent(Boolean touchIntent) {
        ShipControls.touchIntent = touchIntent;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(ship.isMoving()){
            ship.stopMovement();
        }
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY){
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void dispose(){
        ship.dispose();
        dj.dispose();
        cardContainer.dispose();
    }
}
