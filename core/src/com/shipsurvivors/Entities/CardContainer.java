package com.shipsurvivors.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.shipsurvivors.Utilities.Constantes;
import com.shipsurvivors.Utilities.TowerShelve;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SEO on 25/09/2017.
 */

/*The raining tube is where the cards will drop so that you can select them it will only be able to hold five of them at a
 * time */
public class CardContainer extends Table {

    private Attachable[] attachables;
    private final float RAINING_FREQUENCY = 5;
    private float clock;
    private Random random;
    private Attachable dummyAttachable;
    private Texture background;
    private boolean cardTaken;
    private Attachable grabbedCard;
    private Vector2[] cardIndexedPositions = new Vector2[Constantes.CONTAINER_CAPACITY];
    TowerShelve container  = new TowerShelve(Constantes.CONTAINER_CAPACITY);


    public CardContainer(Attachable[] attachables,Texture background){
        this.attachables = attachables;
        this.background = background;

        /*We apply filtering to the background, so that it looks pretty and also set its size*/
        this.background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setBounds(Constantes.SCREEN_WIDTH-Constantes.CARD_CONTAINER_WIDTH,(Constantes.SCREEN_HEIGHT-Constantes.CARD_CONTAINER_HEIGHT)/2,Constantes.CARD_CONTAINER_WIDTH,Constantes.CARD_CONTAINER_HEIGHT);

        //Initialize the clock to zero.
        clock = 0;

        // Fill the cardIndexedPositions
        for (int i = 0;i<Constantes.CONTAINER_CAPACITY;i++){
            cardIndexedPositions[i].x = getX() +(Constantes.CARD_CONTAINER_WIDTH-Constantes.CARD_WIDTH)/ 2;
            cardIndexedPositions[i].y = getY() + i*Constantes.CARD_HEIGHT;
        }


    }

    @Override
    public void act(float delta) {
        clock+=delta;
        if(clock>RAINING_FREQUENCY && container.size()<Constantes.CONTAINER_CAPACITY){
                dummyAttachable = attachables[random.nextInt(attachables.length)];

                if(dummyAttachable.isFree()) {
                    dummyAttachable.setInContainer(true);
                    dummyAttachable.setPosition(cardIndexedPositions[container.getTopIndex()+1].x,cardIndexedPositions[container.getTopIndex()+1].y);
                    try {
                        container.push(dummyAttachable);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    clock = 0;
                }
        }

        /*Here we call the act method of the card that is grabbed.*/
        if(isCardTaken()){
            grabbedCard.act(delta);
        }
    }

    public boolean isCardTaken() {
        return cardTaken;
    }

    public void setCardTaken(boolean cardTaken) {
        this.cardTaken = cardTaken;
    }

    public void setGrabbedCard(Attachable grabbedCard) {
        this.grabbedCard = grabbedCard;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //First draw the background
        batch.draw(background,getX(),getY(),getWidth(),getHeight());

        //Then draw the cards at its respective positions.
        for(Attachable attachable:container.getArray()){
            attachable.draw(batch,parentAlpha);
        }
    }

    public void grabCard(Vector2 touchCoordinates){
        for (Attachable card: container.getArray()){
            if(card.getStage().hit(touchCoordinates.x,touchCoordinates.y,false) == card ){
                setCardTaken(true);
                setGrabbedCard(card);
                container.grabItem(card);

                /*Now assign the new positions to the cards, remember that we have reindexed when we do container.grabItem
                * this looks kind of bad, but it wont execute too much during the run of the program, plus it is O(n), it could
                * be worse*/



                for (int i = 0;i<cardIndexedPositions.length;i++){
                    container.getArray()[i].setPosition(cardIndexedPositions[i].x,cardIndexedPositions[i].y);
                }
            }
        }

    }

}
