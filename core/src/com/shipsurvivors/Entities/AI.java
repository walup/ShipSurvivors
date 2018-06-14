package com.shipsurvivors.Entities;

import com.shipsurvivors.Utilities.Constantes;

import java.util.Random;

public class AI {
    /*This is the AI (which is not really smart enough to be called AI but whatever...)
    * this will control the flow of the game. will be able to change the rock size and maybe
    * make things a little harder as the time goes by i still haven't figured out
    * all the details of what how this class will work exactly. */

    private float gameTime;
    private float score;
    private final float[] START_PROBABILITIES = {0.7f,0.2f,0.09f,0.01f};
    private float[] probRockSizes = {0.7f,0.2f,0.09f,0.01f};
    private Random random;

    //These are used to select rocks.
    private float cumulative;
    private float value;

    //Here we have some variables for the changing of the prob. distribution.
    private float step = 0;
    private float time = 0;
    private final float STEP_TIME = 10;
    private final float STEP_TOP = 10;
    private float increment = 0;
    private float decrement = 0;

    /*I don't know anything about AI , but let's try to make some kind of probabiliistic thing. What i'm thinking is i should make some
     * sort of probability distribution that will be modified, according to the players points.*/

    public AI(){
        /*Initialize the random */
        random = new Random();

    }

    /*Esto arroja un tamaño de roca aleatorio, acorde a la distribucion de probas dad en el arreglo probRockSizes*/

    public float selectRockSize(){
        value = random.nextFloat();
        cumulative = 0;
        for(int i = 0;i<probRockSizes.length;i++) {
            /*Different cases according to the value obtained*/
            if (value > cumulative && value < cumulative + probRockSizes[i]) {
                System.out.println("returned "+Constantes.ROCK_SIZES[i]);
                return Constantes.ROCK_SIZES[i];
            }
            cumulative += probRockSizes[i];
        }
        return 0;
    }

    /*This is the part where the probability distribution will be updated, according to the player points
    * and the gameTime*/

    public void updateAI(float delta, float score){
        this.time +=delta;
        if(score>this.score){
            this.score = score;
            step+=1;
        }

        if(time>STEP_TIME){
            step+=1;
            time = 0;
        }

        if(step>STEP_TOP){
            adjustProbability();
            step = 0;
        }
    }

    private void adjustProbability(){
        /*We would need to have in mind one top dificulty scenerio. What i think could be adequate would be basically all the probability on
        * the giant rock, which would be achieved after several minutes of course. then we reset to the original config i'm just going to
        * try somthing i have in mind*/

        for(int i = 1; i<probRockSizes.length; i++){
            increment  = increment(i-1);
            probRockSizes[i]+=increment;
            probRockSizes[i-1]-=increment;

            /*Si se supera una probabilidad de 0.9 para la ultima roca volvemos al esquema original de probabilidades*/
            if(probRockSizes[probRockSizes.length-1]>0.90){
                probRockSizes = START_PROBABILITIES;
            }
        }

        for(int i = 0;i<probRockSizes.length; i++) {
            System.out.println("Prob Rock "+i + " " +probRockSizes[i]);
        }
    }

    private float increment (int i ){
        return probRockSizes[i]/2;
    }
}
