package com.shipsurvivors.Utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by SEO on 15/10/2017.
 */
public class ScrollingBackground extends Actor {
    private Texture background;
    private float x1,x2;
    private float speed;

    public ScrollingBackground(Texture background){
        /*Always apply a filter to the background so it doesn't look crappy*/
        this.background = background;
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setSize(background.getWidth(),Constantes.SCREEN_HEIGHT);
        x1= 0;
        x2 =getWidth();
        speed = 50;
    }


    @Override
    public void act(float delta) {

        /*We accelerate the scrolling */
        if(speed<Constantes.MAX_SCROLLING_SPEED){
            speed+=Constantes.SCROLL_ACCELERATION*delta;
            if(speed>Constantes.MAX_SCROLLING_SPEED){
                speed = Constantes.MAX_SCROLLING_SPEED;
            }
        }

        /*Advance the background.*/
        x1-=speed*delta;
        x2-=speed*delta;

        /*When the position of the image is less than zero, we put it on top of the one that is
        * currently displayed. */
        if(x1+getWidth()<=0) {
           x1 = x2 + getWidth();

       }
        if(x2+getWidth()<=0){
            x2 = x1+getWidth();
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background,x1,0,getWidth(),getHeight());
        batch.draw(background,x2,0,getWidth(),getHeight());
    }

    public void dispose(){
        background.dispose();
    }
}
