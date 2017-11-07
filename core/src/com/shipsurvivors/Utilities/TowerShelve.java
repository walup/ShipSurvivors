package com.shipsurvivors.Utilities;

import com.shipsurvivors.Entities.Attachable;

/**
 * Created by SEO on 06/11/2017.
 */
public class TowerShelve  {

    /*This is the Tower Shelve class. it is kind of a stack but not exactly, first, although we will
    be putting everything in the top we want to be able to take out any element we want, and to reindex things
    in the appropiate way*/

    protected int capacity;
    protected final int CAPACITY = 5;
    protected Attachable[] array;
    protected int top = -1;


    public TowerShelve(int capacity){
        this.capacity = capacity;

        //Initialize the array

        array = new Attachable[capacity];
     }

    //this will give us the number of elements
    public int size(){
        return top+1;
    }

    //This tells us if the shelve is empty
    public boolean isEmpty(){
        return (top<0);
    }

    //This will push an element
    public void push(Attachable data) throws Exception{
        if(size()==capacity){
            throw new Exception("Your shelve is full");
        }
        else{
            array[++top] =data;
        }

    }

    public Attachable pop() throws Exception{
        Attachable data;
        if(isEmpty()){
            throw new Exception("Your shelve is empty!");
        }
        else{
            data = array[top];
            array[top--] = null;
        }
        return data;
    }

    /*Now this is our addition to what you would usually call a stack
    * it is a function that basically reorders things so that your item goes to the top*/

    public void grabItem(Attachable attachable){
        //First we need to know the index of our attachable
        int index = 0;
        for (int i = 0;i<array.length;i++){
            if(array[i].equals(attachable)){
                index = i;
            }
        }
        //Now we make things so that the grabbed object is in the top
        for (int j = index;j<top;j++){
            Attachable temp = array[j+1];
            array[j+1] = array[j];
            array[j] = temp;
        }

    }

    public Attachable[] getArray() {
        return array;
    }

    public int getTopIndex(){
        return top+1;
    }
}
