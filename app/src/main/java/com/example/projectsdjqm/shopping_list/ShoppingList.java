/**
 * Classname: ShoppingList
 *
 * Version information: Version 1
 *
 * Date: 2022/09/29
 */

package com.example.projectsdjqm.shopping_list;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import java.util.ArrayList;

public class ShoppingList {
    /*
     * Each ShoppingList class has the following:
     * An Ingredient List
     * Value to determine if ingredient has been picked up
     */
    private ArrayList<Ingredient> listOfIngredients;
    private boolean pickedUp;


    public ShoppingList(ArrayList<Ingredient> listOfIngredients, boolean pickedUp) {
        this.listOfIngredients = listOfIngredients;
        this.pickedUp = pickedUp;
    }

    /* Getters */
    public ArrayList<Ingredient> getListOfIngredients() {return listOfIngredients;}

    public boolean getPickedUp() {return pickedUp;}

    /* Setters */
    public void setListOfIngredients(ArrayList<Ingredient> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

}
