package com.shipsurvivors.Utilities.SandBox;

import com.shipsurvivors.Entities.Attachable;

import java.util.List;

/**
 * Created by SEO on 25/12/2017.
 */

/*We'll assume that everything coming from SandBox, is in meters, so we need some convenient way to operate with arrays
* we are going to define here things like multiplication of a vector by scalar (how i miss Julia at times like these)*/
public class VectorOperations {

    public static float[] multiplyArrayByFloat(float[] array,float constant){
        float[] result = new float[array.length];

        for(int i = 0;i<array.length;i++){
            result[i] = array[i]*constant;
        }

        return result;
    }





}
